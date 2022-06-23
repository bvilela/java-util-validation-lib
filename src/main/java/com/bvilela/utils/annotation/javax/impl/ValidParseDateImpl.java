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
	private boolean isRequired;

	@Override
	public void initialize(ValidParseDate annotation) {
		this.pattern = annotation.pattern();
		this.locale = AppUtils.getLocale(annotation.locale());
		this.isRequired = annotation.required();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {

			if (Objects.isNull(value)) {
				overriteMessage(context);
				return !this.isRequired;
			}

			boolean isString = value instanceof String;
			if (!isString) {
				throw new IllegalArgumentException("'@ValidParseDate' can use only in String field.");
			}

			String text = (String) value;
			if (text.isBlank()) {
				overriteMessage(context);
				return !this.isRequired;
			}

			AppUtils.parse(text, this.pattern, this.locale);

			return true;

		} catch (DateTimeParseException e) {
			return false;
		}
	}

	private void overriteMessage(ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(
				"{bvilela.lib.util.validation.ValidParseDate.messageRequired}").addConstraintViolation();
	}

}
