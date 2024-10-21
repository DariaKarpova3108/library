package library.code.dto.userDTO;

import library.code.dto.roleDTO.RoleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String email;

    private RoleDTO role;
}
