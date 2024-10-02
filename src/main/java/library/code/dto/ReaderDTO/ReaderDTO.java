package library.code.dto.ReaderDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ReaderDTO {
    private Long id;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updatedAt;
}
