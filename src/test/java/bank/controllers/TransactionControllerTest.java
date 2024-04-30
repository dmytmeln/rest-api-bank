//package bank.controllers;
//
//import bank.model.BankAccount;
//import bank.model.Role;
//import bank.model.Transaction;
//import bank.model.User;
//import bank.service.BankService;
//import bank.service.TransactionService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(TransactionController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class TransactionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BankService bankService;
//
//    @MockBean
//    private TransactionService transactionServiceMock;
//
//    private static final Long ID = 1L;
//    private static BankAccount bankAccount;
//    private static Transaction transaction;
//    private static UserDetails userDetails;
//
//    private static final String TRANSACTION_PAGE = "transactions";
//
//    @BeforeAll
//    static void init() {
//        transaction = Transaction.builder()
//                .moneyAmount(1000D)
//                .msg("Message")
//                .type("Type")
//                .id(ID)
//                .build();
//
//        bankAccount = BankAccount.builder()
//                .balance(0D)
//                .id(ID)
//                .build();
//
//        userDetails = User.builder()
//                .id(ID)
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
//        when(bankService.findBankAccountByUserId(ID)).thenReturn(bankAccount);
//
//        when(transactionServiceMock.getBankAccountTransactions(ID)).thenReturn(List.of(transaction));
//
//        // Mocking security context with the specified user
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
//    }
//
//    @Test
//    void testGetTransactionPage() throws Exception {
//        mockMvc.perform(get("/transactions").sessionAttr("userId", 1L).with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("transactions"))
//                .andExpect(model().attribute("transactions", List.of(transaction)))
//                .andExpect(view().name(TRANSACTION_PAGE));
//
//        verify(bankService, times(1)).findBankAccountByUserId(ID);
//        verify(transactionServiceMock, times(1)).getBankAccountTransactions(ID);
//    }
//
//    @Test
//    void testGetTransactionPage_WithNoTransactions() throws Exception {
//        when(transactionServiceMock.getBankAccountTransactions(ID)).thenReturn(List.of());
//
//        mockMvc.perform(get("/transactions").sessionAttr("userId", 1L).with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("transactions"))
//                .andExpect(model().attribute("transactions", List.of()))
//                .andExpect(view().name(TRANSACTION_PAGE));
//
//        verify(bankService, times(1)).findBankAccountByUserId(ID);
//        verify(transactionServiceMock, times(1)).getBankAccountTransactions(ID);
//    }
//
//    @Test
//    void testInvalidGetTransactionPage_WithNonexistentAccount() throws Exception {
//        when(bankService.findBankAccountByUserId(ID)).thenThrow(new EntityNotFoundException(
//                "BankAccount with  id [%d] not found".formatted(ID)
//        ));
//
//        mockMvc.perform(get("/transactions").sessionAttr("userId", ID))
//                .andExpect(status().isNotFound())
//                .andExpect(view().name("error"));
//
//        verify(bankService, times(1)).findBankAccountByUserId(ID);
//        verify(transactionServiceMock, times(0)).getBankAccountTransactions(ID);
//    }
//
//}
