package bank.mapper;

import bank.dto.bank.BankResponseDto;
import bank.model.BankAccount;

public interface BankAccountMapper {

    BankResponseDto mapToBankResponseDto(BankAccount bankAccount);

}
