package com.bruno.utils.annotation.impl;

import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bruno.utils.AppUtils;
import com.bruno.utils.annotation.ValidParseDate;

public class ValidParseDateImpl implements ConstraintValidator<ValidParseDate, String> {
	
	private String pattern;
	private Locale locale;

    @Override
    public void initialize(ValidParseDate constraintAnnotation) {
        constraintAnnotation.value();
        this.pattern = constraintAnnotation.pattern();
        this.locale = AppUtils.getLocale(constraintAnnotation.locale());
    }
 
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (Objects.isNull(value) || value.isBlank()) {
                return false;
            }
            AppUtils.parse(value, this.pattern, this.locale);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
	
}
