package bank.repository;

import bank.model.BankAccount;
import bank.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
public class BankRepositoryTest {

    private final BankRepository bankRepository;

    private final Long bankAccountId = 1L;

    @Autowired
    public BankRepositoryTest(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Test
    public void testFindAllBankAccounts() {
        List<BankAccount> accounts = bankRepository.findAll();
        int expectedSize = 1;

        assertEquals(expectedSize, accounts.size());
    }

    @Test
    public void testFindBankAccountById() {
        BankAccount bankAccount = bankRepository.findById(bankAccountId).orElse(null);

        assertNotNull(bankAccount);
        assertEquals(bankAccountId, bankAccount.getId());
    }

    @Test
    public void testCreateBankAccount() {
        long expectedId = 2;
        double expectedBalance = 1000D;
        BankAccount bankAccount = BankAccount.builder()
                .balance(expectedBalance)
                .build();
        BankAccount saved = bankRepository.save(bankAccount);

        assertEquals(expectedId, saved.getId());
        assertEquals(expectedBalance, saved.getBalance());
    }

    @Test
    public void testUpdateBankAccount() {
        double expectedBalance = 2000D;
        BankAccount bankAccount = BankAccount.builder()
                .id(bankAccountId)
                .balance(expectedBalance)
                .build();
        bankRepository.updateWithoutTransactions(bankAccount.getBalance(), bankAccount.getId());
        BankAccount updated = bankRepository.findById(bankAccountId).orElse(null);


        assertNotNull(updated);
        assertNotNull(updated.getTransactions());
        assertEquals(bankAccountId, updated.getId());
        assertEquals(expectedBalance, updated.getBalance());
    }

    @Test
    public void testDeleteBankAccount() {
        bankRepository.deleteById(bankAccountId);
        BankAccount bankAccount = bankRepository.findById(bankAccountId).orElse(null);
        List<Transaction> transactionsByBankAccountId = bankRepository.findTransactionsByBankAccountId(bankAccountId);

        assertNull(bankAccount);
        assertTrue(transactionsByBankAccountId.isEmpty());
    }

    @Test
    public void testFindTransactionsByBankAccountId() {
        int expectedSize = 2;
        List<Transaction> transactionsByBankAccountId = bankRepository.findTransactionsByBankAccountId(bankAccountId);

        assertEquals(expectedSize, transactionsByBankAccountId.size());
    }

    @Test
    public void testDeleteTransactionsByBankAccountId() {
        bankRepository.deleteTransactionsByBankAccountId(bankAccountId);
        List<Transaction> transactionsByBankAccountId = bankRepository.findTransactionsByBankAccountId(bankAccountId);
        BankAccount bankAccount = bankRepository.findById(bankAccountId).orElse(null);

        assertTrue(transactionsByBankAccountId.isEmpty());
        assertNotNull(bankAccount);
    }

}
