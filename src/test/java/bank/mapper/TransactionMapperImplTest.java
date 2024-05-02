package bank.mapper;

import bank.dto.transaction.TransactionRequestDto;
import bank.dto.transaction.TransactionResponseDto;
import bank.mapper.impl.TransactionMapperImpl;
import bank.model.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionMapperImplTest {

    private final TransactionMapper transactionMapper = new TransactionMapperImpl(new ModelMapper());

    private static Transaction transaction;

    @BeforeAll
    public static void init() {
        transaction = Transaction.builder()
                .id(1L)
                .msg("Transaction Message")
                .type("Transaction Type")
                .moneyAmount(200D)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void testMapRequestDtoToTransaction() {
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .type(transaction.getType())
                .msg(transaction.getMsg())
                .moneyAmount(transaction.getMoneyAmount())
                .build();
        Transaction mapped = transactionMapper.mapToEntity(transactionRequestDto);

        assertEquals(mapped.getMsg(), transactionRequestDto.getMsg());
        assertEquals(mapped.getType(), transactionRequestDto.getType());
        assertEquals(mapped.getMoneyAmount(), transactionRequestDto.getMoneyAmount());
    }

    @Test
    public void testMapTransactionToResponseDto() {
        TransactionResponseDto transactionResponseDto = transactionMapper.mapToResponseDto(transaction);

        assertEquals(transactionResponseDto.getId(), transaction.getId());
        assertEquals(transactionResponseDto.getMsg(), transaction.getMsg());
        assertEquals(transactionResponseDto.getType(), transaction.getType());
        assertEquals(transactionResponseDto.getMoneyAmount(), transaction.getMoneyAmount());
        assertEquals(transactionResponseDto.getTransactionDate(), transaction.getTransactionDate().toString());
    }

}
