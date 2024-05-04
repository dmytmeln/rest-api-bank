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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final UserRepository userRepository;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public BankAccount findById(Long accountId, Long userId) {
        return userRepository.findBankAccountByBankAndUserIds(accountId, userId).orElseThrow(
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
    @Transactional
    public BankResponseDto makeDeposit(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        BankAccount bankAccount = findById(accountId, userId);
        double newBalance = bankAccount.getBalance() + transactionRequestDto.getMoneyAmount();
        bankAccount.setBalance(newBalance);
        bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));

        updateUserAccount(user, bankAccount);

        return bankAccountMapper.mapToBankResponseDto(bankAccount);

    }

    @Override
    @Transactional
    public BankResponseDto makeWithdrawal(Long accountId, Long userId, TransactionRequestDto transactionRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        BankAccount bankAccount = findById(accountId, userId);
        Double moneyAmount = transactionRequestDto.getMoneyAmount();
        double newBalance = bankAccount.getBalance() - moneyAmount;
        if (newBalance < 0) {
            throw  new IllegalArgumentException("You don't have enough money to withdraw [%f$]".formatted(moneyAmount));
        }
        bankAccount.setBalance(newBalance);
        bankAccount.addTransaction(transactionMapper.mapToEntity(transactionRequestDto));

        updateUserAccount(user, bankAccount);

        return bankAccountMapper.mapToBankResponseDto(bankAccount);
    }

    @Transactional
    void updateUserAccount(User user, BankAccount bankAccount) {
        Long accountId = bankAccount.getId();
        List<BankAccount> bankAccounts = user.getBankAccounts();
        for (int index = 0; index < bankAccounts.size(); index++) {
            if (Objects.equals(bankAccounts.get(index).getId(), accountId)) {
                bankAccounts.set(index, bankAccount);
                break;
            }
        }
        userRepository.save(user);
    }
//
//    @Transactional
//    BankAccount update(BankAccount bankAccount, TransactionRequestDto transactionRequestDto) {
//        Transaction transaction = transactionMapper.mapToEntity(transactionRequestDto);
//        if (transaction.getMsg().isBlank()) {
//            transaction.setMsg("Standard Transaction Message");
//        }
//
//        bankAccount.getTransactions().add(transaction);
//        return bankRepository.save(bankAccount);
//    }

}