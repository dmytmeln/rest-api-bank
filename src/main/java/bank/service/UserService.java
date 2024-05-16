package bank.service;

import bank.dto.user.UserResponseDto;
import bank.model.User;
import bank.dto.user.UserRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends UserDetailsService {

    @Transactional
    UserResponseDto patchUpdate(UserRequestDto userRequestDto, Long userId);

    void delete(long userId);

    User findById(Long userId);

    UserResponseDto findResponseById(Long userId);

    UserResponseDto signup(UserRequestDto user);

    UserResponseDto update(UserRequestDto user, Long userId);

    boolean alreadyExists(UserRequestDto user, Long userId);

    boolean existsByEmailOrPhoneNumber(UserRequestDto userRequestDto);

    boolean hasBankAccount(User user, Long bankAccountId);

}
