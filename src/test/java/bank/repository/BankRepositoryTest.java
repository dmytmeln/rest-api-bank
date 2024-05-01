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
        long expectedId = 1;
        BankAccount bankAccount = bankRepository.findById(expectedId).get();

        assertEquals(expectedId, bankAccount.getId());
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
        long expectedId = 1;
        double expectedBalance = 2000D;
        List<Transaction> transactions = bankRepository.findById(expectedId).get().getTransactions();
        BankAccount bankAccount = BankAccount.builder()
                .id(expectedId)
                .balance(expectedBalance)
                .transactions(transactions)
                .build();
        bankRepository.save(bankAccount);
        BankAccount updated = bankRepository.findById(expectedId).get();

        assertEquals(expectedId, updated.getId());
        assertEquals(expectedBalance, updated.getBalance());
        assertNotNull(updated.getTransactions());
        assertFalse(updated.getTransactions().isEmpty());
    }

    @Test
    public void testDeleteBankAccount() {
        long realId = 1;
        bankRepository.deleteById(realId);
        BankAccount bankAccount = bankRepository.findById(realId).orElse(null);

        assertNull(bankAccount);
    }

    @Test
    public void testFindTransactionsByBankAccountId() {
        int expectedSize = 2;
        long bankAccountId = 1L;
        List<Transaction> transactionsByBankAccountId = bankRepository.findTransactionsByBankAccountId(bankAccountId);

        assertEquals(expectedSize, transactionsByBankAccountId.size());
    }

}
