package bank.controllers;

import bank.dto.user.UserRequestDto;
import bank.mapper.UserMapper;
import bank.model.User;
import bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userMapper.mapToResponseDto(user));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user) {
        userService.delete(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,
                                    @RequestBody @Validated UserRequestDto userRequestDto) {

        return ResponseEntity.ok().body(userService.update(userRequestDto, user.getId()));

    }

}