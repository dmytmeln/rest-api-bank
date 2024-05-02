package bank.dto.user;

import bank.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;
    private Set<Long> bankAccountsId;

    public void setRoleString(Role role) {
        this.role = role.name();
    }


}
