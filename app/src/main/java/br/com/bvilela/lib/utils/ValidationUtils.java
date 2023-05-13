package br.com.bvilela.lib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import br.com.bvilela.lib.utils.annotation.javax.ValidParseDate;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static <T> List<ConstraintViolation<Object>> validateDto(T dto) {
        Set<ConstraintViolation<Object>> violations =
                Validation.buildDefaultValidatorFactory().getValidator().validate(dto);

        if (violations.isEmpty()) {
            return Collections.emptyList();
        }

        return violations.stream().map(e -> e).collect(Collectors.toList());
    }

    public static <T> List<ConstraintViolation<Object>> validateParseDto(T dto)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException {
        var violations = validateDto(dto);
        if (violations.isEmpty()) {
            parseDatesDto(dto);
        }
        return violations;
    }

    private static <T> void parseDatesDto(T dto)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException {
        String setMethodNameConverted = null;

        for (Field field : dto.getClass().getDeclaredFields()) {
            ValidParseDate annotation = field.getDeclaredAnnotation(ValidParseDate.class);

            if (Objects.nonNull(annotation) && annotation.parse()) {
                String fieldName = AppUtils.capitalize(field.getName());
                String getMethodName = "get".concat(fieldName);
                Method getMethod = dto.getClass().getMethod(getMethodName);

                String value = (String) getMethod.invoke(dto);

                if (Objects.nonNull(value)) {
                    Locale locale = AppUtils.getLocale(annotation.locale());
                    LocalDate valueConverted = AppUtils.parse(value, annotation.pattern(), locale);

                    setMethodNameConverted = "set".concat(fieldName).concat("Converted");
                    Method setMethodConverted =
                            dto.getClass().getMethod(setMethodNameConverted, LocalDate.class);
                    setMethodConverted.invoke(dto, valueConverted);
                }
            }
        }
    }
}
