package bank.mapper.impl;

import bank.dto.transaction.TransactionRequestDto;
import bank.dto.transaction.TransactionResponseDto;
import bank.mapper.TransactionMapper;
import bank.model.Transaction;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    private final ModelMapper modelMapper;
    private final TypeMap<Transaction, TransactionResponseDto> typeMap;

    public TransactionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        Converter<LocalDateTime, String> localDateTimeStringConverter =
                converter -> converter.getSource().toString();
        this.typeMap = modelMapper.createTypeMap(Transaction.class, TransactionResponseDto.class)
                .addMappings(
                        mapper -> mapper.using(localDateTimeStringConverter).map(Transaction::getTransactionDate, TransactionResponseDto::setTransactionDate)
                );
    }

    @Override
    public Transaction mapToEntity(TransactionRequestDto transactionRequestDto) {
        return modelMapper.map(transactionRequestDto, Transaction.class);
    }

    @Override
    public TransactionResponseDto mapToResponseDto(Transaction transaction) {
        return typeMap.map(transaction);
    }

}
