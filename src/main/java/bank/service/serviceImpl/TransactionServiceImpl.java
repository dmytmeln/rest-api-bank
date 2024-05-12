package bank.service.serviceImpl;

import bank.dto.transaction.TransactionResponseDto;
import bank.exception.EntityNotFoundException;
import bank.mapper.TransactionMapper;
import bank.repository.UserRepository;
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
    public void clearBankAccountTransactions(Long bankAccountId) {
        if (!userRepository.deleteTransactionsByBankAccountId(bankAccountId)) {
            throw new EntityNotFoundException(
                    "Either Bank Account with ID [%d] doesn't exists or there is no any transaction for that account"
            );
        }
    }

}