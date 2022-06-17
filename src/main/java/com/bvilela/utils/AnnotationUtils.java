package com.bvilela.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import com.bvilela.utils.annotation.javax.ValidParseDate;

public final class AnnotationUtils {

	private AnnotationUtils() {
	}

	public static <T> void parseDatesDto(T dto) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String setMethodNameConverted = null;

		for (Field field : dto.getClass().getDeclaredFields()) {
			ValidParseDate annotation = field.getDeclaredAnnotation(ValidParseDate.class);

			if (Objects.nonNull(annotation) && annotation.parse()) {
				String fieldName = AppUtils.capitalize(field.getName());
				String getMethodName = "get".concat(fieldName);
				Method getMethod = dto.getClass().getMethod(getMethodName);

				String value = (String) getMethod.invoke(dto);
				Locale locale = AppUtils.getLocale(annotation.locale());
				LocalDate valueConverted = AppUtils.parse(value, annotation.pattern(), locale);

				setMethodNameConverted = "set".concat(fieldName).concat("Converted");
				Method setMethodConverted = dto.getClass().getMethod(setMethodNameConverted, LocalDate.class);
				setMethodConverted.invoke(dto, valueConverted);
			}
		}
	}
	
}
