package bank.service.serviceImpl;

import bank.dto.transaction.TransactionResponseDto;
import bank.exception.EntityNotFoundException;
import bank.mapper.TransactionMapper;
import bank.model.Transaction;
import bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {


    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private TransactionMapper transactionMapperMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    void testGetBankAccountTransactions() {

        int expectedSize = 1;

        long bankAccountId = 1L,
                transactionId = 1L;
        LocalDateTime transactionDate = LocalDateTime.now();
        double moneyAmount = 1000D;
        String transactionMsg = "Transaction Msg";
        String transactionType = "Transaction Type";
        Transaction transaction = Transaction.builder()
                .id(transactionId)
                .type(transactionType)
                .msg(transactionMsg)
                .moneyAmount(moneyAmount)
                .transactionDate(transactionDate)
                .build();
        TransactionResponseDto transactionResponseDto = TransactionResponseDto.builder()
                .id(transactionId)
                .msg(transactionMsg)
                .moneyAmount(moneyAmount)
                .type(transactionType)
                .transactionDate(transactionDate.toString())
                .build();
        List<Transaction> transactions = List.of(transaction);
        List<TransactionResponseDto> transactionResponseDtos = List.of(transactionResponseDto);

        when(userRepositoryMock.findTransactionsByBankAccountId(bankAccountId)).thenReturn(transactions);
        when(transactionMapperMock.mapEntityListToResponseDtoList(transactions)).thenReturn(transactionResponseDtos);

        List<TransactionResponseDto> bankAccountTransactions = transactionService.getBankAccountTransactions(bankAccountId);

        verify(userRepositoryMock, times(1)).findTransactionsByBankAccountId(bankAccountId);
        verify(transactionMapperMock, times(1)).mapEntityListToResponseDtoList(transactions);

        assertEquals(expectedSize, bankAccountTransactions.size());
        assertEquals(transactionResponseDtos, bankAccountTransactions);
        assertEquals(transactionId, bankAccountTransactions.get(0).getId());

    }

    @Test
    void testClearBankAccountTransactions() {

        long bankAccountId = 1L;
        when(userRepositoryMock.deleteTransactionsByBankAccountId(bankAccountId)).thenReturn(true);
        assertDoesNotThrow(() -> transactionService.clearBankAccountTransactions(bankAccountId));
        verify(userRepositoryMock, times(1)).deleteTransactionsByBankAccountId(bankAccountId);
    }

    @Test
    void testClearBankAccountTransactions_noTransactions() {

        long bankAccountId = 1L;
        when(userRepositoryMock.deleteTransactionsByBankAccountId(bankAccountId)).thenReturn(false);
        assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.clearBankAccountTransactions(bankAccountId)
        );
        verify(userRepositoryMock, times(1)).deleteTransactionsByBankAccountId(bankAccountId);
    }

}
