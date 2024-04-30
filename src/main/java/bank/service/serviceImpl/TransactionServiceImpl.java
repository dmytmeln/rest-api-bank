//package bank.service.serviceImpl;
//
//import bank.model.BankAccount;
//import bank.model.Transaction;
//import bank.service.BankService;
//import bank.service.TransactionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionServiceImpl implements TransactionService {
//
//    private final TransactionRepository transactionRepo;
//    private final BankService bankService;
//
//    @Override
//    public List<Transaction> getBankAccountTransactions(Long bankAccountId) {
//        bankService.findById(bankAccountId);
//        return transactionRepo.findTransactionsByBankAccountId(bankAccountId);
//    }
//
//    @Override
//    public List<Transaction> getBankAccountTransactionsByUserId(Long userId) {
//        BankAccount bankAccount = bankService.findBankAccountByUserId(userId);
//        return getBankAccountTransactions(bankAccount.getId());
//    }
//
//}