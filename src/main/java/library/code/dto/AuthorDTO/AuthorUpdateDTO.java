package library.code.dto.AuthorDTO;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class AuthorUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;
}
