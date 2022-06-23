package com.bvilela.utils.annotation.javax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;

import com.bvilela.utils.ValidationUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

class ValidDateTest {

	@AllArgsConstructor
	private class MyTestDTO1 {
		@ValidParseDate
		private String date;
	}

	@Test
	void shouldSuccessCase1() {
		checkValidateDtoNoViolations(new MyTestDTO1("01/01/2022"));
	}

	@Test
	void shouldSuccessCase2() {
		checkValidateDtoNoViolations(new MyTestDTO1("13/06/2022"));
	}

	@Test
	void shouldSuccessCase3() {
		checkValidateDtoNoViolations(new MyTestDTO1("31/12/2022"));
	}

	@Test
	void shouldExceptionDateNull() {
		baseException(new MyTestDTO1(null));
	}

	@Test
	void shouldExceptionDateEmpty() {
		baseException(new MyTestDTO1(""));
	}

	@Test
	void shouldExceptionDateBlank() {
		baseException(new MyTestDTO1(" "));
	}

	@Test
	void shouldExceptionDateInvalidCase1() {
		baseException(new MyTestDTO1("abc"));
	}

	@Test
	void shouldExceptionDateInvalidCase2() {
		baseException(new MyTestDTO1("31-02-2022"));
	}

	@Test
	void shouldExceptionDateInvalidCase3() {
		baseException(new MyTestDTO1("01-01-2022"));
	}

	private void baseException(MyTestDTO1 dto) {
		checkMessageInvalidDate(ValidationUtils.validateDto(dto));
	}

	private void checkMessageInvalidDate(List<ConstraintViolation<Object>> errors) {
		assertEquals(1, errors.size());
		assertEquals("Value is a invalid date", errors.get(0).getMessage());
		assertEquals("date", errors.get(0).getPropertyPath().toString());
	}

	@AllArgsConstructor
	private class MyTestDTO2 {
		@ValidParseDate(locale = "")
		private String date;
	}

	@Test
	void shouldExceptionLocaleEmpty() {
		baseExceptionLocale(new MyTestDTO2("01/01/2022"));
	}

	@AllArgsConstructor
	private class MyTestDTO3 {
		@ValidParseDate(locale = " ")
		private String date;
	}

	@Test
	void shouldExceptionLocaleBlank() {
		baseExceptionLocale(new MyTestDTO3("01/01/2022"));
	}

	private <T> void baseExceptionLocale(T dto) {
		var ex = assertThrows(ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
		assertEquals("HV000032: Unable to initialize com.bvilela.utils.annotation.javax.impl.ValidParseDateImpl.",
				ex.getMessage());
		assertEquals(IllegalArgumentException.class, ex.getCause().getClass());
		assertEquals("Param 'locale' must not be Blank.", ex.getCause().getMessage());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO4 {
		@ValidParseDate
		private String date;
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateFalse() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var myDate = "01/01/2022";
		var dto = new MyTestDTO4(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertNull(dto.getDateConverted());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO5 {
		@ValidParseDate(parse = true)
		private String date;

		@Setter
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateSuccess() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var myDate = "02/01/2022";
		var dto = new MyTestDTO5(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 1, 2), dto.getDateConverted());
	}

	@Test
	void shouldParseDateInvalidDate() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var myDate = "02-01-2022";
		var dto = new MyTestDTO5(myDate, null);
		checkMessageInvalidDate(ValidationUtils.validateParseDto(dto));
		assertEquals(myDate, dto.getDate());
		assertNull(dto.getDateConverted());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO6 {
		@ValidParseDate(parse = true)
		private String date;
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateExceptionMethodNotFound() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "03/01/2022";
		var dto = new MyTestDTO6(myDate, null);
		var ex = assertThrows(NoSuchMethodException.class, () -> ValidationUtils.validateParseDto(dto));
		assertTrue(ex.getMessage().contains("setDateConverted(java.time.LocalDate"));
		assertEquals(myDate, dto.getDate());
		assertNull(dto.getDateConverted());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO7 {
		@ValidParseDate(parse = true, pattern = "dd MMMM yyyy")
		private String date;

		@Setter
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateSuccessMonthMMM() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var myDate = "01 janeiro 2022";
		var dto = new MyTestDTO7(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 1, 1), dto.getDateConverted());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO8 {
		@ValidParseDate(parse = true, pattern = "dd MMMM yyyy", locale = "en")
		private String date;

		@Setter
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateSuccessMonthMMMEnglish1() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01 January 2022";
		var dto = new MyTestDTO8(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 1, 1), dto.getDateConverted());
	}

	@Test
	void shouldParseDateSuccessMonthMMMEnglish2() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01 february 2022";
		var dto = new MyTestDTO8(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 2, 1), dto.getDateConverted());
	}

	@Getter
	@AllArgsConstructor
	public class MyTestDTO9 {
		@ValidParseDate(parse = true, pattern = "yyyy dd MMMM", locale = "de_DE")
		private String date;

		@Setter
		private LocalDate dateConverted;
	}

	@Test
	void shouldParseDateSuccessGermany() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var myDate = "2022 15 Oktober";
		var dto = new MyTestDTO9(myDate, null);
		checkValidateParseDtoNoViolations(dto);
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 10, 15), dto.getDateConverted());
	}

	@AllArgsConstructor
	private class MyTestDTO10<T> {
		@ValidParseDate
		private T date;
	}

	@Test
	void shouldParseDateInvalidTypeFieldInteger() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Integer>(123);
		checkInvalidTypeField(dto);
	}

	@Test
	void shouldParseDateInvalidTypeFieldLocalDate() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<LocalDate>(LocalDate.now());
		checkInvalidTypeField(dto);
	}

	@Test
	void shouldParseDateInvalidTypeFieldBigDecimal() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<BigDecimal>(BigDecimal.ONE);
		checkInvalidTypeField(dto);
	}

	@Test
	void shouldParseDateInvalidTypeFieldList() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<List<String>>(List.of());
		checkInvalidTypeField(dto);
	}

	@Test
	void shouldParseDateInvalidTypeFieldFloat() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Float>(1.2f);
		checkInvalidTypeField(dto);
	}

	@Test
	void shouldParseDateInvalidTypeFieldObject() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Object>(new MyTestDTO1("test"));
		checkInvalidTypeField(dto);
	}

	private <T> void checkInvalidTypeField(T dto) {
		var ex = assertThrows(ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
		assertEquals("HV000028: Unexpected exception during isValid call.", ex.getMessage());
		assertEquals(IllegalArgumentException.class, ex.getCause().getClass());
		assertEquals("'@ValidParseDate' can use only in String field.", ex.getCause().getMessage());
	}

	private <T> void checkValidateParseDtoNoViolations(T dto) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		assertTrue(ValidationUtils.validateParseDto(dto).isEmpty());
	}

	private <T> void checkValidateDtoNoViolations(T dto) {
		assertTrue(ValidationUtils.validateDto(dto).isEmpty());
	}

}
