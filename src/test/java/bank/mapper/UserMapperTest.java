package bank.mapper;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.mapper.impl.UserMapperImpl;
import bank.model.BankAccountRef;
import bank.model.Role;
import bank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

    private UserMapperImpl userMapper;

    @BeforeEach
    public void init() {
        this.userMapper = new UserMapperImpl(modelMapper, passwordEncoder);
    }

    @Test
    void testMapFromUserFormToUser() {
        String hashedPassword = "1234";
        String password = "12!@asAS";
        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .password(password)
                .email("john.doe@example.com")
                .phoneNumber("123123123123")
                .build();
        User mappedUser = userMapper.mapToEntity(userRequestDto);

        assertEquals(userRequestDto.getEmail(), mappedUser.getEmail());
        assertEquals(userRequestDto.getFirstname(), mappedUser.getFirstname());
        assertEquals(userRequestDto.getLastname(), mappedUser.getLastname());
        assertEquals(hashedPassword, mappedUser.getPassword());
        assertEquals(Role.ROLE_USER.name(), mappedUser.getRole().name());
        assertEquals(userRequestDto.getPhoneNumber(), mappedUser.getPhoneNumber());

    }

    @Test
    void testMapUserToResponse() {
        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .password("12!@asAS")
                .email("john.doe@example.com")
                .phoneNumber("123123123123")
                .role(Role.ROLE_USER)
                .bankAccounts(Set.of(new BankAccountRef(() -> 1L)))
                .build();
        UserResponseDto mapped = userMapper.mapToResponseDto(user);

        assertEquals(user.getEmail(), mapped.getEmail());
        assertEquals(user.getFirstname(), mapped.getFirstname());
        assertEquals(user.getLastname(), mapped.getLastname());
        assertEquals(user.getPassword(), mapped.getPassword());
        assertEquals(user.getPhoneNumber(), mapped.getPhoneNumber());
        assertEquals(1L, mapped.getBankAccountsId().iterator().next());
    }



}
