package library.code.dto.AuthorDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreateDTO {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
