package com.bruno.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class AppUtils {
	
	private AppUtils() {
	}

	public static LocalDate parse(String date, String pattern, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale);
		return LocalDate.parse(date, formatter);
	}
	
	public static String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
	
	public static Locale getLocale(String locale) {
        if (Objects.isNull(locale) || locale.isBlank()) {
        	throw new IllegalArgumentException("Param locate can't Null or Blank");
        }
        
        String language = locale.split("_")[0];
        String country = locale.split("_")[1];
        return new Locale(language, country);
	}

}
