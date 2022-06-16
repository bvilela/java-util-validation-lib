package com.bruno.utils;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

public class TestUtils {

	public static <T> List<ConstraintViolation<Object>> validateDto(T obj) {
		Set<ConstraintViolation<Object>> violations = Validation.buildDefaultValidatorFactory().getValidator()
				.validate(obj);

		if (!violations.isEmpty()) {
			List<ConstraintViolation<Object>> errors = violations.stream().map(e -> e).toList();
			return errors;
		}
		return null;
	}
}
