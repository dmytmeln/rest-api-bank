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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    private final ModelMapper modelMapper;
    private final TypeMap<Transaction, TransactionResponseDto> typeMap;

    public TransactionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        Converter<LocalDateTime, String> localDateTimeStringConverter =
                converter -> converter.getSource().format(DateTimeFormatter.ofPattern("d-M-uuuu H:m:s"));
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

    @Override
    public List<TransactionResponseDto> mapEntityListToResponseDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
}
