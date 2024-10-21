package library.code.dto.administatorDTO;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class AdminUpdateDTO {
    private JsonNullable<String> jobTitle;

    private JsonNullable<String> phone;
}
