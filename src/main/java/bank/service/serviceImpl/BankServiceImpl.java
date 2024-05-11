package bank.service.serviceImpl;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.exception.EntityNotFoundException;
import bank.mapper.BankAccountMapper;
import bank.mapper.TransactionMapper;
import bank.model.BankAccount;
import bank.model.User;
import bank.repository.UserRepository;
import bank.service.BankService;
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
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public BankAccount findById(Long accountId, Long userId) {
        return userRepository.findBankAccountByBankAndUserIds(accountId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Account with id [%d] and/or user id [%d] not found!".formatted(accountId, userId)
                        )
                );
    }

    @Override
    public BankResponseDto findBankResponseById(Long accountId, Long userId) {
        BankAccount bankAccount = findById(accountId, userId);
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
        findById(accountId, userId);
        return userRepository.deleteBankAccountWithoutUser(accountId);
    }

    @Override
    @Transactional
    public BankResponseDto makeDeposit(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {

        User user = userService.findById(userId);

        BankResponseDto bankResponseDto = null;
        List<BankAccount> bankAccounts = user.getBankAccounts();
        for (int index = 0; index < bankAccounts.size(); index++) {

            BankAccount bankAccount = bankAccounts.get(index);

            if (Objects.equals(bankAccount.getId(), accountId)) {
                double newBalance = bankAccount.getBalance() + transactionRequestDto.getMoneyAmount();
                bankAccount.setBalance(newBalance);
                bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));
                bankAccounts.set(index, bankAccount);
                bankResponseDto = bankAccountMapper.mapToBankResponseDto(bankAccount);
                break;
            }
        }
        if (bankResponseDto == null) {
            throw new EntityNotFoundException("Bank account with id [%d] not found!".formatted(accountId));
        }
        userRepository.save(user);

        return bankResponseDto;

    }

    @Override
    @Transactional
    public BankResponseDto makeWithdrawal(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {

        User user = userService.findById(userId);

        BankResponseDto bankResponseDto = null;
        List<BankAccount> bankAccounts = user.getBankAccounts();
        for (int index = 0; index < bankAccounts.size(); index++) {

            BankAccount bankAccount = bankAccounts.get(index);

            if (Objects.equals(bankAccount.getId(), accountId)) {
                Double moneyAmount = transactionRequestDto.getMoneyAmount();
                double newBalance = bankAccount.getBalance() - moneyAmount;
                if (newBalance < 0) {
                    throw  new IllegalArgumentException("You don't have enough money to withdraw [%f$]".formatted(moneyAmount));
                }
                bankAccount.setBalance(newBalance);
                bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));
                bankAccounts.set(index, bankAccount);
                bankResponseDto = bankAccountMapper.mapToBankResponseDto(bankAccount);
                break;
            }
        }
        if (bankResponseDto == null) {
            throw new EntityNotFoundException("Bank account with id [%d] not found!".formatted(accountId));
        }
        userRepository.save(user);

        return bankResponseDto;

    }



}