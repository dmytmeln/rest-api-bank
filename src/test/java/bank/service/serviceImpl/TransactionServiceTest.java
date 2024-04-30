//package bank.service.serviceImpl;
//
//import bank.model.BankAccount;
//import bank.model.Transaction;
//import bank.service.BankService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class TransactionServiceTest {
//
//    @Mock
//    private TransactionRepository transactionRepoMock;
//    @Mock
//    private BankService bankServiceMock;
//
//    @InjectMocks
//    private TransactionServiceImpl transactionService;
//
//    private static List<Transaction> transactions;
//    private static BankAccount bankAccount;
//    private final static Long ID = 1L;
//
//    @BeforeAll
//    static void init() {
//        bankAccount = BankAccount.builder()
//                .id(ID)
//                .build();
//
//        Transaction transaction = Transaction.builder()
//                .id(ID)
//                .msg("Transaction Message")
//                .type("Transaction Type")
//                .moneyAmount(1000D)
//                .bankAccount(bankAccount)
//                .build();
//
//        transactions = List.of(transaction);
//    }
//
//    @BeforeEach
//    void setup() {
//        when(bankServiceMock.findById(ID)).thenReturn(bankAccount);
//        when(transactionRepoMock.findTransactionsByBankAccountId(ID)).thenReturn(transactions);
//    }
//
//    @Test
//    void testGetBankAccountTransactions() {
//        List<Transaction> bankAccountTransactions = transactionService.getBankAccountTransactions(ID);
//
//        assertNotNull(bankAccountTransactions);
//        assertEquals(bankAccountTransactions, transactions);
//    }
//
//    @Test
//    void testGetBankAccountTransactionsByUserId() {
//        when(bankServiceMock.findBankAccountByUserId(ID)).thenReturn(bankAccount);
//
//        List<Transaction> bankAccountTransactions = transactionService.getBankAccountTransactionsByUserId(ID);
//
//        assertNotNull(bankAccountTransactions);
//        assertEquals(bankAccountTransactions, transactions);
//    }
//
//
//}
