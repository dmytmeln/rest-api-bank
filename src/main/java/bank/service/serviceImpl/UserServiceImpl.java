package bank.service.serviceImpl;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.exception.EntityAlreadyExistsException;
import bank.exception.EntityNotFoundException;
import bank.mapper.UserMapper;
import bank.model.BankAccount;
import bank.model.User;
import bank.repository.UserRepository;
import bank.service.UserService;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public User findById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with ID [%d] not found!".formatted(userId))
                );
    }

    @Override
    public UserResponseDto findResponseById(Long userId) {
        return userMapper.mapToResponseDto(findById(userId));
    }

    @Override
    public boolean existsByEmailOrPhoneNumber(UserRequestDto userRequestDto) {
        return userRepo.existsByEmailOrPhoneNumber(userRequestDto.getEmail(), userRequestDto.getPhoneNumber());
    }

    @Override
    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {

        if (existsByEmailOrPhoneNumber(userRequestDto)) {
            throw new EntityAlreadyExistsException(
                    "User with email [%s] and/or phone number [%s] already exists!"
                            .formatted(userRequestDto.getEmail(), userRequestDto.getPhoneNumber())
            );
        }

        User user = userMapper.mapToEntity(userRequestDto);
        user.addBankAccount(BankAccount.builder().balance(0D).build()); // automatically create bank account for user

        User savedUser = userRepo.save(user);
        return userMapper.mapToResponseDto(savedUser);
    }

    @Override
    public boolean alreadyExists(UserRequestDto userRequestDto, Long userId) {

        User userDB = findById(userId);

        String email = userRequestDto.getEmail();
        String phoneNumber = userRequestDto.getPhoneNumber();

        // Check whether email and phone number are equal to old ones. If so, it means they haven't been changed.
        // If something changed, we have to ensure that there's no user with such info
        boolean sameEmail = Objects.equals(userDB.getEmail(), email);
        boolean samePhoneNumber = Objects.equals(userDB.getPhoneNumber(), phoneNumber);
        // if user changed both email and phone number -> check whether user with such info already exists
        if (!sameEmail && !samePhoneNumber) {

            return userRepo.existsByEmailOrPhoneNumber(email, phoneNumber);
        } else if (!sameEmail) { // if userForm change only email -> check whether user with such email already exists

            return userRepo.existsByEmail(email);
        } else if (!samePhoneNumber) { //if userForm change only phone number -> check whether user with such phone number already exists

            return userRepo.existsByPhoneNumber(phoneNumber);
        }

        return false;

    }

    @Override
    @Transactional
    public UserResponseDto update(UserRequestDto userRequestDto, Long userId) {

        if (alreadyExists(userRequestDto, userId)) {
            throw new EntityAlreadyExistsException(
                    "User with email [%s] and/or phone number [%s] already exists!"
                            .formatted(userRequestDto.getEmail(), userRequestDto.getPhoneNumber())
            );
        }

        User user = userMapper.mapToEntity(userRequestDto);
        user.setId(userId);
        userRepo.updateWithoutBankAccount(user);

        return userMapper.mapToResponseDto(findById(userId));
    }

    @Override
    @Transactional
    public UserResponseDto patchUpdate(UserRequestDto userRequestDto, Long userId) {

        User user = findById(userId);
        String firstname = userRequestDto.getFirstname(),
                lastname = userRequestDto.getLastname(),
                email = userRequestDto.getEmail(),
                password = userRequestDto.getPassword(),
                phoneNumber = userRequestDto.getPhoneNumber();

        int expectedErrors = 5;

        if (email != null) {
            if (!email.equals(user.getEmail()) && userRepo.existsByEmail(email)) {
                throw new EntityAlreadyExistsException(
                        "User with email [%s] already exists!".formatted(email)
                );
            }

            expectedErrors--;
            user.setEmail(email);
        }

        if (phoneNumber != null) {
            if (!phoneNumber.equals(user.getPhoneNumber()) && userRepo.existsByPhoneNumber(phoneNumber)) {
                throw new EntityAlreadyExistsException(
                        "User with email [%s] and/or phone number [%s] already exists!"
                                .formatted(userRequestDto.getEmail(), userRequestDto.getPhoneNumber())
                );
            }

            expectedErrors--;
            user.setPhoneNumber(phoneNumber);
        }

        if (firstname != null) {
            expectedErrors--;
            user.setFirstname(firstname);
        }

        if (lastname != null) {
            expectedErrors--;
            user.setLastname(lastname);
        }

        if (password != null) {
            expectedErrors--;
            user.setPassword(passwordEncoder.encode(password));
        }

        // for example, if 2 fields was changed -> 3 errors are expected (because of fields that are unset)
        var constraintViolations = validator.validate(userRequestDto);
        if (constraintViolations.size() != expectedErrors) {
            throw new IllegalArgumentException("Constraint violations for given fields");
        }

        userRepo.updateWithoutBankAccount(user);

        return userMapper.mapToResponseDto(user);

    }

    @Override
    public void delete(long userId) {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException(
                    "User with  userId [%d] not found".formatted(userId)
            );
        }

        userRepo.deleteById(userId);
    }

    @Override
    public boolean hasBankAccount(User user, Long bankAccountId) {
        return user.getBankAccounts().stream()
                .anyMatch(bankAccount -> Objects.equals(bankAccountId, bankAccount.getId()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username [%s] not found!".formatted(username)
                ));
    }
}