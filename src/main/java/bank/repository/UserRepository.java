package bank.repository;

import bank.model.BankAccount;
import bank.model.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends ListCrudRepository<User, Long>, BankRepository {

    Optional<User> findUserByEmailAndPhoneNumberAndPassword(String email, String phoneNumber, String password);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query( value =
            """
            UPDATE users u SET firstname = :#{#user.firstname}, lastname = :#{#user.lastname}, email = :#{#user.email},\s
                    password = :#{#user.password}, phone_number = :#{#user.phoneNumber}\s
            WHERE u.id = :#{#user.id};
            """
    )
    boolean updateWithoutBankAccount(User user);

    @Query("SELECT ba.* FROM bank_accounts ba WHERE ba.users = :userId")
    List<BankAccount> findBankAccountsByUserId(@Param("userId") Long userId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndPhoneNumber(String email, String phoneNumber);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

}
