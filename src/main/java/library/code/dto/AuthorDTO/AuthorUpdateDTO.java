package library.code.dto.AuthorDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class AuthorUpdateDTO {

    @JsonProperty("first_name")
    private JsonNullable<String> firstName;

    @JsonProperty("last_name")
    private JsonNullable<String> lastName;
}
