package bank.dto;

import bank.dto.user.UserRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRequestDtoTest {

    private UserRequestDto validUser;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void init() {
        validUser = UserRequestDto.builder()
                .email("dimamel28@gmail.com")
                .firstname("Dmytro")
                .lastname("Melnyk")
                .password("qwQW12!@")
                .phoneNumber("380984035791")
                .build();
    }

    @Test
    void testValidUser() {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailUser")
    void userWithInvalidEmail(String input, String errorValue) {
        validUser.setEmail(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidEmailUser() {
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("email@", "email@"),
                Arguments.of("email@.", "email@."),
                Arguments.of("email@.com", "email@.com"),
                Arguments.of("email@gmail", "email@gmail"),
                Arguments.of("email@gmail.", "email@gmail."),
                Arguments.of("email@gmail-com", "email@gmail-com"),
                Arguments.of("", ""),
                Arguments.of("invalid", "invalid"),
                Arguments.of("username.@domain.com", "username.@domain.com"),
                Arguments.of(".user.name@domain.com", ".user.name@domain.com"),
                Arguments.of("user-name@domain.com.", "user-name@domain.com.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidEmailUser")
    void userWithValidEmail(String input) {
        validUser.setEmail(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    private static Stream<Arguments> provideValidEmailUser(){
        return Stream.of(
                Arguments.of("username@domain.com", "username@domain.com"),
                Arguments.of("user.name@domain.com", "user.name@domain.com"),
                Arguments.of("user-name@domain.com", "user-name@domain.com"),
                Arguments.of("username@domain.co.in", "username@domain.co.in"),
                Arguments.of("user_name@domain.com", "user_name@domain.com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidFirstNameUser")
    void UserWithValidFirstName(String input) {
        validUser.setLastname(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    private static Stream<Arguments> provideValidFirstNameUser(){
        return Stream.of(
                Arguments.of("Valid", "Valid"),
                Arguments.of("Valid-Valid", "Valid-Valid"),
                Arguments.of("Valid-Valid-Valid", "Valid-Valid-Valid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFirstNameUser")
    void userWithInvalidFirstName(String input, String errorValue) {
        validUser.setFirstname(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidFirstNameUser(){
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("Invalid-", "Invalid-"),
                Arguments.of("-Invalid", "-Invalid"),
                Arguments.of("Invalid-invalid", "Invalid-invalid"),
                Arguments.of("Invalid-iNvalid", "Invalid-iNvalid"),
                Arguments.of("invalid-Invalid", "invalid-Invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidLastNameUser")
    void userWithInvalidLastName(String input) {
        validUser.setLastname(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    private static Stream<Arguments> provideValidLastNameUser(){
        return Stream.of(
                Arguments.of("Valid", "Valid"),
                Arguments.of("Valid-Valid", "Valid-Valid"),
                Arguments.of("Valid-Valid-Valid", "Valid-Valid-Valid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLastNameUser")
    void userWithInvalidLastName(String input, String errorValue) {
        validUser.setLastname(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidLastNameUser(){
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("Invalid-", "Invalid-"),
                Arguments.of("-Invalid", "-Invalid"),
                Arguments.of("Invalid-invalid", "Invalid-invalid"),
                Arguments.of("Invalid-iNvalid", "Invalid-iNvalid"),
                Arguments.of("invalid-Invalid", "invalid-Invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidPasswords")
    void userWithValidPass(String password) {
        validUser.setPassword(password);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    private static Stream<Arguments> provideValidPasswords() {
        return Stream.of(
                Arguments.of("*Pa8ss2W0rd!"),
                Arguments.of("A2noT4herPass12!@"),
                Arguments.of("T%estPa^ssWo*rD@123")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPasswords")
    void userWithInvalidPass(String password, String errorValue) {
        validUser.setPassword(password);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidPasswords() {
        return Stream.of(
                Arguments.of("short1!", "short1!"),
                Arguments.of("2134", "2134"),
                Arguments.of("noSpecial123", "noSpecial123"),
                Arguments.of("!nouppercase@", "!nouppercase@"),
                Arguments.of("!NOLOWERCASE@", "!NOLOWERCASE@"),
                Arguments.of("0Nespecia1symbo$l", "0Nespecia1symbo$l"),
                Arguments.of("NoDigits@#", "NoDigits@#"),
                Arguments.of("12$%^DAA5d", "12$%^DAA5d"),
                Arguments.of("12$%^daa5D", "12$%^daa5D"),
                Arguments.of("0neDigit@#", "0neDigit@#")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPhoneNumbers")
    void userWithInvalidPhoneNumber(String input, String errorValue) {
        validUser.setPhoneNumber(input);

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidPhoneNumbers() {
        return Stream.of(
                Arguments.of("38098403579", "38098403579"),
                Arguments.of("3809840357911", "3809840357911"),
                Arguments.of("380984!035791", "380984!035791"),
                Arguments.of("380984d035791", "380984d035791"),
                Arguments.of("380984 035791", "380984 035791"),
                Arguments.of("+380984035791", "+380984035791"),
                Arguments.of("qwerty", "qwerty"),
                Arguments.of("qwerty380984035791", "qwerty380984035791")
        );
    }

    @Test
    void userWithValidPhoneNumber() {
        validUser.setPhoneNumber("380984305791");

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

}

