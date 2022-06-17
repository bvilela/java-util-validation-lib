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

import com.bvilela.utils.TestUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

class TestValidDate {

	@AllArgsConstructor
	private class MyTestDTO1 {
		@ValidParseDate
		private String date;
	}

	@Test
	void testSuccessCase1() {
		assertNull(TestUtils.validateDto(new MyTestDTO1("01/01/2022")));
	}

	@Test
	void testSuccessCase2() {
		assertNull(TestUtils.validateDto(new MyTestDTO1("13/06/2022")));
	}
	
	@Test
	void testSuccessCase3() {
		assertNull(TestUtils.validateDto(new MyTestDTO1("31/12/2022")));
	}
	
	@Test
	void testExceptionDateNull() {
		baseException(new MyTestDTO1(null));
	}
	
	@Test
	void testExceptionDateEmpty() {
		baseException(new MyTestDTO1(""));
	}
	
	@Test
	void testExceptionDateBlank() {
		baseException(new MyTestDTO1(" "));
	}
	
	@Test
	void testExceptionDateInvalidCase1() {
		baseException(new MyTestDTO1("abc"));
	}
	
	@Test
	void testExceptionDateInvalidCase2() {
		baseException(new MyTestDTO1("31-02-2022"));
	}
	
	@Test
	void testExceptionDateInvalidCase3() {
		baseException(new MyTestDTO1("01-01-2022"));
	}
	
	private void baseException(MyTestDTO1 dto) {
		checkMessageInvalidDate(TestUtils.validateDto(dto));
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
	void testExceptionLocaleEmpty() {
		baseExceptionLocale(new MyTestDTO2("01/01/2022"));
	}
	
	@AllArgsConstructor
	private class MyTestDTO3 {
		@ValidParseDate(locale = " ")
		private String date;
	}
	
	@Test
	void testExceptionLocaleBlank() {
		baseExceptionLocale(new MyTestDTO3("01/01/2022"));
	}
	
	private <T> void baseExceptionLocale(T dto) {
		var ex = assertThrows(ValidationException.class, () -> TestUtils.validateParseDto(dto));
		assertEquals("HV000032: Unable to initialize com.bvilela.utils.annotation.javax.impl.ValidParseDateImpl.", ex.getMessage());
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
	void testParseDateFalse() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01/01/2022";
		var dto = new MyTestDTO4(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
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
	void testParseDateSuccess() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "02/01/2022";
		var dto = new MyTestDTO5(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 1, 2), dto.getDateConverted());
	}
	
	@Test
	void testParseDateInvalidDate() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "02-01-2022";
		var dto = new MyTestDTO5(myDate, null);
		checkMessageInvalidDate(TestUtils.validateParseDto(dto));
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
	void testParseDateExceptionMethodNotFound() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "03/01/2022";
		var dto = new MyTestDTO6(myDate, null);
		var ex = assertThrows(NoSuchMethodException.class, () -> TestUtils.validateParseDto(dto));
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
	void testParseDateSuccessMonthMMM() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01 janeiro 2022";
		var dto = new MyTestDTO7(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
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
	void testParseDateSuccessMonthMMMEnglish1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01 January 2022";
		var dto = new MyTestDTO8(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 1, 1), dto.getDateConverted());
	}
	
	@Test
	void testParseDateSuccessMonthMMMEnglish2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "01 february 2022";
		var dto = new MyTestDTO8(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
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
	void testParseDateSuccessGermany() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var myDate = "2022 15 Oktober";
		var dto = new MyTestDTO9(myDate, null);
		assertNull(TestUtils.validateParseDto(dto));
		assertEquals(myDate, dto.getDate());
		assertEquals(LocalDate.of(2022, 10, 15), dto.getDateConverted());
	}
	
	@AllArgsConstructor
	private class MyTestDTO10<T> {
		@ValidParseDate
		private T date;
	}
	
	@Test
	void testParseDateInvalidTypeFieldInteger() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Integer>(123);
		checkInvalidTypeField(dto);
	}
	
	@Test
	void testParseDateInvalidTypeFieldLocalDate() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<LocalDate>(LocalDate.now());
		checkInvalidTypeField(dto);
	}
	
	@Test
	void testParseDateInvalidTypeFieldBigDecimal() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<BigDecimal>(BigDecimal.ONE);
		checkInvalidTypeField(dto);
	}
	
	@Test
	void testParseDateInvalidTypeFieldList() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<List<String>>(List.of());
		checkInvalidTypeField(dto);
	}
	
	@Test
	void testParseDateInvalidTypeFieldFloat() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Float>(1.2f);
		checkInvalidTypeField(dto);
	}
	
	@Test
	void testParseDateInvalidTypeFieldObject() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var dto = new MyTestDTO10<Object>(new MyTestDTO1("test"));
		checkInvalidTypeField(dto);
	}

	private <T> void checkInvalidTypeField(T dto) {
		var ex = assertThrows(ValidationException.class, () -> TestUtils.validateParseDto(dto));
		assertEquals("HV000028: Unexpected exception during isValid call.", ex.getMessage());
		assertEquals(IllegalArgumentException.class, ex.getCause().getClass());
		assertEquals("'@ValidParseDate' can use only in String field.", ex.getCause().getMessage());
	}
	
}
