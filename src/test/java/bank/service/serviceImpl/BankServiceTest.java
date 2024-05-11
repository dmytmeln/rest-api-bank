package bank.service.serviceImpl;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.exception.EntityNotFoundException;
import bank.mapper.BankAccountMapper;
import bank.mapper.TransactionMapper;
import bank.model.BankAccount;
import bank.model.Role;
import bank.model.Transaction;
import bank.model.User;
import bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @Mock
    private UserRepository userRepoMock;
    @Mock
    private TransactionMapper transactionMapperMock;
    @Mock
    private BankAccountMapper bankAccountMapperMock;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private BankServiceImpl bankService;

    private final Long id = 1L;
    private BankAccount bankAccount;

    @BeforeEach
    void init() {
        bankAccount = BankAccount.builder()
                .id(id)
                .balance(1000D)
                .transactions(new ArrayList<>())
                .build();
    }

    @Test
    void testFindById() {
        when(userRepoMock.findBankAccountByBankAndUserIds(id, id)).thenReturn(Optional.of(bankAccount));

        BankAccount result = bankService.findById(id, id);

        assertNotNull(result);
        assertEquals(bankAccount, result);
    }

    @Test
    void testInvalidFindById() {
        when(userRepoMock.findBankAccountByBankAndUserIds(id, id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bankService.findById(id, id));
    }

    @Test
    void testFindBankResponseById() {
        BankResponseDto bankResponseDto = new BankResponseDto(
                bankAccount.getId(),
                bankAccount.getBalance(),
                new ArrayList<>()
        );
        when(userRepoMock.findBankAccountByBankAndUserIds(id, id)).thenReturn(Optional.of(bankAccount));
        when(bankAccountMapperMock.mapToBankResponseDto(bankAccount)).thenReturn(bankResponseDto);

        BankResponseDto result = bankService.findBankResponseById(id, id);

        assertNotNull(result);
        assertEquals(bankResponseDto, result);
    }

    @Test
    void testInvalidFindBankResponseById() {
        when(userRepoMock.findBankAccountByBankAndUserIds(id, id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bankService.findById(id, id));
    }

    @Test
    void testCreateBankAccount() {
        int expectedSize = 2;
        long userId = 1L;
        long expectedBankAccountId = 2L;
        User user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .password("12!@asAS")
                .email("john.doe@example.com")
                .phoneNumber("123123123123")
                .role(Role.ROLE_USER)
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .build();
        BankResponseDto bankResponseDto = new BankResponseDto(id, bankAccount.getBalance(), List.of(id));

        when(userService.findById(userId)).thenReturn(user);
        when(userRepoMock.save(user))
                .thenAnswer(invocationOnMock -> {
                    User userToUpdate = invocationOnMock.getArgument(0);
                    List<BankAccount> bankAccounts = userToUpdate.getBankAccounts();
                    BankAccount bankAccountToUpdate = bankAccounts.get(bankAccounts.size() - 1);
                    bankAccountToUpdate.setId(expectedBankAccountId);
                    bankAccountToUpdate.setTransactions(new ArrayList<>());
                    return userToUpdate;
                });
        when(bankAccountMapperMock.mapToBankResponseDto(any(BankAccount.class))).thenReturn(bankResponseDto);

        BankResponseDto actualBankResponseDto = bankService.creteBankAccountForUser(userId);

        List<BankAccount> bankAccounts = user.getBankAccounts();
        int size = bankAccounts.size();
        assertEquals(expectedSize, size);
        assertEquals(expectedBankAccountId, bankAccounts.get(size - 1).getId());
        assertEquals(bankResponseDto.getId(), actualBankResponseDto.getId());

    }

    @Test
    void testDeleteBankAccount() {
        when(userRepoMock.findBankAccountByBankAndUserIds(id, id)).thenReturn(Optional.of(bankAccount));
        when(userRepoMock.deleteBankAccountWithoutUser(id)).thenReturn(true);

        boolean hasDeleted = bankService.deleteBankAccount(id, id);

        assertTrue(hasDeleted);
    }

    @Test
    void testMakeDepositTest() {

        double moneyAmount = 2000D;
        double expectedBalance = moneyAmount + bankAccount.getBalance();
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .type("Transaction Type")
                .msg("Transaction Msg")
                .moneyAmount(moneyAmount)
                .build();
        int expectedUserBankAccountsListSize = 1;
        long userId = 1L;
        User user = User.builder()
                .firstname("Peter")
                .lastname("Stinger")
                .email("dimon281@gmail.com")
                .password("asAS!@12")
                .phoneNumber("380981258958")
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .role(Role.ROLE_USER)
                .build();
        Transaction transaction = Transaction.builder()
                .id(id)
                .type(transactionRequestDto.getType())
                .msg(transactionRequestDto.getMsg())
                .moneyAmount(transactionRequestDto.getMoneyAmount())
                .transactionDate(LocalDateTime.now())
                .build();
        BankResponseDto bankResponseDto = new BankResponseDto(id, bankAccount.getBalance(), List.of(id));
        when(userService.findById(userId)).thenReturn(user);
        when(userRepoMock.save(user)).thenReturn(user);
        when(transactionMapperMock.mapToEntity(transactionRequestDto)).thenReturn(transaction);
        when(bankAccountMapperMock.mapToBankResponseDto(bankAccount)).thenReturn(bankResponseDto);
        when(userRepoMock.findBankAccountByBankAndUserIds(userId, id)).thenReturn(Optional.of(bankAccount));
        BankResponseDto result = bankService.makeDeposit(id, userId, transactionRequestDto);

        assertEquals(bankResponseDto, result);
        assertEquals(bankResponseDto, result);
        List<Transaction> transactions = bankAccount.getTransactions();
        assertFalse(transactions.isEmpty());
        assertEquals(transaction, transactions.get(0));
        List<BankAccount> bankAccounts = user.getBankAccounts();
        assertEquals(expectedUserBankAccountsListSize, bankAccounts.size());
        BankAccount actual = bankAccounts.get(0);
        assertEquals(bankAccount, actual);
        assertEquals(expectedBalance, actual.getBalance());

    }


    @Test
    void testMakeWithdrawalTest() {
        double moneyAmount = 500D;
        double expectedBalance = bankAccount.getBalance() - moneyAmount;
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .type("Transaction Type")
                .msg("Transaction Msg")
                .moneyAmount(moneyAmount)
                .build();
        final int expectedUserBankAccountsListSize = 1;
        long userId = 1L;
        User user = User.builder()
                .firstname("Peter")
                .lastname("Stinger")
                .email("dimon281@gmail.com")
                .password("asAS!@12")
                .phoneNumber("380981258958")
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .role(Role.ROLE_USER)
                .build();
        Transaction transaction = Transaction.builder()
                .id(id)
                .type(transactionRequestDto.getType())
                .msg(transactionRequestDto.getMsg())
                .moneyAmount(transactionRequestDto.getMoneyAmount())
                .transactionDate(LocalDateTime.now())
                .build();
        BankResponseDto bankResponseDto = new BankResponseDto(id, bankAccount.getBalance(), List.of(id));
        when(userService.findById(id)).thenReturn(user);
        when(userRepoMock.save(user)).thenReturn(user);
        when(userRepoMock.findBankAccountByBankAndUserIds(userId, id)).thenReturn(Optional.of(bankAccount));
        when(transactionMapperMock.mapToEntity(transactionRequestDto)).thenReturn(transaction);
        when(bankAccountMapperMock.mapToBankResponseDto(bankAccount)).thenReturn(bankResponseDto);

        BankResponseDto result = bankService.makeWithdrawal(id, userId, transactionRequestDto);

        assertEquals(bankResponseDto, result);
        assertEquals(bankResponseDto, result);
        List<Transaction> transactions = bankAccount.getTransactions();
        assertFalse(transactions.isEmpty());
        assertEquals(transaction, transactions.get(0));
        List<BankAccount> bankAccounts = user.getBankAccounts();
        assertEquals(expectedUserBankAccountsListSize, bankAccounts.size());
        BankAccount actual = bankAccounts.get(0);
        assertEquals(bankAccount, actual);
        assertEquals(expectedBalance, actual.getBalance());
    }

    @Test
    void testInvalidMakeWithdrawalTest() {
        String expectedInfo = "Transaction Test Withdrawal";
        double moneyAmount = 20000.;
        TransactionRequestDto transactionRequestDto = TransactionRequestDto.builder()
                .moneyAmount(moneyAmount)
                .msg(expectedInfo)
                .type(expectedInfo)
                .build();
        long userId = 1L;
        User user = User.builder()
                .firstname("Peter")
                .lastname("Stinger")
                .email("dimon281@gmail.com")
                .password("asAS!@12")
                .phoneNumber("380981258958")
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .role(Role.ROLE_USER)
                .build();

        when(userRepoMock.findBankAccountByBankAndUserIds(userId, id)).thenReturn(Optional.of(bankAccount));

        assertThrows(IllegalArgumentException.class, () -> bankService.makeWithdrawal(id, id, transactionRequestDto));
    }

    @Test
    void testUpdateUserBankAccount_existingBankAccount() {

        long userId = 1L;
        User user = User.builder()
                .firstname("Peter")
                .lastname("Stinger")
                .email("dimon281@gmail.com")
                .password("asAS!@12")
                .phoneNumber("380981258958")
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .role(Role.ROLE_USER)
                .build();
        double newBalance = 2000D;
        BankAccount bankAccountToUpdate = BankAccount.builder()
                .id(id)
                .balance(newBalance)
                .transactions(new ArrayList<>())
                .build();

        when(userService.findById(userId)).thenReturn(user);
        when(userRepoMock.save(any(User.class)))
                .thenAnswer(invocationOnMock
                        -> invocationOnMock.getArgument(0));

        BankAccount updated = bankService.updateUserBankAccount(userId, bankAccountToUpdate);

        verify(userService, times(1)).findById(userId);
        verify(userRepoMock, times(1)).save(any(User.class));

        assertEquals(newBalance, updated.getBalance());
        assertEquals(id, updated.getId());

    }

    @Test
    void testUpdateUserBankAccount_nonExistingBankAccount() {

        long userId = 1L;
        User user = User.builder()
                .firstname("Peter")
                .lastname("Stinger")
                .email("dimon281@gmail.com")
                .password("asAS!@12")
                .phoneNumber("380981258958")
                .bankAccounts(new ArrayList<>(List.of(bankAccount)))
                .role(Role.ROLE_USER)
                .build();
        double newBalance = 2000D;
        long nonExistingBankAccountId = -1L;
        BankAccount bankAccountToUpdate = BankAccount.builder()
                .id(nonExistingBankAccountId)
                .balance(newBalance)
                .transactions(new ArrayList<>())
                .build();

        when(userService.findById(userId)).thenReturn(user);

        assertThrows(
                EntityNotFoundException.class,
                () -> bankService.updateUserBankAccount(userId, bankAccountToUpdate)
        );

        verify(userService, times(1)).findById(userId);

    }



}