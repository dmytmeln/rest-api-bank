//package bank.controllers;
//
//import bank.dto.user.UserForm;
//import bank.mapper.UserMapper;
//import bank.model.Role;
//import bank.model.User;
//import bank.service.UserService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userServiceMock;
//
//    @MockBean
//    private UserMapper userMapper;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
//    @Captor
//    private ArgumentCaptor<UserForm> userCaptor;
//
//    private final String USER_UPDATE_PAGE = "user-update";
//    private final String USER_PAGE = "user-info";
//
//    private final static Long USER_ID = 1L;
//    private static UserForm userForm;
//    private static User user;
//
//    @BeforeAll
//    static void init() {
//        String email = "dimamel28@gmail.com";
//        String password = "12!@asAS";
//        String phoneNumber = "380984035791";
//        String firstname = "Dmytro";
//        String lastname = "Melnyk";
//        userForm = UserForm.builder()
//                .email(email)
//                .password(password)
//                .phoneNumber(phoneNumber)
//                .firstname(firstname)
//                .lastname(lastname)
//                .build();
//
//        user = User.builder()
//                .id(USER_ID)
//                .email(email)
//                .password(password)
//                .phoneNumber(phoneNumber)
//                .role(Role.ROLE_USER)
//                .firstname(firstname)
//                .lastname(lastname)
//                .build();
//
//    }
//
//    @BeforeEach
//    void setup() {
//        when(userServiceMock.findById(USER_ID)).thenReturn(user);
//        when(userServiceMock.update(any(UserForm.class), eq(USER_ID))).thenReturn(user);
//        when(userMapper.mapToUserForm(user)).thenReturn(userForm);
//        when(passwordEncoder.matches(userForm.getPassword(), userForm.getPassword())).thenReturn(true);
//
//        // Mocking security context with the specified user
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
//    }
//
//    @Test
//    void testGetShowHome() throws Exception {
//        mockMvc.perform(get("/user").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("user"))
//                .andExpect(model().attribute("user", user))
//                .andExpect(view().name(USER_PAGE));
//
//        verify(userServiceMock, times(1)).findById(USER_ID);
//    }
//
//    @Test
//    void testInvalidGetShowHome() throws Exception {
//
//        when(userServiceMock.findById(USER_ID)).thenThrow(new EntityNotFoundException(
//                "User with  id: %d not found".formatted(USER_ID)
//        ));
//
//        mockMvc.perform(get("/user").with(csrf()))
//                .andExpect(status().isNotFound())
//                .andExpect(view().name("error"));
//
//        verify(userServiceMock, times(1)).findById(USER_ID);
//    }
//
//    @Test
//    void testShowUpdate() throws Exception {
//        mockMvc.perform(get("/user/update").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("userForm"))
//                .andExpect(model().attribute("userForm", userForm))
//                .andExpect(model().attributeExists("userId"))
//                .andExpect(model().attribute("userId", USER_ID))
//                .andExpect(view().name(USER_UPDATE_PAGE));
//
//        verify(userServiceMock, times(1)).findById(USER_ID);
//    }
//
//    @Test
//    void testValidUpdateUser() throws Exception {
//        String updatedFirstName = "Jason";
//        String email = userForm.getEmail();
//        mockMvc.perform(post("/user/update").with(csrf())
//                        .param("firstname", updatedFirstName)
//                        .param("lastname", userForm.getLastname())
//                        .param("email", email)
//                        .param("password", userForm.getPassword())
//                        .param("oldPassword", userForm.getPassword())
//                        .param("phoneNumber", userForm.getPhoneNumber()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/user"))
//                .andExpect(view().name("redirect:/user"));
//
//        verify(userServiceMock, times(1)).update(userCaptor.capture(), eq(USER_ID));
//        UserForm updatedUser = userCaptor.getValue();
//
//        assertEquals(updatedFirstName, updatedUser.getFirstname());
//        assertEquals(email, updatedUser.getEmail());
//    }
//
//    @Test
//    void testInvalidUpdateUser() throws Exception {
//        mockMvc.perform(post("/user/update").with(csrf())
//                        .param("firstname", "")
//                        .param("lastname", userForm.getLastname())
//                        .param("email", userForm.getEmail())
//                        .param("password", userForm.getPassword())
//                        .param("oldPassword", userForm.getPassword())
//                        .param("phoneNumber", userForm.getPhoneNumber()))
//                .andExpect(model().hasErrors())
//                .andExpect(model().attributeHasFieldErrors("userForm", "firstname"))
//                .andExpect(status().isOk())
//                .andExpect(view().name(USER_UPDATE_PAGE));
//    }
//
//
//}
