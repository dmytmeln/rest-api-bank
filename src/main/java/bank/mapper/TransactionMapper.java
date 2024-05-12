package bank.mapper;

import bank.dto.transaction.TransactionRequestDto;
import bank.dto.transaction.TransactionResponseDto;
import bank.model.Transaction;

import java.util.List;

public interface TransactionMapper {

    Transaction mapToEntity(TransactionRequestDto transactionRequestDto);

    TransactionResponseDto mapToResponseDto(Transaction transaction);

    List<TransactionResponseDto> mapEntityListToResponseDtoList(List<Transaction> transactions);

}
