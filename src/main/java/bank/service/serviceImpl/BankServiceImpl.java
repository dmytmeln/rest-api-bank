//package bank.service.serviceImpl;
//
//import bank.model.BankAccount;
//import bank.model.Transaction;
//import bank.dto.TransactionForm;
//import bank.service.BankService;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class BankServiceImpl implements BankService {
//
//    private final AccountRepository accountRepo;
//    private final ModelMapper modelMapper;
//
//    @Override
//    @Transactional
//    public BankAccount findById(Long accountId) {
//        return accountRepo.findById(accountId).orElseThrow(
//                () -> new EntityNotFoundException(
//                        "Account with id [%d] not found!".formatted(accountId)
//                )
//        );
//    }
//
//    @Override
//    @Transactional
//    public BankAccount makeDeposit(Long accountId, TransactionForm transaction) {
//        BankAccount bankAccount = findById(accountId);
//        bankAccount.setBalance(bankAccount.getBalance() + transaction.getMoneyAmount());
//        if (transaction.getType().isBlank()) {
//            transaction.setType("Deposit");
//        }
//        update(bankAccount, transaction);
//
//        return bankAccount;
//    }
//
//    @Override
//    @Transactional
//    public BankAccount makeWithdrawal(Long accountId, TransactionForm transaction) {
//        BankAccount bankAccount = findById(accountId);
//
//        double balance = bankAccount.getBalance();
//        Double moneyAmount = transaction.getMoneyAmount();
//        double result = balance - moneyAmount;
//        if (result < 0) {
//            throw new IllegalArgumentException("You don't have enough money to withdraw " + moneyAmount);
//        }
//
//        bankAccount.setBalance(result);
//        if (transaction.getType().isBlank()) {
//            transaction.setType("Withdrawal");
//        }
//        return update(bankAccount, transaction);
//    }
//
//    private BankAccount update(BankAccount bankAccount, TransactionForm transactionForm) {
//        Transaction transaction = modelMapper.map(transactionForm, Transaction.class);
//        transaction.setBankAccount(bankAccount);
//        if (transaction.getMsg().isBlank()) {
//            transaction.setMsg("Standard Transaction Message");
//        }
//
//        bankAccount.getTransactions().add(transaction);
//        return accountRepo.save(bankAccount);
//    }
//
//    @Override
//    @Transactional
//    public BankAccount findBankAccountByUserId(Long userId) {
//        return accountRepo.findBankAccountByUserId(userId).orElseThrow(
//                () -> new EntityNotFoundException(
//                        "Bank Account with user id [%d] not found!".formatted(userId)
//                )
//        );
//    }
//
//}