package bank.repository;

import bank.model.BankAccount;
import bank.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
public class UserBankRepositoryTest {

    private final UserRepository userRepository;

    private final Long realBankAccountId = 1L;

    @Autowired
    public UserBankRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testFindTransactionsByBankAccountId() {
        int expectedSize = 2;
        List<Transaction> transactionsByBankAccountId = userRepository.findTransactionsByBankAccountId(realBankAccountId);

        assertEquals(expectedSize, transactionsByBankAccountId.size());
    }

    @Test
    public void testUpdateBankAccountWithoutTransactions() {
        Long realUserId = 1L;
        Double balance = 2000D;
        BankAccount bankAccount = BankAccount.builder()
                .id(realBankAccountId)
                .balance(balance)
                .build();

        boolean hasUpdated = userRepository.updateBankAccountWithoutTransactions(bankAccount);
        BankAccount bankAccountDb = userRepository.findBankAccountByBankAndUserIds(realBankAccountId, realUserId)
                .orElseThrow(RuntimeException::new);

        assertTrue(hasUpdated);
        assertEquals(balance, bankAccountDb.getBalance());
        assertEquals(realBankAccountId, bankAccountDb.getId());
        assertNotNull(bankAccountDb.getTransactions());
    }

    @Test
    public void testDeleteTransactionsByBankAccountId() {
        boolean hasDeleted = userRepository.deleteTransactionsByBankAccountId(realBankAccountId);

        List<Transaction> transactionsByBankAccountId = userRepository.findTransactionsByBankAccountId(realBankAccountId);

        assertTrue(hasDeleted);
        assertTrue(transactionsByBankAccountId.isEmpty());
    }

    @Test
    public void testDeleteBankAccountWithoutUser() {
        long userId = 1L;
        userRepository.deleteBankAccountWithoutUser(realBankAccountId);
        boolean bankAccountAbsent = userRepository.findBankAccountsByUserId(userId).stream()
                .noneMatch(bankAccount -> Objects.equals(bankAccount.getId(), realBankAccountId));

        assertTrue(bankAccountAbsent);
    }

}
