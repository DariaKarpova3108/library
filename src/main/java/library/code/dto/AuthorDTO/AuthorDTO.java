package library.code.dto.AuthorDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
    @NotNull
    private Long id;
    @NotNull
    private String fullName;
}
