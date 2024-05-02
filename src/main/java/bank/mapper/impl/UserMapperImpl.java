package bank.mapper.impl;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.mapper.UserMapper;
import bank.model.BankAccountRef;
import bank.model.Role;
import bank.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    private final TypeMap<UserRequestDto, User> requestToUserMapper;
    private final TypeMap<User, UserResponseDto> userToResponseMapper;

    public UserMapperImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder) {

        Converter<String, String> passwordConverter = converter -> passwordEncoder.encode(converter.getSource());
        this.requestToUserMapper = modelMapper.createTypeMap(UserRequestDto.class, User.class)
                .setProvider(p -> User.builder().role(Role.ROLE_USER).build())
                .addMappings(
                        mapper -> mapper.using(passwordConverter).map(UserRequestDto::getPassword, User::setPassword)
                );

        Converter<Set<BankAccountRef>, Set<Long>> listConverter =
                converter -> converter.getSource().stream()
                        .map(bankAccountRef -> bankAccountRef.getBankAccount().getId())
                        .collect(Collectors.toSet());
        this.userToResponseMapper = modelMapper.createTypeMap(User.class, UserResponseDto.class)
                .addMappings(mapper -> mapper.map(User::getRole, UserResponseDto::setRoleString))
                .addMappings(mapper -> mapper.using(listConverter).map(User::getBankAccounts, UserResponseDto::setBankAccountsId));

    }

    @Override
    public User mapToEntity(UserRequestDto userRequestDto) {
        return requestToUserMapper.map(userRequestDto);
    }

    @Override
    public UserResponseDto mapToResponseDto(User user) {
        return userToResponseMapper.map(user);
    }

}
