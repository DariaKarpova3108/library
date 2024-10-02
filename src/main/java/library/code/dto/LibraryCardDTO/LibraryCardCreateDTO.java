package library.code.dto.LibraryCardDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryCardCreateDTO {
    @NotNull
    @JsonProperty("reader_name")
    private String readerFullName;

    @NotNull
    @JsonProperty("library_card_number")
    private String cardNumber;
}
