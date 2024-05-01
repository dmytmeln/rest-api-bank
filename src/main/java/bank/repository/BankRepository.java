package bank.repository;

import bank.model.BankAccount;
import bank.model.Transaction;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BankRepository extends ListCrudRepository<BankAccount, Long> {

    @Query("SELECT t.* FROM bank_accounts ba JOIN transactions t ON ba.id = t.bank_accounts")
    List<Transaction> findTransactionsByBankAccountId(Long bankAccountId);

}