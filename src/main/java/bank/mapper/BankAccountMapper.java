package bank.mapper;

import bank.dto.bank.BankResponseDto;
import bank.model.BankAccount;
import bank.model.Transaction;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankAccountMapper {

    private final TypeMap<BankAccount, BankResponseDto> typeMap;
    private final ModelMapper modelMapper;

    public BankAccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        Converter<List<Transaction>, List<Long>> listConverter = converter -> converter.getSource().stream()
            .map(Transaction::getId)
            .collect(Collectors.toList());
        this.typeMap = modelMapper.createTypeMap(BankAccount.class, BankResponseDto.class)
            .addMappings(
                mapper -> mapper.using(listConverter).map(BankAccount::getTransactions, BankResponseDto::setTransactionsId)
            );
    }

    public BankResponseDto mapToBankResponseDto(BankAccount bankAccount) {
        return typeMap.map(bankAccount);
    }

}
