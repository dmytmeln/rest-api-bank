package bank.repository;

import bank.model.BankAccount;
import bank.model.Role;
import bank.model.Transaction;
import bank.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
public class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testFindAllUsers() {
        int expectedSize = 1;
        List<User> allUsers = userRepository.findAll();

        assertEquals(expectedSize, allUsers.size(),
                "%d users should be in the users table".formatted(expectedSize));
    }

    @Test
    void testFindExistingUserById() {
        String expectedName = "Dmytro";
        long id = 1L;
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);

        assertEquals(id, user.getId());
        assertEquals(expectedName, user.getFirstname());
    }

    @Test
    void testFindNonExistingUserById() {
        long id = 0L;
        User user = userRepository.findById(id).orElse(null);

        assertNull(user);
    }

    @Test
    public void testFindBankAccountsByUserId() {
        int expectedSize = 1;
        Long expectedBankAccountId = 1L;
        Double expectedBalance = 1000D;

        Long userId = 1L;
        List<BankAccount> bankAccounts = userRepository.findBankAccountsByUserId(userId);
        BankAccount bankAccount = bankAccounts.get(0);

        assertEquals(expectedSize, bankAccounts.size());
        assertEquals(expectedBankAccountId, bankAccount.getId());
        assertEquals(expectedBalance, bankAccount.getBalance());
    }

    @Test
    void testCreateUser() {
        int expectedSize = 2;
        long expectedId = 2;
        String email = "dimon281@gmail.com";
        User user = User.builder()
                .email(email)
                .firstname("Peter")
                .lastname("Stinger")
                .password("asAS!@12")
                .role(Role.ROLE_USER)
                .phoneNumber("380981258958")
                .build();

        User savedUser = userRepository.save(user);

        assertEquals(expectedSize, userRepository.findAll().size());
        assertEquals(expectedId, savedUser.getId());
        assertEquals(email, savedUser.getEmail());
    }

    @Test
    void testUpdateUserTest() {
        int expectedSize = 1;
        long expectedId = 1;
        String email = "dimon281@gmail.com";
        String password = "asAS!@12";
        User user = User.builder()
                .email(email)
                .id(expectedId)
                .firstname("Peter")
                .lastname("Stinger")
                .role(Role.ROLE_USER)
                .password(password)
                .phoneNumber("380981258958")
                .build();

        userRepository.updateWithoutBankAccount(user);
        User updatedUser = userRepository.findById(expectedId).orElseThrow(RuntimeException::new);

        assertEquals(expectedSize, userRepository.findAll().size());
        assertEquals(expectedId, updatedUser.getId());
        assertEquals(email, updatedUser.getEmail());
        assertEquals(password, updatedUser.getPassword());
    }

    @Test
    void testDeleteTest() {
        long id = 1L;
        userRepository.deleteById(id);

        assertFalse(userRepository.existsById(id));
    }

    @Test
    void testFindUserByEmailAndPhoneNumberAndPasswordTest() {
        String existingEmail = "dimamel28@gmail.com";
        String existingPhoneNumber = "380984035791";
        String existingPass = "asAS12!@";

        long expectedId = 1;
        String expectedFirstName = "Dmytro";

        User user = userRepository.findUserByEmailAndPhoneNumberAndPassword(
                        existingEmail,
                        existingPhoneNumber,
                        existingPass
                )
                .orElseThrow(RuntimeException::new);

        assertEquals(expectedId, user.getId());
        assertEquals(expectedFirstName, user.getFirstname());
    }

    @Test
    void testFindUserByEmailAndPhoneNumberAndPasswordTest_NonExistingUser() {
        String existingEmail = "dimamel28@gmail.com";
        String existingPhoneNumber = "380984035791";
        String nonExistingPass = "1234";

        User user = userRepository.findUserByEmailAndPhoneNumberAndPassword(
                        existingEmail,
                        existingPhoneNumber,
                        nonExistingPass
                )
                .orElseThrow(RuntimeException::new);

        assertNull(user);
    }

    @Test
    void testExistsByEmailTest() {
        String existingEmail = "dimamel28@gmail.com";
        String nonExistingEmail = "123";

        assertFalse(userRepository.existsByEmail(nonExistingEmail));
        assertTrue(userRepository.existsByEmail(existingEmail));
    }

    @Test
    void testExistsByPhoneNumberTest() {
        String existingPhoneNumber = "380984035791";
        String nonExistingPhoneNumber = "123";

        assertFalse(userRepository.existsByPhoneNumber(nonExistingPhoneNumber));
        assertTrue(userRepository.existsByPhoneNumber(existingPhoneNumber));
    }

    @Test
    void testExistsByEmailAndPhoneNumber() {
        String existingEmail = "dimamel28@gmail.com";
        String existingPhoneNumber = "380984035791";
        String nonExistingEmail = "123";
        String nonExistingPhoneNumber = "123";

        assertFalse(userRepository.existsByEmailAndPhoneNumber(nonExistingEmail, nonExistingPhoneNumber));
        assertFalse(userRepository.existsByEmailAndPhoneNumber(existingEmail, nonExistingPhoneNumber));
        assertFalse(userRepository.existsByEmailAndPhoneNumber(nonExistingEmail, existingPhoneNumber));
        assertTrue(userRepository.existsByEmailAndPhoneNumber(existingEmail, existingPhoneNumber));
    }

}
