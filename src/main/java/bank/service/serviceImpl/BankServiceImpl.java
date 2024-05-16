package bank.service.serviceImpl;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.exception.EntityNotFoundException;
import bank.mapper.BankAccountMapper;
import bank.mapper.TransactionMapper;
import bank.model.BankAccount;
import bank.model.Transaction;
import bank.model.User;
import bank.repository.UserRepository;
import bank.service.BankService;
import bank.service.TransactionService;
import bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public BankAccount findById(Long userId, Long accountId) {
         return userRepository.findBankAccountByBankAndUserIds(userId, accountId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Either Bank Account with id [%d] not found or you're not the owner of this account!".formatted(accountId)
                        )
                );
    }

    @Override
    public BankResponseDto findBankResponseById(Long userId, Long accountId) {
        BankAccount bankAccount = findById(userId, accountId);
        return bankAccountMapper.mapToBankResponseDto(bankAccount);
    }

    @Override
    public BankResponseDto creteBankAccountForUser(Long userId) {
        User user = userService.findById(userId);
        user.addBankAccount(
                BankAccount.builder()
                        .balance(0D)
                        .build()
        );
        User saved = userRepository.save(user);
        List<BankAccount> bankAccounts = saved.getBankAccounts();

        return bankAccountMapper.mapToBankResponseDto(bankAccounts.get(bankAccounts.size() - 1));
    }

    @Override
    public boolean deleteBankAccount(Long accountId, Long userId) {
        BankAccount bankAccount = findById(userId, accountId);
        if (!bankAccount.getTransactions().isEmpty()) {
            transactionService.clearBankAccountTransactions(accountId);
        }

        return userRepository.deleteBankAccountWithoutUser(accountId);
    }

    @Override
    public BankAccount updateUserBankAccount(Long userId, BankAccount bankAccountToUpdate) {
        User user = userService.findById(userId);

        List<BankAccount> bankAccounts = user.getBankAccounts();
        Long accountId = bankAccountToUpdate.getId();
        for (int index = 0; index < bankAccounts.size(); index++) {
            BankAccount oldBankAccount = bankAccounts.get(index);

            if (Objects.equals(oldBankAccount.getId(), accountId)) {
                addTransactions(oldBankAccount, bankAccountToUpdate);
                bankAccounts.set(index, bankAccountToUpdate);
                userRepository.save(user);
                return bankAccountToUpdate;
            }
        }

        throw new EntityNotFoundException(
                "Bank account with id [%d] not found!".formatted(accountId)
        );
    }

    private void addTransactions(BankAccount from, BankAccount to) {
        boolean isInList;
        for (Transaction newTransaction : to.getTransactions()) {
            Long newTransactionId = newTransaction.getId();
            // check if transaction is already in list
            isInList = from.getTransactions().stream()
                    .anyMatch(oldTransaction -> Objects.equals(newTransactionId, oldTransaction.getId()));
            if (!isInList) {
                from.addTransaction(newTransaction);
            }
        }
        // add transactions this way to preserve order of transactions
        to.setTransactions(from.getTransactions());
    }

    @Override
    @Transactional
    public BankResponseDto makeDeposit(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {

        BankAccount bankAccount = findById(userId, accountId);

        bankAccount.setBalance(bankAccount.getBalance() + transactionRequestDto.getMoneyAmount());
        bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));
        BankAccount updatedUserBankAccount = updateUserBankAccount(userId, bankAccount);

        return bankAccountMapper.mapToBankResponseDto(updatedUserBankAccount);

    }

    @Override
    @Transactional
    public BankResponseDto makeWithdrawal(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {

        BankAccount bankAccount = findById(userId, accountId);

        // Check if there's enough money in the User's Bank Account
        double moneyAmount = transactionRequestDto.getMoneyAmount();
        double newBalance = bankAccount.getBalance() - moneyAmount;
        if (newBalance < 0) {
            throw  new IllegalArgumentException("You don't have enough money to withdraw [%f$]".formatted(moneyAmount));
        }

        // Update Bank Account
        bankAccount.setBalance(newBalance);
        bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));
        BankAccount updatedUserBankAccount = updateUserBankAccount(userId, bankAccount);

        return bankAccountMapper.mapToBankResponseDto(updatedUserBankAccount);

    }

}