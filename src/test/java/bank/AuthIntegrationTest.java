//package bank;
//
//import bank.model.BankAccount;
//import bank.model.User;
//import bank.service.BankService;
//import bank.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class AuthIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private BankService bankService;
//
//    @Test
//    @Transactional
//    void signupTest() throws Exception {
//        Long expectedId = 2L;
//        User expectedUser = User.builder()
//                .id(expectedId)
//                .email("dimon28@gmail.com")
//                .password("12!@asAS")
//                .phoneNumber("380984035792")
//                .firstname("Dmytro")
//                .lastname("Melnyk")
//                .build();
//        mockMvc.perform(post("/auth/signup")
//                        .param("email", expectedUser.getEmail())
//                        .param("firstname", expectedUser.getFirstname())
//                        .param("lastname", expectedUser.getLastname())
//                        .param("password", expectedUser.getPassword())
//                        .param("phoneNumber", expectedUser.getPhoneNumber()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/auth/login"));
//
//        User actualUser = userService.findById(expectedId);
//        BankAccount actualBankAccount = bankService.findBankAccountByUserId(expectedId);
//
//        BankAccount expectedBankAccount = actualUser.getBankAccount();
//        assertEquals(expectedBankAccount, actualBankAccount);
//        assertEquals(expectedUser, actualUser);
//    }
//
//}
