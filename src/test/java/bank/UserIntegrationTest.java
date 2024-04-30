//package bank;
//
//import bank.model.User;
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
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class UserIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    @Transactional
//    void updateTest() throws Exception {
//        String expectedFirstname = "Jason";
//        long id = 1L;
//        User user = userService.findById(id);
//
//        mockMvc.perform(post("/user/update/{userId}", id)
//                        .param("firstname", expectedFirstname)
//                        .param("lastname", user.getLastname())
//                        .param("email", user.getEmail())
//                        .param("password", user.getPassword())
//                        .param("phoneNumber", user.getPhoneNumber()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/user"))
//                .andExpect(view().name("redirect:/user"));
//
//        user = userService.findById(id);
//
//        assertEquals(expectedFirstname, user.getFirstname());
//    }
//
//}
