package library.code.dto.ReaderDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ReaderDTO {
    private Long id;

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

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
