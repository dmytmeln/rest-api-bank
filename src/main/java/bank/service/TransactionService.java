package bank.service;

import bank.dto.transaction.TransactionResponseDto;

import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> getBankAccountTransactions(Long bankAccountId);

    void clearBankAccountTransactions(Long bankAccountId);

}
