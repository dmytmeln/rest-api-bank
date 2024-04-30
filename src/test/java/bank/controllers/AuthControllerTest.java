//package bank.controllers;
//
//import bank.dto.UserForm;
//import bank.model.User;
//import bank.service.UserService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userServiceMock;
//    @Captor
//    private ArgumentCaptor<UserForm> userFormCaptor;
//
//    private final String SIGNUP_PAGE = "auth-signup";
//    private final String LOGIN_PAGE = "auth-login";
//
//    private static final Long ID = 1L;
//    private static User user;
//
//    @BeforeAll
//    static void init() {
//        user = User.builder()
//                .id(ID)
//                .email("dimamel28@gmail.com")
//                .password("12!@asAS")
//                .phoneNumber("380984035791")
//                .firstname("Dmytro")
//                .lastname("Melnyk")
//                .build();
//    }
//
//    @BeforeEach
//    void setup() {
//        when(userServiceMock.signup(any(UserForm.class))).thenReturn(user);
//    }
//
//    @Test
//    void testShowSignup() throws Exception {
//        mockMvc.perform(get("/register").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("userForm"))
//                .andExpect(view().name(SIGNUP_PAGE));
//    }
//
//    @Test
//    void testSignupUser() throws Exception {
//        String email = user.getEmail();
//        String password = user.getPassword();
//        mockMvc.perform(post("/register").with(csrf())
//                        .param("email", email)
//                        .param("firstname", user.getFirstname())
//                        .param("lastname", user.getLastname())
//                        .param("password", password)
//                        .param("phoneNumber", user.getPhoneNumber()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/login"));
//
//        verify(userServiceMock, times(1)).signup(userFormCaptor.capture());
//        UserForm captorValue = userFormCaptor.getValue();
//
//        assertEquals(email, captorValue.getEmail());
//        assertEquals(password, captorValue.getPassword());
//    }
//
//    @Test
//    void testInvalidAuthorizedSignupUser() throws Exception {
//        mockMvc.perform(post("/register").with(csrf())
//                        .param("email", user.getEmail())
//                        .param("firstname", "")
//                        .param("lastname", user.getLastname())
//                        .param("password", user.getPassword())
//                        .param("phoneNumber", user.getPhoneNumber()))
//                .andExpect(model().attributeHasFieldErrors("userForm", "firstname"))
//                .andExpect(status().isOk())
//                .andExpect(view().name(SIGNUP_PAGE));
//    }
//
//}