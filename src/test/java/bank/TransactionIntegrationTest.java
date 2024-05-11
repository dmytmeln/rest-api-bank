package bank;

import bank.dto.transaction.TransactionResponseDto;
import bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TransactionIntegrationTest {

    private final Long  realBankAccountId = 1L;

    private final TransactionService transactionService;

    @Autowired
    public TransactionIntegrationTest(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Test
    public void testGetBankAccountTransactions() {

        int expectedSize = 2;

        List<TransactionResponseDto> bankAccountTransactions = transactionService.getBankAccountTransactions(realBankAccountId);

        assertFalse(bankAccountTransactions.isEmpty());
        assertEquals(expectedSize, bankAccountTransactions.size());

    }

    @Test
    public void testClearBankAccountTransactions() {
        boolean hasCleared = transactionService.clearBankAccountTransactions(realBankAccountId);
        List<TransactionResponseDto> bankAccountTransactions = transactionService.getBankAccountTransactions(realBankAccountId);

        assertTrue(hasCleared);
        assertTrue(bankAccountTransactions.isEmpty());
    }

}