package library.code.dto.ReaderDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderCreateDTO {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    @NotNull
    private String passportDetails;


    private int age;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    private String address;
}
