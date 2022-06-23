package com.bvilela.utils.annotation.javax;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.bvilela.utils.annotation.javax.impl.ValidParseDateImpl;

/**
 * The annotated element must be {@code String} and must contain a valid date
 * according to pattern and locale.
 * 
 * @param pattern - Pattern to valid/parse String Date. Default "dd/MM/yyyy".
 *                For more details, see
 *                {@link java.time.format.DateTimeFormatterBuilder#appendPattern(String)}
 * 
 *                <p>
 * @param locale  - Locale of Date input. Default "pt_BR". For more details, see
 *                {@link java.util.Locale}
 * 
 *                <p>
 * @param parse   - Indicates whether the field will be converted to LocalDate.
 *                Default: Boolean.False
 *                <p>
 *                A field with the same name is required, with "Converted" at
 *                the end of the name. Example:
 * 
 *                <pre>
 * {@code
 * ValidParseDate(parse = true)
 * private String myDate;
 * private LocalDate myDateConverted;
 * }
 * </pre>
 * 
 * @author Bruno Vilela
 * @since 0.0.1
 *
 */

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidParseDateImpl.class)
@Documented
public @interface ValidParseDate {

	String message() default "{bvilela.lib.util.validation.ValidParseDate.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String value() default "";

	String pattern() default "dd/MM/yyyy";

	String locale() default "pt_BR";

	boolean parse() default false;
	
	boolean required() default true;
	
}
