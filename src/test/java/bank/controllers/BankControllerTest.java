//package bank.controllers;
//
//import bank.config.WebMvcTestConfiguration;
//import bank.dto.TransactionForm;
//import bank.model.BankAccount;
//import bank.model.Role;
//import bank.model.User;
//import bank.service.BankService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BankController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class BankControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BankService bankServiceMock;
//
//    @Captor
//    ArgumentCaptor<TransactionForm> transactionCaptor;
//
//    private static Long accountId;
//    private static BankAccount bankAccount;
//
//    private static UserDetails userDetails;
//
//    private static final String BANK_PAGE = "bank";
//
//    @BeforeAll
//    static void init() {
//        accountId = 1L;
//
//        bankAccount = BankAccount.builder()
//                .balance(0D)
//                .id(accountId)
//                .build();
//
//        userDetails = User.builder()
//                .id(accountId)
//                .email("dimamel28@gmail.com")
//                .password("12!@asAS")
//                .phoneNumber("380984035791")
//                .role(Role.ROLE_USER)
//                .firstname("Dmytro")
//                .lastname("Melnyk")
//                .build();
//    }
//
//    @BeforeEach
//    void setup() {
//        when(bankServiceMock.findBankAccountByUserId(accountId)).thenReturn(bankAccount);
//        when(bankServiceMock.findById(accountId)).thenReturn(bankAccount);
//
//        // Mocking security context with the specified user
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
//    }
//
//    @Test
//    void testShowBank() throws Exception {
//
//        mockMvc.perform(get("/bank").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attribute("account", bankAccount))
//                .andExpect(model().attributeExists("transactionFormD"))
//                .andExpect(model().attribute("transactionFormD", new TransactionForm()))
//                .andExpect(model().attributeExists("transactionFormW"))
//                .andExpect(model().attribute("transactionFormW", new TransactionForm()))
//                .andExpect(view().name(BANK_PAGE));
//
//        verify(bankServiceMock, times(1)).findBankAccountByUserId(accountId);
//    }
//
//    @Test
//    void testInvalidShowBank() throws Exception {
//        when(bankServiceMock.findBankAccountByUserId(accountId)).thenThrow(new EntityNotFoundException(
//                "BankAccount with  id [%d] not found".formatted(accountId)
//        ));
//
//        mockMvc.perform(get("/bank").with(csrf()))
//                .andExpect(status().isNotFound())
//                .andExpect(view().name("error"));
//
//        verify(bankServiceMock, times(1)).findBankAccountByUserId(accountId);
//    }
//
//    @Test
//    void testMakeDeposit() throws Exception {
//        String message = "Message";
//        String type = "Type";
//        Double moneyAmount = 1000D;
//        double expectedBalance = bankAccount.getBalance() + moneyAmount;
//
//        when(bankServiceMock.makeDeposit(eq(accountId), any(TransactionForm.class))).thenAnswer(invocationOnMock -> {
//            Long bankAccountId = invocationOnMock.getArgument(0);
//            TransactionForm transaction = invocationOnMock.getArgument(1);
//            BankAccount account = bankServiceMock.findById(bankAccountId);
//            account.setBalance(account.getBalance() + transaction.getMoneyAmount());
//
//            return account;
//        });
//
//        mockMvc.perform(post("/bank/deposit/{accountId}", accountId).with(csrf())
//                        .param("moneyAmount", moneyAmount.toString())
//                        .param("msg", message)
//                        .param("type", type))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bank"))
//                .andExpect(view().name("redirect:/bank"));
//
//        verify(bankServiceMock, times(1)).findById(accountId);
//        verify(bankServiceMock, times(1)).makeDeposit(eq(accountId), transactionCaptor.capture());
//
//        TransactionForm captorValue = transactionCaptor.getValue();
//
//        assertEquals(message, captorValue.getMsg());
//        assertEquals(type, captorValue.getType());
//        assertEquals(expectedBalance, bankAccount.getBalance());
//    }
//
//    @Test
//    void testInvalidMakeDeposit() throws Exception {
//        mockMvc.perform(post("/bank/deposit/{accountId}", accountId).with(csrf())
//                        .param("moneyAmount", "-1000"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attribute("account", bankAccount))
//                .andExpect(view().name(BANK_PAGE));
//    }
//
//    @Test
//    void testMakeWithdrawal() throws Exception {
//
//        String message = "Message";
//        String type = "Type";
//        double expectedBalance = bankAccount.getBalance() - 1000D;
//
//        when(bankServiceMock.makeWithdrawal(eq(accountId), any(TransactionForm.class))).thenAnswer(invocationOnMock -> {
//            Long bankAccountId = invocationOnMock.getArgument(0);
//            TransactionForm transaction = invocationOnMock.getArgument(1);
//            BankAccount account = bankServiceMock.findById(bankAccountId);
//            account.setBalance(account.getBalance() - transaction.getMoneyAmount());
//
//            return account;
//        });
//
//        mockMvc.perform(post("/bank/withdrawal/{accountId}", accountId).with(csrf())
//                        .param("moneyAmount", "1000")
//                        .param("msg", message)
//                        .param("type", type))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bank"))
//                .andExpect(view().name("redirect:/bank"));
//
//        verify(bankServiceMock, times(1)).findById(accountId);
//        verify(bankServiceMock, times(1)).makeWithdrawal(eq(accountId), transactionCaptor.capture());
//
//        TransactionForm captorValue = transactionCaptor.getValue();
//
//        assertEquals(message, captorValue.getMsg());
//        assertEquals(type, captorValue.getType());
//        assertEquals(expectedBalance, bankAccount.getBalance());
//
//    }
//
//    @Test
//    void testInvalidMakeWithdrawal() throws Exception {
//        mockMvc.perform(post("/bank/withdrawal/{accountId}", accountId).with(csrf())
//                        .param("moneyAmount", "-1000"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attribute("account", bankAccount))
//                .andExpect(view().name(BANK_PAGE));
//    }
//
//}
