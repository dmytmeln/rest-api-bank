package bank;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.exception.EntityAlreadyExistsException;
import bank.exception.EntityNotFoundException;
import bank.model.User;
import bank.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class UserIntegrationTest {

    private final UserService userService;

    private final Long realUserId = 1L;
    private final User userDb;

    @Autowired
    public UserIntegrationTest(UserService userService) {
        this.userService = userService;

        userDb = this.userService.findById(realUserId);
    }

    @Test
    public void testFindById() {

        String expectedEmail = userDb.getEmail();

        User user = userService.findById(realUserId);

        assertEquals(realUserId, user.getId());
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    public void testFindById_nonExistingUser() {

        long nonExistingUserId = -1L;

        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(nonExistingUserId)
        );
    }

    @Test
    public void testFindResponseById() {

        String expectedEmail = userDb.getEmail();

        UserResponseDto userResponseDto = userService.findResponseById(realUserId);

        assertEquals(realUserId, userResponseDto.getId());
        assertEquals(expectedEmail, userResponseDto.getEmail());
    }

    @Test
    public void testFindResponseById_nonExistingUser() {

        long nonExistingUserId = -1L;

        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findResponseById(nonExistingUserId)
        );
    }

    @Test
    public void testExitsByEmailOrPhoneNumber_realEmail() {

        String userDbEmail = userDb.getEmail();
        String nonExistingPhoneNumber = "123";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(userDbEmail)
                .phoneNumber(nonExistingPhoneNumber)
                .build();

        boolean existsByEmailOrPhoneNumber = userService.existsByEmailOrPhoneNumber(userRequestDto);

        assertTrue(existsByEmailOrPhoneNumber);
    }

    @Test
    public void testExitsByEmailOrPhoneNumber_realPhoneNumber() {

        String nonExistingEmail = "123";
        String userDbPhoneNumber = userDb.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(nonExistingEmail)
                .phoneNumber(userDbPhoneNumber)
                .build();

        boolean existsByEmailOrPhoneNumber = userService.existsByEmailOrPhoneNumber(userRequestDto);

        assertTrue(existsByEmailOrPhoneNumber);
    }

    @Test
    public void testExitsByEmailOrPhoneNumber_realEmailAndPhoneNumber() {

        String userDbEmail = userDb.getEmail();
        String userDbPhoneNumber = userDb.getPhoneNumber();
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(userDbEmail)
                .phoneNumber(userDbPhoneNumber)
                .build();

        boolean existsByEmailOrPhoneNumber = userService.existsByEmailOrPhoneNumber(userRequestDto);

        assertTrue(existsByEmailOrPhoneNumber);
    }

    @Test
    @Order(1)
    public void testSignup_nonExistingUser() {

        long expectedUserId = 2L;
        long expectedBankAccountId = 2L;

        String email = "someEmail10@gmail.com";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email(email)
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        assertEquals(expectedUserId, userResponseDto.getId());
        assertEquals(email, userRequestDto.getEmail());
        assertDoesNotThrow(() -> userService.findById(expectedUserId));

        Set<Long> bankAccountsId = userResponseDto.getBankAccountsId();
        assertFalse(bankAccountsId.isEmpty());
        assertEquals(expectedBankAccountId, bankAccountsId.iterator().next());
    }

    @Test
    public void testSignup_userWithAlreadyExistingEmail() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email(userDb.getEmail())
                .phoneNumber("380923344555")
                .build();

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.signup(userRequestDto)
        );
    }

    @Test
    public void testSignup_userWithAlreadyExistingPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail@gmail.com")
                .phoneNumber(userDb.getPhoneNumber())
                .build();

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.signup(userRequestDto)
        );
    }

    @Test
    public void testSignup_userWithAlreadyExistingEmailAndPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email(userDb.getEmail())
                .phoneNumber(userDb.getPhoneNumber())
                .build();

        assertThrows(
                EntityAlreadyExistsException.class,
                () -> userService.signup(userRequestDto)
        );
    }

    @Test
    public void testAlreadyExists_changedEmail_alreadyExistingEmail() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .email(userDb.getEmail())
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertTrue(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumber_alreadyExistingPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber(userDb.getPhoneNumber())
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertTrue(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumberAndEmail_alreadyExistingPhoneNumberAndEmail() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber(userDb.getPhoneNumber())
                .email(userDb.getEmail())
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertTrue(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumberAndEmail_alreadyExistingEmail() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber("380934455666")
                .email(userDb.getEmail())
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertTrue(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumberAndEmail_alreadyExistingPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber(userDb.getPhoneNumber())
                .email("nonExistingEmail@gmail.com")
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertTrue(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumberAndEmail_nonExistingEmailAndPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber("380978899000")
                .email("nonExistingEmail@gmail.com")
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertFalse(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedPhoneNumber_nonExistingPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .phoneNumber("380978899000")
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertFalse(alreadyExists);
    }

    @Test
    public void testAlreadyExists_changedEmail_nonExistingEmail() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("Peter")
                .lastname("Stringer")
                .password("1234")
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        userRequestDto = userRequestDto.toBuilder()
                .email("nonExistingEmail@gmail.com")
                .build();

        boolean alreadyExists = userService.alreadyExists(userRequestDto, userResponseDto.getId());

        assertFalse(alreadyExists);
    }

    @Test
    public void testUpdateUser_updateEmailAndPhoneNumber() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname(userDb.getFirstname())
                .lastname(userDb.getLastname())
                .password(userDb.getPassword())
                .email("someEmail10@gmail.com")
                .phoneNumber("380923344555")
                .build();

        UserResponseDto responseDto = userService.update(userRequestDto, realUserId);

        assertEquals(realUserId, responseDto.getId());
        assertEquals(userRequestDto.getEmail(), responseDto.getEmail());
        assertEquals(responseDto, userService.findResponseById(realUserId));
    }

    @Test
    public void testDelete() {

        assertDoesNotThrow(() -> userService.delete(realUserId));
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(realUserId)
        );
    }

    @Test
    public void testDelete_nonExistingUser() {

        long nonExistingUserId = -1L;
        assertThrows(
                EntityNotFoundException.class,
                () -> userService.delete(nonExistingUserId)
        );
    }



}
