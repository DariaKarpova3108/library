package library.code.dto.AuthorDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
@Getter
@Setter
public class AuthorUpdateDTO {
    @NotNull
    private JsonNullable<String> fullName;
}
