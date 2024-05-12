package bank.repository;

import bank.model.BankAccount;
import bank.model.Transaction;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository {

    @Query("SELECT ba.* FROM bank_accounts ba WHERE ba.users = :userId AND ba.id = :bankAccountId")
    Optional<BankAccount> findBankAccountByBankAndUserIds(@Param("userId") Long userId, @Param("bankAccountId") Long bankAccountId);

    @Query("SELECT t.* FROM bank_accounts ba JOIN transactions t ON ba.id = t.bank_accounts WHERE t.bank_accounts = :bankAccountId")
    List<Transaction> findTransactionsByBankAccountId(@Param("bankAccountId") Long bankAccountId);

    @Modifying
    @Query("DELETE FROM transactions t WHERE t.bank_accounts = :bankAccount")
    boolean deleteTransactionsByBankAccountId(@Param("bankAccount") Long bankAccount);

    @Modifying
    @Query("UPDATE bank_accounts ba SET balance = :#{#bankAccount.balance} WHERE ba.id = :#{#bankAccount.id}")
    boolean updateBankAccountWithoutTransactions(BankAccount bankAccount);

}