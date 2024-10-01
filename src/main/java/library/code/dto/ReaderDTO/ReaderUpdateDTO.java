package library.code.dto.ReaderDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
@Getter
@Setter
public class ReaderUpdateDTO {
    @NotNull
    private JsonNullable<String> firstName;

    @NotNull
    private JsonNullable<String> lastName;

    @NotNull
    private JsonNullable<String> passportDetails;

    private JsonNullable<Integer> age;

    @NotNull
    private JsonNullable<String> phone;

    @NotNull
    private JsonNullable<String> email;

    private JsonNullable<String> address;
}
