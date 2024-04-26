package bank.repository;

import bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findBankAccountByUserId(Long userId);

}
