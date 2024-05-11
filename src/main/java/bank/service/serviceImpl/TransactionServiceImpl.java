package bank.service.serviceImpl;

import bank.dto.transaction.TransactionResponseDto;
import bank.mapper.TransactionMapper;
import bank.repository.UserRepository;
import bank.service.BankService;
import bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponseDto> getBankAccountTransactions(Long bankAccountId) {
        return transactionMapper
                .mapEntityListToResponseDtoList(
                        userRepository.findTransactionsByBankAccountId(bankAccountId)
                );
    }

    @Override
    public boolean clearBankAccountTransactions(Long bankAccountId) {
        return userRepository.deleteTransactionsByBankAccountId(bankAccountId);
    }

}