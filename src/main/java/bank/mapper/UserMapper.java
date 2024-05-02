package bank.mapper;

import bank.dto.user.UserRequestDto;
import bank.dto.user.UserResponseDto;
import bank.model.User;

public interface UserMapper {

    User mapToEntity(UserRequestDto userRequestDto);

    UserResponseDto mapToResponseDto(User user);

}
