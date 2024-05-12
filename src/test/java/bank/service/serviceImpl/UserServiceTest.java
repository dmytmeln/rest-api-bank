package bank.service.serviceImpl;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.exception.EntityAlreadyExistsException;
import bank.exception.EntityNotFoundException;
import bank.mapper.UserMapper;
import bank.model.Role;
import bank.model.User;
import bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepoMock;
    @Mock
    private UserMapper userMapperMock;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .password("12!@asAS")
                .email("john.doe@example.com")
                .phoneNumber("123123123123")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void testFindById_valid() {
        Long id = 1L;
        user.setId(id);
        when(userRepoMock.findById(id)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.findById(id);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindById_invalid() {
        Long userId = 1L;
        when(userRepoMock.findById(userId)).thenThrow(new EntityNotFoundException());
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(userId)
        );
    }

    @Test
    void testFindResponseById() {

        Long userId = 1L;
        long bankAccountId = 1L;
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role("ROLE_USER")
                .bankAccountsId(Set.of(bankAccountId))
                .build();

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userMapperMock.mapToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto actualUserResponseDto = userService.findResponseById(userId);

        assertEquals(userResponseDto, actualUserResponseDto);

    }

    @Test
    void testFindResponseById_nonExistingUser() {
        Long userId = -1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findResponseById(userId)
        );
    }

    @Test
    void testExistsByEmailOrPhoneNumber_valid() {
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        when(userRepoMock.existsByEmailOrPhoneNumber(email, phoneNumber)).thenReturn(false);
        assertDoesNotThrow(() -> userService.existsByEmailOrPhoneNumber(userRequestDto));
    }

    @Test
    void testExistsByEmailOrPhoneNumber_invalid() {
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        when(userRepoMock.existsByEmailOrPhoneNumber(email, phoneNumber)).thenReturn(true);
        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.existsByEmailOrPhoneNumber(userRequestDto)
        );
    }

    @Test
    void testSignupTest() {

        long expectedId = 2;
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(expectedId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .email(email)
                .role("ROLE_USER")
                .bankAccountsId(Set.of(expectedId))
                .build();


        when(userRepoMock.existsByEmailOrPhoneNumber(email, phoneNumber)).thenReturn(false);
        when(userMapperMock.mapToEntity(userRequestDto)).thenReturn(user);
        when(userRepoMock.save(user)).thenReturn(user);
        when(userMapperMock.mapToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto signupUser = userService.signup(userRequestDto);
        String actualEmail = signupUser.getEmail(),
                actualPhoneNumber = signupUser.getPhoneNumber();
        Long actualId = signupUser.getId();

        verify(userRepoMock, times(1)).save(user);
        verify(userRepoMock, times(1)).existsByEmailOrPhoneNumber(email, phoneNumber);
        verify(userMapperMock, times(1)).mapToResponseDto(user);
        verify(userMapperMock, times(1)).mapToEntity(userRequestDto);

        assertEquals(expectedId, actualId);
        assertEquals(email, actualEmail);
        assertEquals(phoneNumber, actualPhoneNumber);
        Set<Long> bankAccountsId = signupUser.getBankAccountsId();
        assertFalse(bankAccountsId.isEmpty());
        assertEquals(expectedId, bankAccountsId.iterator().next());

    }

    @Test
    void testAlreadyExists_changedEmailAndPhoneNumber_doesntExist() {
        String email = "email@gmail.com";
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(false);

        assertDoesNotThrow(() -> userService.alreadyExists(userRequestDto, userId));
    }

    @Test
    void testAlreadyExists_changedEmailAndPhoneNumber_alreadyExists() {
        String email = "email@gmail.com";
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(true);

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.alreadyExists(userRequestDto, userId)
        );
    }

    @Test
    void testAlreadyExists_changedEmail_doesntExist() {
        String email = "email@gmail.com";
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userService.alreadyExists(userRequestDto, userId));
    }

    @Test
    void testAlreadyExists_changedEmail_alreadyExists() {
        String email = "email@gmail.com";
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmail(email)).thenReturn(true);

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.alreadyExists(userRequestDto, userId)
        );
    }

    @Test
    void testAlreadyExists_changedPhoneNumber_doesntExist() {
        String email = user.getEmail();
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByPhoneNumber(phoneNumber)).thenReturn(false);

        assertDoesNotThrow(() -> userService.alreadyExists(userRequestDto, userId));
    }

    @Test
    void testAlreadyExists_changedPhoneNumber_alreadyExists() {
        String email = user.getEmail();
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();

        long userId = 1L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.alreadyExists(userRequestDto, userId)
        );
    }

    @Test
    void testUpdate_updateEmailAndPhoneNumber() {

        long userId = 1L;
        String email = "email@gmail.com";
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(false);
        when(userMapperMock.mapToEntity(userRequestDto)).thenReturn(user);
        when(userRepoMock.updateWithoutBankAccount(user))
                .thenAnswer(invocationOnMock -> {
                    User userToUpdate = invocationOnMock.getArgument(0);
                    userToUpdate.setEmail(email);
                    userToUpdate.setPhoneNumber(phoneNumber);
                    userToUpdate.setId(userId);
                    return true;
                });
        when(userMapperMock.mapToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto actualResponseDto = userService.update(userRequestDto, userId);

        assertEquals(userResponseDto, actualResponseDto);
        assertEquals(email, user.getEmail());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(userId, user.getId());

    }

    @Test
    void testUpdate_updateEmail() {

        long userId = 1L;
        String email = "email@gmail.com";
        String phoneNumber = user.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByEmail(email)).thenReturn(false);
        when(userMapperMock.mapToEntity(userRequestDto)).thenReturn(user);
        when(userRepoMock.updateWithoutBankAccount(user))
                .thenAnswer(invocationOnMock -> {
                    User userToUpdate = invocationOnMock.getArgument(0);
                    userToUpdate.setEmail(email);
                    userToUpdate.setPhoneNumber(phoneNumber);
                    userToUpdate.setId(userId);
                    return true;
                });
        when(userMapperMock.mapToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto actualResponseDto = userService.update(userRequestDto, userId);

        assertEquals(userResponseDto, actualResponseDto);
        assertEquals(email, user.getEmail());
        assertEquals(userId, user.getId());

    }

    @Test
    void testUpdate_updatePhoneNumber() {

        long userId = 1L;
        String email = user.getEmail();
        String phoneNumber = "380966666666";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .password(user.getPassword())
                .email(email)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepoMock.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        when(userMapperMock.mapToEntity(userRequestDto)).thenReturn(user);
        when(userRepoMock.updateWithoutBankAccount(user))
                .thenAnswer(invocationOnMock -> {
                    User userToUpdate = invocationOnMock.getArgument(0);
                    userToUpdate.setEmail(email);
                    userToUpdate.setPhoneNumber(phoneNumber);
                    userToUpdate.setId(userId);
                    return true;
                });
        when(userMapperMock.mapToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto actualResponseDto = userService.update(userRequestDto, userId);

        assertEquals(userResponseDto, actualResponseDto);
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(userId, user.getId());

    }

    @Test
    void testDelete_valid() {
        long userId = 1L;
        when(userRepoMock.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userService.delete(userId));
    }

    @Test
    void testDelete_invalid() {
        long id = 1L;
        when(userRepoMock.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
    }

}