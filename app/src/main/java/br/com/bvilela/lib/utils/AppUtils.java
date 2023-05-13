package br.com.bvilela.lib.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public final class AppUtils {

	private AppUtils() {
	}

	public static LocalDate parse(String date, String pattern, Locale locale) throws DateTimeParseException {
		pattern = pattern.replace("y", "u");
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(pattern)
				.toFormatter(locale).withResolverStyle(ResolverStyle.STRICT);
		return LocalDate.parse(date, formatter);
	}
	
	public static String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	public static Locale getLocale(String locale) {
		if (locale.isBlank()) {
			throw new IllegalArgumentException("Param 'locale' must not be Blank.");
		}
	
		var splitted = locale.split("_");
		String language = splitted[0];
		String country = "";
		
		if (splitted.length > 1) {
			country = locale.split("_")[1];
		}
		
		return new Locale(language, country);
	}

}
