package library.code.dto.readerDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class ReaderUpdateDTO {
    @JsonProperty("first_name")
    private JsonNullable<String> firstName;

    @JsonProperty("last_name")
    private JsonNullable<String> lastName;

    @JsonProperty("passport_details")
    private JsonNullable<String> passportDetails;

    private JsonNullable<Integer> age;

    private JsonNullable<String> phone;

    private JsonNullable<String> address;
}
