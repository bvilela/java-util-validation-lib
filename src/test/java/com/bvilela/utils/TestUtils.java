package com.bvilela.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

public final class TestUtils {

	private TestUtils() {
	}

	public static <T> List<ConstraintViolation<Object>> validateDto(T dto) {
		Set<ConstraintViolation<Object>> violations = Validation.buildDefaultValidatorFactory().getValidator()
				.validate(dto);

		if (!violations.isEmpty()) {
			List<ConstraintViolation<Object>> errors = violations.stream().map(e -> e).collect(Collectors.toList());
			return errors;
		}

		return null;
	}

	public static <T> List<ConstraintViolation<Object>> validateParseDto(T dto) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var list = validateDto(dto);
		if (Objects.isNull(list)) {
			AnnotationUtils.parseDatesDto(dto);
		}
		return list;
	}

}
