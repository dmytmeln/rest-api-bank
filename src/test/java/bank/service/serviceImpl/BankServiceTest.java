//package bank.service.serviceImpl;
//
//import bank.model.BankAccount;
//import bank.model.Transaction;
//import bank.dto.TransactionForm;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class BankServiceTest {
//
//    @Mock
//    private AccountRepository accountRepoMock;
//
//    @Mock
//    private ModelMapper modelMapperMock;
//
//    @InjectMocks
//    private BankServiceImpl bankService;
//
//    private static final Long ID = 1L;
//    private BankAccount bankAccount;
//
//    @BeforeEach
//    void init() {
//        bankAccount = BankAccount.builder()
//                .id(ID)
//                .transactions(new ArrayList<>())
//                .build();
//    }
//
//    @Test
//    void testFindById() {
//        when(accountRepoMock.findById(ID)).thenReturn(Optional.of(bankAccount));
//
//        BankAccount result = bankService.findById(ID);
//
//        assertNotNull(result);
//        assertEquals(ID, result.getId());
//    }
//
//    @Test
//    void testInvalidFindById() {
//        assertThrows(EntityNotFoundException.class, () -> bankService.findById(ID));
//    }
//
//    @Test
//    void testFindByUserIdTest() {
//        long userId = 1;
//        when(accountRepoMock.findBankAccountByUserId(userId)).thenReturn(Optional.of(bankAccount));
//
//        BankAccount bankAccount = bankService.findBankAccountByUserId(userId);
//
//        assertNotNull(bankAccount);
//        assertEquals(ID, bankAccount.getId());
//    }
//
//    @Test
//    void testInvalidFindByUserId() {
//        long userId = 1;
//        when(accountRepoMock.findBankAccountByUserId(userId)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> bankService.findBankAccountByUserId(userId));
//    }
//
//    @Test
//    void testMakeDepositTest() {
//        long id = 1;
//        double expectedBalance = 1000;
//        int expectedSize = 1;
//
//        String expectedInfo = "Transaction Test Deposit";
//        double moneyAmount = 1000.;
//        TransactionForm transaction = TransactionForm.builder()
//                .moneyAmount(moneyAmount)
//                .msg(expectedInfo)
//                .type(expectedInfo)
//                .build();
//
//        when(accountRepoMock.findById(ID)).thenReturn(Optional.of(bankAccount));
//        when(accountRepoMock.save(bankAccount)).thenReturn(bankAccount);
//        when(modelMapperMock.map(transaction, Transaction.class)).thenReturn(
//                Transaction.builder()
//                        .moneyAmount(moneyAmount)
//                        .type(expectedInfo)
//                        .msg(expectedInfo)
//                        .build()
//        );
//
//        BankAccount bankAccount = bankService.makeDeposit(id, transaction);
//        List<Transaction> transactions = bankAccount.getTransactions();
//        Transaction transactionDB = transactions.get(0);
//
//        assertNotNull(transactionDB.getBankAccount());
//        assertEquals(expectedSize, transactions.size());
//        assertEquals(expectedBalance, bankAccount.getBalance());
//
//        assertEquals(transaction.getType(), transactionDB.getType());
//        assertEquals(transaction.getMsg(), transactionDB.getMsg());
//        assertEquals(transaction.getMoneyAmount(), transactionDB.getMoneyAmount());
//    }
//
//    @Test
//    void testMakeWithdrawalTest() {
//        bankAccount.setBalance(1000D);
//        String expectedInfo = "Transaction Test Withdrawal";
//        double expectedBalance = 0;
//        int expectedSize = 1;
//
//        double moneyAmount = 1000.;
//        TransactionForm transaction = TransactionForm.builder()
//                .moneyAmount(moneyAmount)
//                .msg(expectedInfo)
//                .type(expectedInfo)
//                .build();
//
//        when(accountRepoMock.findById(ID)).thenReturn(Optional.of(bankAccount));
//        when(accountRepoMock.save(bankAccount)).thenReturn(bankAccount);
//        when(modelMapperMock.map(transaction, Transaction.class)).thenReturn(
//                Transaction.builder()
//                        .moneyAmount(moneyAmount)
//                        .type(expectedInfo)
//                        .msg(expectedInfo)
//                        .build()
//        );
//
//        BankAccount bankAccount = bankService.makeWithdrawal(ID, transaction);
//        List<Transaction> transactions = bankAccount.getTransactions();
//        Transaction transactionDB = transactions.get(0);
//
//        assertNotNull(transactionDB.getBankAccount());
//        assertEquals(expectedSize, transactions.size());
//        assertEquals(expectedBalance, bankAccount.getBalance());
//
//        assertEquals(transaction.getType(), transactionDB.getType());
//        assertEquals(transaction.getMoneyAmount(), transactionDB.getMoneyAmount());
//        assertEquals(transaction.getMsg(), transactionDB.getMsg());
//    }
//
//    @Test
//    void testInvalidMakeWithdrawalTest() {
//        String expectedInfo = "Transaction Test Withdrawal";
//        double moneyAmount = 1000.;
//        TransactionForm transaction = TransactionForm.builder()
//                .moneyAmount(moneyAmount)
//                .msg(expectedInfo)
//                .type(expectedInfo)
//                .build();
//
//        when(accountRepoMock.findById(ID)).thenReturn(Optional.of(bankAccount));
//
//        assertThrows(IllegalArgumentException.class, () -> bankService.makeWithdrawal(ID, transaction));
//    }
//
//
//
//}