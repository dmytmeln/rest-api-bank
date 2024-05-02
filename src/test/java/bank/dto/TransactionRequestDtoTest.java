package bank.dto;

import bank.dto.transaction.TransactionRequestDto;
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

public class TransactionRequestDtoTest {

    private TransactionRequestDto validTransaction;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void init() {
        validTransaction =  TransactionRequestDto.builder()
                .msg("Transaction Message")
                .type("Transaction Type")
                .moneyAmount(1000D)
                .build();
    }

    @Test
    void testValidTransaction() {
        Set<ConstraintViolation<TransactionRequestDto>> violations = validator.validate(validTransaction);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidMsg() {
        validTransaction.setMsg(null);

        Set<ConstraintViolation<TransactionRequestDto>> violations = validator.validate(validTransaction);

        assertEquals(1, violations.size());
        assertEquals("Transaction msg can't be null", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidTransactionType() {
        validTransaction.setType(null);

        Set<ConstraintViolation<TransactionRequestDto>> violations = validator.validate(validTransaction);

        assertEquals(1, violations.size());
        assertEquals("Transaction type can't be null", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMoneyAmount")
    void testInvalidMoneyAmount(Double inputValue, Double errorValue) {
        validTransaction.setMoneyAmount(inputValue);

        Set<ConstraintViolation<TransactionRequestDto>> violations = validator.validate(validTransaction);

        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidMoneyAmount() {
        return Stream.of(
               Arguments.of(0D, 0D),
                Arguments.of(-1D, -1D),
                Arguments.of(-1000D, -1000D),
                Arguments.of(null, null)
        );
    }


}
