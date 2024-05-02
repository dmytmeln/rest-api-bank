package bank.repository;

import bank.model.BankAccount;
import bank.model.Transaction;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankRepository extends ListCrudRepository<BankAccount, Long> {

    @Query("SELECT t.* FROM bank_accounts ba JOIN transactions t ON ba.id = t.bank_accounts WHERE t.bank_accounts = :bankAccountId")
    List<Transaction> findTransactionsByBankAccountId(@Param("bankAccountId") Long bankAccountId);

    @Modifying
    @Query("DELETE FROM transactions t WHERE t.bank_accounts = :bankAccount")
    void deleteTransactionsByBankAccountId(@Param("bankAccount") Long bankAccount);

    @Modifying
    @Query("UPDATE bank_accounts ba SET balance = :balance WHERE ba.id = :bankAccountId")
    void updateWithoutTransactions(@Param("balance") Double balance,
                                          @Param("bankAccountId") Long bankAccountId);

//    BankAccount

}