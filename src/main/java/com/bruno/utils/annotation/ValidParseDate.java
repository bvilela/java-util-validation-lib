package com.bruno.utils.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.bruno.utils.annotation.impl.ValidParseDateImpl;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidParseDateImpl.class)
@Documented
public @interface ValidParseDate {
	
    String message() default "Value not is a valid date";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
 
    String value() default "";
    
    String pattern() default "dd-MM-yyyy";
    
    String locale() default "pt_BR"; 
    
    boolean parse() default true;
	
}
