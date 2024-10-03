package library.code.dto.ReaderDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderCreateDTO {
    @NotNull
    @Size(max = 50)
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @Size(max = 50)
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("passport_details")
    private String passportDetails;

    private int age;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    private String address;
}