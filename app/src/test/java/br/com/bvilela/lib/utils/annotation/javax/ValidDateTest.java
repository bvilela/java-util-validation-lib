package br.com.bvilela.lib.utils.annotation.javax;

import br.com.bvilela.lib.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ValidDateTest {

    @AllArgsConstructor
    private static class MyTestDTO1 {
        @ValidParseDate private String date;
    }

    @DisplayName("DTO Valid - Required Date OK")
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

    @SneakyThrows
    @DisplayName("DTO Invalid - Date Format Invalid Exception")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @ValueSource(strings = {"abc", "31-02-2022", "01-01-2022", "02-01-2022"})
    void shouldExceptionDateInvalid(String date) {
        var dto = new MyTestDTO1(date);
        var errors = ValidationUtils.validateParseDto(dto);
        assertEquals(1, errors.size());
        assertEquals("Value is a invalid date.", errors.get(0).getMessage());
        assertEquals("date", errors.get(0).getPropertyPath().toString());
    }

    @Test
    @DisplayName("DTO Invalid - Required Date Exception With Custom Message")
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

    @DisplayName("DTO Valid - Not Required Date OK")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidDtoNotRequiredDate(String date) {
        var dto = new MyTestDTO1NoRequired(date);
        assertTrue(ValidationUtils.validateDto(dto).isEmpty());
    }

    @DisplayName("DTO Invalid - Locale Parameter Invalid")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @MethodSource("shouldExceptionLocaleInvalidParameters")
    void shouldExceptionLocaleInvalid(Object dto) {
        var exception =
                assertThrows(
                        ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
        assertEquals(
                "HV000032: Unable to initialize br.com.bvilela.lib.utils.annotation.javax.impl.ValidParseDateImpl.",
                exception.getMessage());
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
        assertEquals("Param 'locale' must not be Blank.", exception.getCause().getMessage());
    }

    static Stream<Arguments> shouldExceptionLocaleInvalidParameters() {
        @AllArgsConstructor
        class MyTestDTO1 {
            @ValidParseDate(locale = "")
            private String date;
        }
        @AllArgsConstructor
        class MyTestDTO2 {
            @ValidParseDate(locale = " ")
            private String date;
        }

        return Stream.of(
                Arguments.of(new MyTestDTO1("01/01/2022")),
                Arguments.of(new MyTestDTO2("01/01/2022")));
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

    @DisplayName("DTO Valid - Parse Date Success (Format MMM EN)")
    @ParameterizedTest(name = "Date is \"{0}\"")
    @MethodSource("shouldParseDateSuccessMonthMMMEnglishParameters")
    void shouldParseDateSuccessMonthMMMEnglish(String date, LocalDate localDate) {
        var dto = new MyTestDTO8(date, null);
        checkValidateParseDtoNoViolations(dto);
        assertEquals(date, dto.getDate());
        assertEquals(localDate, dto.getDateConverted());
    }

    static Stream<Arguments> shouldParseDateSuccessMonthMMMEnglishParameters() {
        return Stream.of(
                Arguments.of("01 January 2022", LocalDate.of(2022, 1, 1)),
                Arguments.of("01 february 2022", LocalDate.of(2022, 2, 1)));
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

    @DisplayName("DTO Invalid - Parse Date Invalid Type Field")
    @ParameterizedTest(name = "Type Field is \"{0}\"")
    @MethodSource("shouldParseDateInvalidTypeFieldParameters")
    void shouldParseDateInvalidTypeField(Object dto) {
        var exception =
                assertThrows(
                        ValidationException.class, () -> ValidationUtils.validateParseDto(dto));
        assertEquals("HV000028: Unexpected exception during isValid call.", exception.getMessage());
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
        assertEquals(
                "'@ValidParseDate' can use only in String field.",
                exception.getCause().getMessage());
    }

    static Stream<Arguments> shouldParseDateInvalidTypeFieldParameters() {
        @AllArgsConstructor
        class MyTestDTO10<T> {
            @ValidParseDate private T date;

            @Override
            @SneakyThrows
            public String toString() {
                return this.date.getClass().getName();
            }
        }

        return Stream.of(
                Arguments.of(new MyTestDTO10<Integer>(123)),
                Arguments.of(new MyTestDTO10<LocalDate>(LocalDate.now())),
                Arguments.of(new MyTestDTO10<BigDecimal>(BigDecimal.ONE)),
                Arguments.of(new MyTestDTO10<List<String>>(new ArrayList<>())),
                Arguments.of(new MyTestDTO10<Float>(1.2f)),
                Arguments.of(new MyTestDTO10<Object>(new Object())));
    }

    @SneakyThrows
    private <T> void checkValidateParseDtoNoViolations(T dto) {
        assertTrue(ValidationUtils.validateParseDto(dto).isEmpty());
    }
}
