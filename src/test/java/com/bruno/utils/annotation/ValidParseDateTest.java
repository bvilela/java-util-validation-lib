package com.bruno.utils.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;

import com.bruno.utils.TestUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

class ValidParseDateTest {

	@AllArgsConstructor
	private class MyTestDTO {
		@ValidParseDate
		private String date1;
		
		@ValidParseDate(pattern = "yyyy-MM-dd")
		private String date2;
	}

	private void success(MyTestDTO dto) {
		assertNull(TestUtils.validateDto(dto));
	}

	@Test
	void shouldSuccessCase1() {
		success(new MyTestDTO("01-01-2022", "2022-01-01"));
	}

	@Test
	void shouldSuccessCase2() {
		success(new MyTestDTO("13-06-2022", "2022-06-13"));
	}
	
	@Test
	void shouldSuccessCase3() {
		success(new MyTestDTO("13-06-2022", "2022-06-01"));
	}
	
	private void exception(MyTestDTO dto) {
		List<ConstraintViolation<Object>> errors = TestUtils.validateDto(dto);
		assertEquals(1, errors.size());
		assertEquals("Value not is a valid date", errors.get(0).getMessage());
	}

	@Test
	void shouldExceptionCase1() {
		exception(new MyTestDTO("abc", "2022-06-01"));
	}
	
	@Test
	void shouldExceptionCase2() {
		exception(new MyTestDTO("13-06-2022", "abc"));
	}
	
	@Test
	void shouldExceptionCase3() {
		exception(new MyTestDTO(null, "2022-06-01"));
	}
	
	@Test
	void shouldExceptionCase4() {
		exception(new MyTestDTO("13-06-2022", ""));
	}
	
	@Test
	void shouldExceptionCase5() {
		exception(new MyTestDTO("31-02-2022", "abc"));
	}
	
	@Test
	void shouldExceptionCase6() {
		exception(new MyTestDTO("13-02-2022", "30-02-2022"));
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	public class MyTestDTO1 {
		@ValidParseDate
		private String date;
		private LocalDate dateConverted;
	}
	
	@Getter
	@AllArgsConstructor
	public class MyTestDTO2 {
		@ValidParseDate
		private String date;
	}
	
	@Getter
	@AllArgsConstructor
	public class MyTestDTO3 {
		private String date;
	}
	
	@Setter
	@Getter
	@NoArgsConstructor
	private class MyTestDTO4 {
		@ValidParseDate
		private String date;
		private LocalDate dateConverted;
	}
	
	@Getter
	@AllArgsConstructor
	public class MyTestDTO5 {
		@ValidParseDate(parse = false)
		private String date;
	}
	
	@Setter
	@Getter
	@NoArgsConstructor
	public class MyTestDTO6 {
		@ValidParseDate(pattern = "yyyy/MM/dd")
		private String date;
		private LocalDate dateConverted;
	}
	
/*	
	@Test
	void shouldParseDate() throws ListBuilderException {
		var dto = new MyTestDTO1();
		dto.setDate("10-06-2022");
		assertDoesNotThrow(() -> AppUtils.dtoParseDates(dto));
		assertEquals(LocalDate.of(2022, 6, 10), dto.getDateConverted());
	}
	
	@Test
	void shouldParseDateEspecificPattern() throws ListBuilderException {
		var dto = new MyTestDTO6();
		dto.setDate("2022/01/15");
		assertDoesNotThrow(() -> AppUtils.dtoParseDates(dto));
		assertEquals(LocalDate.of(2022, 1, 15), dto.getDateConverted());
	}
	
	@Test
	void shouldParseDateExceptionMethodNotFound() throws ListBuilderException {
		var dto = new MyTestDTO2("10-06-2022");
		var ex = assertThrows(ListBuilderException.class, () -> AppUtils.dtoParseDates(dto));
		assertEquals("Método setDateConverted(LocalDate) não encontrado!", ex.getMessage());
	}
	
	@Test
	void shouldParseDateDtoNull() throws ListBuilderException {
		assertDoesNotThrow(() -> AppUtils.dtoParseDates(null));
	}
	
	@Test
	void shouldParseDateNoAnnotation() throws ListBuilderException {
		var date = "10-06-2022";
		var dto = new MyTestDTO3(date);
		assertDoesNotThrow(() -> AppUtils.dtoParseDates(dto));
		assertEquals(date, dto.getDate());
	}
	
	@Test
	void shouldParseDateParamParseFalse() throws ListBuilderException {
		var myDate = "01-06-2022";
		var dto = new MyTestDTO5(myDate);
		assertDoesNotThrow(() -> AppUtils.dtoParseDates(dto));
		assertEquals(myDate, dto.getDate());
	}
	
	@Test
	void shouldParseDateException() throws ListBuilderException {
		var date = "10-06-2022";
		var dto = new MyTestDTO4();
		dto.setDate(date);
		dto.setDateConverted(null);
		var ex = assertThrows(ListBuilderException.class, () -> AnnotationUtils.dtoParseDates(dto));
		assertEquals("Erro ao Setar Campo(s) LocalDate Convertido(s)!", ex.getMessage());
		assertNull(dto.getDateConverted());
		assertEquals(date, dto.getDate());
	}
*/
}
