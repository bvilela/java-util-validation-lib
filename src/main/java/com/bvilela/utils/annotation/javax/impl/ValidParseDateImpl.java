package com.bvilela.utils.annotation.javax.impl;

import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bvilela.utils.AppUtils;
import com.bvilela.utils.annotation.javax.ValidParseDate;

public class ValidParseDateImpl implements ConstraintValidator<ValidParseDate, Object> {

	private String pattern;
	private Locale locale;

	@Override
	public void initialize(ValidParseDate annotation) {
		this.pattern = annotation.pattern();
		this.locale = AppUtils.getLocale(annotation.locale());
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			
			if (Objects.isNull(value)) {
				return false;
			}
			
			boolean isString = value instanceof String; 
			if (!isString) {
				throw new IllegalArgumentException("'@ValidParseDate' can use only in String field.");
			}
			
			var text = (String) value;
			if (text.isBlank()) {
				return false;
			}

			AppUtils.parse(text, this.pattern, this.locale);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

}
