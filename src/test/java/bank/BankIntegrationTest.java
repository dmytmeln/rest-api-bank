package bank;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.exception.EntityNotFoundException;
import bank.model.BankAccount;
import bank.service.BankService;
import org.junit.jupiter.api.Order;
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
public class BankIntegrationTest {

    private final Long realUserId = 1L;
    private final Long  realBankAccountId = 1L;

    private final BankService bankService;

    @Autowired
    public BankIntegrationTest(BankService bankService) {
        this.bankService = bankService;
    }

    @Test
    public void testFindById() {
        Double expectedBalance = 1000D;

        BankAccount bankAccount = bankService.findById(realBankAccountId, realUserId);

        assertNotNull(bankAccount);
        assertEquals(realUserId, bankAccount.getId());
        assertEquals(expectedBalance, bankAccount.getBalance());
    }

    @Test
    public void testFindById_nonExistingBankAccount() {
        Long nonExistingBankAccountId = -1L,
                nonExistingUserId = -1L;

        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.findById(nonExistingBankAccountId, nonExistingUserId)
        );
    }

    @Test
    public void testFindBankResponseById() {
        Double expectedBalance = 1000D;

        BankResponseDto bankResponseDto = bankService.findBankResponseById(realBankAccountId, realUserId);

        assertNotNull(bankResponseDto);
        assertEquals(realUserId, bankResponseDto.getId());
        assertEquals(expectedBalance, bankResponseDto.getBalance());
    }

    @Test
    public void testFindBankResponseById_nonExistingBankAccount() {
        Long nonExistingBankAccountId = -1L,
                nonExistingUserId = -1L;

        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.findBankResponseById(nonExistingBankAccountId, nonExistingUserId)
        );
    }

    @Test
    public void testDelete_existingAccount() {

        boolean hasDeleted = bankService.deleteBankAccount(realBankAccountId, realUserId);

        assertTrue(hasDeleted);
        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.findById(realBankAccountId, realUserId)
        );

    }

    @Test
    public void testDelete_nonExistingAccount() {
        Long nonExistingBankAccountId = -1L,
                nonExistingUserId = -1L;

        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.deleteBankAccount(nonExistingBankAccountId, nonExistingUserId)
        );
    }

    @Test
    public void testCreateBankAccountForUser() {
        Double expectedBalance = 0D;
        Long expectedBankResponseId = 2L;

        BankResponseDto bankResponseDto = bankService.creteBankAccountForUser(realUserId);

        assertNotNull(bankResponseDto);
        assertEquals(expectedBankResponseId, bankResponseDto.getId());
        assertEquals(expectedBalance, bankResponseDto.getBalance());
    }

    @Test
    public void testUpdateUserBankAccount() {

        double balance = 10000D;
        BankAccount bankAccountToUpdate = BankAccount.builder()
                .id(realBankAccountId)
                .balance(balance)
                .build();

        BankAccount bankAccount = bankService.updateUserBankAccount(realUserId, bankAccountToUpdate);

        assertEquals(realBankAccountId, bankAccount.getId());
        assertEquals(balance, bankAccount.getBalance());

    }

    @Test
    public void testUpdateUserBankAccount_nonExistingBankAccount() {
        BankAccount bankAccountToUpdate = BankAccount.builder()
                .id(-1L)
                .balance(0D)
                .build();

        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.updateUserBankAccount(realUserId, bankAccountToUpdate)
        );
    }


    @Test
    @Order(1)
    public void testMakeDeposit() {

        String type = "Transaction Type";
        double moneyAmount = 1000D;
        String msg = "Transaction Msg";
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .type(type)
                .moneyAmount(moneyAmount)
                .msg(msg)
                .build();

        double bankAccountDbBalance = 1000D;
        double expectedBalance = moneyAmount + bankAccountDbBalance;
        int expectedSize = 3;
        long expectedTransactionId = 3L;

        BankResponseDto bankResponseDto = bankService.makeDeposit(realBankAccountId, realUserId, transactionRequestDto);

        List<Long> transactionsId = bankResponseDto.getTransactionsId();
        int size = transactionsId.size();
        assertEquals(expectedSize, size);
        assertEquals(expectedTransactionId, transactionsId.get(size - 1));
        assertEquals(expectedBalance, bankResponseDto.getBalance());

    }

    @Test
    @Order(2)
    public void testMakeWithdrawal() {

        String type = "Transaction Type";
        double moneyAmount = 1000D;
        String msg = "Transaction Msg";
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .type(type)
                .moneyAmount(moneyAmount)
                .msg(msg)
                .build();

        double bankAccountDbBalance = 1000D;
        double expectedBalance = bankAccountDbBalance - moneyAmount;
        int expectedSize = 3;
        long expectedTransactionId = 4L;

        BankResponseDto bankResponseDto = bankService.makeWithdrawal(realBankAccountId, realUserId, transactionRequestDto);

        List<Long> transactionsId = bankResponseDto.getTransactionsId();
        int size = transactionsId.size();
        assertEquals(expectedSize, size);
        assertEquals(expectedTransactionId, transactionsId.get(size - 1));
        assertEquals(expectedBalance, bankResponseDto.getBalance());

    }

}