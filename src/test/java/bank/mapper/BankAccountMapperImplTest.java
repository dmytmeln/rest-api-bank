package bank.mapper;

import bank.dto.bank.BankResponseDto;
import bank.mapper.impl.BankAccountMapperImpl;
import bank.model.BankAccount;
import bank.model.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BankAccountMapperImplTest {
    private final BankAccountMapperImpl bankAccountMapperImpl = new BankAccountMapperImpl(new ModelMapper());

    private static BankAccount bankAccount;

    @BeforeAll
    public static void init() {
        long id = 1L;
        bankAccount = BankAccount.builder()
            .id(id)
            .balance(1000D)
            .transactions(List.of(Transaction.builder().id(id).build()))
            .build();
    }

    @Test
    public void testMapBankAccountToResponseDto() {
        BankResponseDto bankResponseDto = bankAccountMapperImpl.mapToBankResponseDto(bankAccount);

        assertNotNull(bankResponseDto);
        assertEquals(bankResponseDto.getId(), bankAccount.getId());
        assertEquals(bankResponseDto.getBalance(), bankAccount.getBalance());
        int index = 0;
        assertEquals(bankResponseDto.getTransactionsId().get(index), bankAccount.getTransactions().get(index).getId());
    }

}
