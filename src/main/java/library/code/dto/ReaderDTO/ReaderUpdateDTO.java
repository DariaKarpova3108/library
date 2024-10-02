package library.code.dto.ReaderDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
@Getter
@Setter
public class ReaderUpdateDTO {
    @NotNull
    @JsonProperty("first_name")
    private JsonNullable<String> firstName;

    @NotNull
    @JsonProperty("last_name")
    private JsonNullable<String> lastName;

    @NotNull
    @JsonProperty("passport_details")
    private JsonNullable<String> passportDetails;

    private JsonNullable<Integer> age;

    @NotNull
    private JsonNullable<String> phone;

    @NotNull
    private JsonNullable<String> email;

    private JsonNullable<String> address;
}
