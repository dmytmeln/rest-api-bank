package bank.service;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.model.BankAccount;

public interface BankService {

    BankAccount findById(Long accountId, Long userId);

    BankResponseDto findBankResponseById(Long accountId, Long userId);

    BankResponseDto creteBankAccountForUser(Long userId);

    boolean deleteBankAccount(Long accountId, Long userId);

    BankResponseDto makeDeposit(Long accountId, Long userId, TransactionRequestDto transactionRequestDto);

    BankResponseDto makeWithdrawal(Long accountId, Long userId, TransactionRequestDto transactionRequestDto);

}
