package library.code.dto.userDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotNull
    private String password;

    @NotNull
    private String email;
}
