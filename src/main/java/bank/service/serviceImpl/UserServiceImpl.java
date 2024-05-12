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
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;

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
    public void existsByEmailOrPhoneNumber(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail(),
                phoneNumber = userRequestDto.getPhoneNumber();
        boolean existsByEmailOrPhoneNumber = userRepo.existsByEmailOrPhoneNumber(email, phoneNumber);
        if (existsByEmailOrPhoneNumber) {
            throw new EntityAlreadyExistsException(
                    "User with email [%s] and phone number [%s] already exists!".formatted(email, phoneNumber)
            );
        }
    }

    @Override
    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        existsByEmailOrPhoneNumber(userRequestDto);

        User user = userMapper.mapToEntity(userRequestDto);
        user.addBankAccount(BankAccount.builder().balance(0D).build()); // automatically create bank account for user

        User savedUser = userRepo.save(user);
        return userMapper.mapToResponseDto(savedUser);
    }

    @Override
    public void alreadyExists(UserRequestDto userRequestDto, Long userId) {

        User userDB = findById(userId);

        String email = userRequestDto.getEmail();
        String phoneNumber = userRequestDto.getPhoneNumber();

        // Check whether email and phone number are equal to old ones. If so, it means they haven't been changed.
        // If something changed, we have to ensure that there's no user with such info
        boolean sameEmail = Objects.equals(userDB.getEmail(), email);
        boolean samePhoneNumber = Objects.equals(userDB.getPhoneNumber(), phoneNumber);
        // if user changed both email and phone number -> check whether user with such info already exists
        if (!sameEmail && !samePhoneNumber) {

            if (userRepo.existsByEmailAndPhoneNumber(email, phoneNumber)) {
                throw new EntityAlreadyExistsException(
                        "User with email [%s] and phone number [%s] already exists!".formatted(email, phoneNumber)
                );
            }

        } else if (!sameEmail) { // if userForm change only email -> check whether user with such email already exists

            if (userRepo.existsByEmail(email)) {
                throw new EntityAlreadyExistsException(
                        "User with email [%s] already exists!".formatted(email)
                );
            }

        } else if (!samePhoneNumber) { //if userForm change only phone number -> check whether user with such phone number already exists

            if (userRepo.existsByPhoneNumber(phoneNumber)) {
                throw new EntityAlreadyExistsException(
                        "User with phone number [%s] already exists!".formatted(phoneNumber)
                );
            }

        }

    }

    @Override
    @Transactional
    public UserResponseDto update(UserRequestDto userRequestDto, Long userId) {
        alreadyExists(userRequestDto, userId);

        User user = userMapper.mapToEntity(userRequestDto);
        user.setId(userId);
        userRepo.updateWithoutBankAccount(user);

        return userMapper.mapToResponseDto(findById(userId));
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username [%s] not found!".formatted(username)
                ));
    }
}