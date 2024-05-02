package bank.mapper;

import bank.dto.transaction.TransactionRequestDto;
import bank.dto.transaction.TransactionResponseDto;
import bank.model.Transaction;

public interface TransactionMapper {

    Transaction mapToEntity(TransactionRequestDto transactionRequestDto);

    TransactionResponseDto mapToResponseDto(Transaction transaction);

}
