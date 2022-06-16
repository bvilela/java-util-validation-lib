package com.bruno.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import com.bruno.utils.annotation.ValidParseDate;

public final class AnnotationUtils {

	private AnnotationUtils() {
	}

	public static <T> void dtoParseDates(T dto) throws Exception {
		String setMethodNameConverted = null;
		if (Objects.isNull(dto)) {
			return;
		}

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
