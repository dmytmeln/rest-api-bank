//package bank.service.serviceImpl;
//
//import bank.mapper.UserMapper;
//import bank.model.User;
//import bank.dto.UserForm;
//import jakarta.persistence.EntityExistsException;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepoMock;
//    @Mock
//    private UserMapper userMapperMock;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Captor
//    private ArgumentCaptor<User> userCaptor;
//
//    private User user;
//
//    private UserForm userForm;
//
//    @BeforeEach
//    void init() {
//        user = User.builder()
//                .firstname("John")
//                .lastname("Doe")
//                .password("12!@asAS")
//                .email("john.doe@example.com")
//                .phoneNumber("123123123123")
//                .build();
//
//        userForm = UserForm.builder()
//                .firstname("UpdatedFirstName")
//                .lastname("UpdatedLastName")
//                .phoneNumber("123123123123")
//                .password("12!@asAS")
//                .email("john.doe@example.com")
//                .build();
//    }
//
//    @Test
//    void testValidFindById() {
//        long id = 1L;
//        user.setId(id);
//        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));
//
//        User foundUser = userService.findById(id);
//
//        assertEquals(user, foundUser);
//    }
//
//    @Test
//    void testInvalidFindById() {
//        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
//    }
//
//    @Test
//    void testSignupTest() {
//
//        String email = user.getEmail();
//        String phoneNumber = user.getPhoneNumber();
//        long expectedId = 2;
//
//        when(userRepoMock.existsByEmailOrPhoneNumber(email, phoneNumber)).thenReturn(false);
//        when(userRepoMock.save(any(User.class))).thenAnswer(invocationOnMock -> {
//            User userToSave = invocationOnMock.getArgument(0);
//            userToSave.setId(expectedId);
//            return userToSave;
//        });
//        when(userMapperMock.mapToUser(userForm)).thenReturn(user);
//
//        User signupUser = userService.signup(userForm);
//        Long actualId = signupUser.getId();
//
//        when(userRepoMock.findById(actualId)).thenReturn(Optional.of(signupUser));
//
//        User savedUser = userService.findById(actualId);
//
//        verify(userRepoMock, times(1)).save(userCaptor.capture());
//        verify(userRepoMock, times(1)).findById(actualId);
//
//        assertEquals(expectedId, actualId);
//        assertEquals(user, savedUser);
//
//    }
//
//    @Test
//    void testSignupExistingUserTest() {
//        String email = user.getEmail();
//        String phoneNumber = user.getPhoneNumber();
//
//        when(userRepoMock.existsByEmailOrPhoneNumber(email, phoneNumber)).thenReturn(true);
//
//        assertThrows(EntityExistsException.class, () -> userService.signup(userForm));
//        verify(userRepoMock, times(1)).existsByEmailOrPhoneNumber(email, phoneNumber);
//    }
//
//
//    @Test
//    void testUpdateTest() {
//        long id = 1L;
//
//        String email = "email@gmail.com";
//        String phoneNumber = "380981258958";
//        userForm.setEmail(email);
//        userForm.setPhoneNumber(phoneNumber);
//
//        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));
//        when(userRepoMock.existsByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(false);
//        when(userRepoMock.save(any(User.class))).thenAnswer(invocationOnMock -> {
//            User userToSave = invocationOnMock.getArgument(0);
//            return userToSave.toBuilder()
//                    .id(id)
//                    .email(userForm.getEmail())
//                    .password(userForm.getPassword())
//                    .phoneNumber(userForm.getPhoneNumber())
//                    .firstname(userForm.getFirstname())
//                    .lastname(userForm.getLastname())
//                    .build();
//        });
//        when(userMapperMock.mapToUser(userForm)).thenReturn(user);
//
//        User updatedUser = userService.update(userForm, id);
//
//        verify(userRepoMock, times(1)).existsByEmailAndPhoneNumber(email, phoneNumber);
//        verify(userRepoMock, times(1)).findById(id);
//        verify(userRepoMock, times(1)).save(userCaptor.capture());
//
//        assertNotNull(updatedUser);
//        assertEquals(userForm.getEmail(), updatedUser.getEmail());
//        assertEquals(userCaptor.getValue().getId(), updatedUser.getId());
//
//    }
//
//    @Test
//    void testUpdateNonExistingUserTest() {
//        long id = 1L;
//        assertThrows(EntityNotFoundException.class, () -> userService.update(new UserForm(), id));
//    }
//
//    @Test
//    void testUpdateWithAlreadyExistingEmailAndPhoneNumber() {
//        long id = 1L;
//        String email = "email@gmail.com";
//        String phoneNumber = "380981258958";
//        userForm.setEmail(email);
//        userForm.setPhoneNumber(phoneNumber);
//
//        user.setId(id);
//        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));
//        when(userRepoMock.existsByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(true);
//
//        assertThrows(EntityExistsException.class, () -> userService.update(userForm, id));
//    }
//
//    @Test
//    void testUpdateWithAlreadyExistingEmail() {
//        long id = 1L;
//        String email = "email@gmail.com";
//        userForm.setEmail(email);
//
//        user.setId(id);
//        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));
//        when(userRepoMock.existsByEmail(email)).thenReturn(true);
//
//        assertThrows(EntityExistsException.class, () -> userService.update(userForm, id));
//    }
//
//    @Test
//    void testUpdateWithAlreadyExistingPhoneNumber() {
//        long id = 1L;
//        String phoneNumber = "380981258958";
//        userForm.setPhoneNumber(phoneNumber);
//
//        user.setId(id);
//        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));
//        when(userRepoMock.existsByPhoneNumber(phoneNumber)).thenReturn(true);
//
//        assertThrows(EntityExistsException.class, () -> userService.update(userForm, id));
//    }
//
//    @Test
//    void testDeleteNonexistentUser() {
//        long id = 1L;
//        when(userRepoMock.existsById(id)).thenReturn(false);
//
//        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
//    }
//
//}