//package bank;
//
//import bank.model.Transaction;
//import bank.service.TransactionService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class BankIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TransactionService transactionService;
//
//    private final Long ACTUAL_ACCOUNT_ID = 1L;
//
//    private static Transaction expectedTransaction;
//
//    @BeforeAll
//    public static void init() {
//        expectedTransaction = Transaction.builder()
//                .id(3L)
//                .msg("Message")
//                .type("Type")
//                .moneyAmount(1000D)
//                .build();
//    }
//
//    @Test
//    @Transactional
//    public void makeDepositWithdrawalTest() throws Exception {
//        makeDeposit();
//        makeWithdrawal();
//    }
//
//    @Transactional
//    public void makeDeposit() throws Exception {
//
//        int expectedSize = 3;
//        mockMvc.perform(post("/bank/deposit/{accountId}", ACTUAL_ACCOUNT_ID)
//                        .param("moneyAmount",  expectedTransaction.getMoneyAmount().toString())
//                        .param("msg", expectedTransaction.getMsg())
//                        .param("type", expectedTransaction.getType()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bank"))
//                .andExpect(view().name("redirect:/bank"));
//
//        List<Transaction> bankAccountTransactions = transactionService.getBankAccountTransactions(ACTUAL_ACCOUNT_ID);
//
//        assertEquals(expectedSize, bankAccountTransactions.size());
//        Transaction actualTransaction = bankAccountTransactions.get(expectedSize - 1);
//        assertEquals(expectedTransaction, actualTransaction);
//
//    }
//
//    @Transactional
//    public void makeWithdrawal() throws Exception {
//
//        int expectedSize = 4;
//        mockMvc.perform(post("/bank/withdrawal/{accountId}", ACTUAL_ACCOUNT_ID)
//                        .param("moneyAmount",  expectedTransaction.getMoneyAmount().toString())
//                        .param("msg", expectedTransaction.getMsg())
//                        .param("type", expectedTransaction.getType()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bank"))
//                .andExpect(view().name("redirect:/bank"));
//
//        List<Transaction> bankAccountTransactions = transactionService.getBankAccountTransactions(ACTUAL_ACCOUNT_ID);
//
//        assertEquals(expectedSize, bankAccountTransactions.size());
//        Transaction actualTransaction = bankAccountTransactions.get(expectedSize - 1);
//        expectedTransaction.setId(4L);
//        assertEquals(expectedTransaction, actualTransaction);
//
//    }
//
//}
