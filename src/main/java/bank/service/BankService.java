package bank.service;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.model.BankAccount;

public interface BankService {

    BankAccount findById(Long userId, Long accountId);

    BankResponseDto findBankResponseById(Long userId, Long accountId);

    BankResponseDto creteBankAccountForUser(Long userId);

    boolean deleteBankAccount(Long accountId, Long userId);

    BankResponseDto makeDeposit(Long accountId, Long userId, TransactionRequestDto transactionRequestDto);

    BankResponseDto makeWithdrawal(Long accountId, Long userId, TransactionRequestDto transactionRequestDto);

    BankAccount updateUserBankAccount(Long userId, BankAccount bankAccount);

}
