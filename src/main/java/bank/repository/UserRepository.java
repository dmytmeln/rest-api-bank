package bank.repository;

import bank.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

    Optional<User> findUserByEmailAndPhoneNumberAndPassword(String email, String phoneNumber, String password);

    Optional<User> findByEmail(String email);

    @Query("SELECT u.* FROM users u JOIN user_bank_account uba on u.id = uba.users WHERE uba.bank_account = :bankAccountId")
    Optional<User> findByBankAccountId(@Param("bankAccountId") Long bankAccountId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndPhoneNumber(String email, String phoneNumber);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

}
