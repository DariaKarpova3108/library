package library.code.dto.publisherDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherCreateDTO {
    @NotNull
    private String title;

    @NotNull
    private String address;

    @Pattern(regexp = "\\+[0-9]{11}")
    private String phone;
}
