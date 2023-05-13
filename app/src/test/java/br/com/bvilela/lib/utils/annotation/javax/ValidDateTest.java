package br.com.bvilela.lib.utils.annotation.javax;

import br.com.bvilela.lib.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidDateTest {

    @AllArgsConstructor
    private static class MyTestDTO1 {
        @ValidParseDate
        private String date;
    }

    @DisplayName("DTO Valid - Required Date")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @ValueSource(strings = {"01/01/2022", "13/06/2022", "31/12/2022"})
    void shouldValidDto(String date) {
        var dto = new MyTestDTO1(date);
        assertTrue(ValidationUtils.validateDto(dto).isEmpty());
    }

    @DisplayName("DTO Invalid - Required Date Exception")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldInvalidDtoRequiredDate(String date) {
        var dto = new MyTestDTO1(date);
        var errors = ValidationUtils.validateDto(dto);
        assertEquals(1, errors.size());
        assertEquals("Field is required.", errors.get(0).getMessage());
        assertEquals("date", errors.get(0).getPropertyPath().toString());
    }

    @Test
    void shouldExceptionRequiredDateNullCustomRequiredMessage() {
        @AllArgsConstructor
        class CustomRequiredMessageDTO {
            @ValidParseDate(messageRequired = "My Custom Message for Required Field.")
            private String date;
        }

        List<ConstraintViolation<Object>> errors =
                ValidationUtils.validateDto(new CustomRequiredMessageDTO(null));
        assertEquals(1, errors.size());
        assertEquals("My Custom Message for Required Field.", errors.get(0).getMessage());
        assertEquals("date", errors.get(0).getPropertyPath().toString());
    }

    @AllArgsConstructor
    private static class MyTestDTO1NoRequired {
        @ValidParseDate(required = false)
        private String date;
    }

    @DisplayName("DTO Valid - Not Required Date")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidDtoNotRequiredDate(String date) {
        var dto = new MyTestDTO1NoRequired(date);
        assertTrue(ValidationUtils.validateDto(dto).isEmpty());
    }

    @DisplayName("DTO Invalid - Date Format Invalid Exception")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @ValueSource(strings = {"abc", "31-02-2022", "01-01-2022"})
    void shouldExceptionDateInvalid(String date) {
        var dto = new MyTestDTO1(date);
        checkMessageInvalidDate(ValidationUtils.validateDto(dto));
    }

    private void checkMessageInvalidDate(List<ConstraintViolation<Object>> errors) {
        assertEquals(1, errors.size());
        assertEquals("Value is a invalid date.", errors.get(0).getMessage());
        assertEquals("date", errors.get(0).getPropertyPath().toString());
    }

    @AllArgsConstructor
    private static class MyTestDTO2 {
        @ValidParseDate(locale = "")
        private String date;
    }

    @Test
    void shouldExceptionLocaleEmpty() {
        baseExceptionLocale(new MyTestDTO2("01/01/2022"));
    }

    @AllArgsConstructor
    private static class MyTestDTO3 {
        @ValidParseDate(locale = " ")
        private String date;
    }

    @Test
    void shouldExceptionLocaleBlank() {
        baseExceptionLocale(new MyTestDTO3("01/01/2022"));
    }

    private <T> void baseExceptionLocale(T dto) {
        var exception =
                assertThrows(
                        ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
        assertEquals(
                "HV000032: Unable to initialize br.com.bvilela.lib.utils.annotation.javax.impl.ValidParseDateImpl.",
                exception.getMessage());
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
        assertEquals("Param 'locale' must not be Blank.", exception.getCause().getMessage());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO4 {
        @ValidParseDate private String date;
        private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateFalse() {
        var myDate = "01/01/2022";
        var dto = new MyTestDTO4(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertNull(dto.getDateConverted());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO5 {
        @ValidParseDate(parse = true)
        private String date;

        @Setter private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateSuccess() {
        var myDate = "02/01/2022";
        var dto = new MyTestDTO5(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertEquals(LocalDate.of(2022, 1, 2), dto.getDateConverted());
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidDate() {
        var myDate = "02-01-2022";
        var dto = new MyTestDTO5(myDate, null);
        checkMessageInvalidDate(ValidationUtils.validateParseDto(dto));
        assertEquals(myDate, dto.getDate());
        assertNull(dto.getDateConverted());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO6 {
        @ValidParseDate(parse = true)
        private String date;

        private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateExceptionMethodNotFound() {
        var myDate = "03/01/2022";
        var dto = new MyTestDTO6(myDate, null);
        var exception =
                assertThrows(
                        NoSuchMethodException.class, () -> ValidationUtils.validateParseDto(dto));
        assertTrue(exception.getMessage().contains("setDateConverted(java.time.LocalDate"));
        assertEquals(myDate, dto.getDate());
        assertNull(dto.getDateConverted());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO7 {
        @ValidParseDate(parse = true, pattern = "dd MMMM yyyy")
        private String date;

        @Setter private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateSuccessMonthMMM() {
        var myDate = "01 janeiro 2022";
        var dto = new MyTestDTO7(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertEquals(LocalDate.of(2022, 1, 1), dto.getDateConverted());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO8 {
        @ValidParseDate(parse = true, pattern = "dd MMMM yyyy", locale = "en")
        private String date;

        @Setter private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateSuccessMonthMMMEnglish1() {
        var myDate = "01 January 2022";
        var dto = new MyTestDTO8(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertEquals(LocalDate.of(2022, 1, 1), dto.getDateConverted());
    }

    @Test
    @SneakyThrows
    void shouldParseDateSuccessMonthMMMEnglish2() {
        var myDate = "01 february 2022";
        var dto = new MyTestDTO8(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertEquals(LocalDate.of(2022, 2, 1), dto.getDateConverted());
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO9 {
        @ValidParseDate(parse = true, pattern = "yyyy dd MMMM", locale = "de_DE")
        private String date;

        @Setter private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldParseDateSuccessGermany() {
        var myDate = "2022 15 Oktober";
        var dto = new MyTestDTO9(myDate, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(myDate, dto.getDate());
        assertEquals(LocalDate.of(2022, 10, 15), dto.getDateConverted());
    }

    @AllArgsConstructor
    private static class MyTestDTO10<T> {
        @ValidParseDate private T date;
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldInteger() {
        var dto = new MyTestDTO10<Integer>(123);
        checkInvalidTypeField(dto);
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldLocalDate() {
        var dto = new MyTestDTO10<LocalDate>(LocalDate.now());
        checkInvalidTypeField(dto);
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldBigDecimal() {
        var dto = new MyTestDTO10<BigDecimal>(BigDecimal.ONE);
        checkInvalidTypeField(dto);
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldList() {
        var dto = new MyTestDTO10<List<String>>(List.of());
        checkInvalidTypeField(dto);
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldFloat() {
        var dto = new MyTestDTO10<Float>(1.2f);
        checkInvalidTypeField(dto);
    }

    @Test
    @SneakyThrows
    void shouldParseDateInvalidTypeFieldObject() {
        var dto = new MyTestDTO10<Object>(new MyTestDTO1("test"));
        checkInvalidTypeField(dto);
    }

    @Getter
    @AllArgsConstructor
    public static class MyTestDTO11 {
        @ValidParseDate(required = false, parse = true)
        private String date;

        private LocalDate dateConverted;
    }

    @Test
    @SneakyThrows
    void shouldNullValue() {
        var dto = new MyTestDTO11(null, null);
        checkValidateParseDtoNoViolations(dto);
        assertNull(dto.getDate());
        assertNull(dto.getDateConverted());
    }

    private <T> void checkInvalidTypeField(T dto) {
        var exception =
                assertThrows(
                        ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
        assertEquals("HV000028: Unexpected exception during isValid call.", exception.getMessage());
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
        assertEquals("'@ValidParseDate' can use only in String field.", exception.getCause().getMessage());
    }

    private <T> void checkValidateParseDtoNoViolations(T dto)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException {
        assertTrue(ValidationUtils.validateParseDto(dto).isEmpty());
    }

}
