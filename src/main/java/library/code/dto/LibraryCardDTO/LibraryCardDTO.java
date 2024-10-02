package library.code.dto.LibraryCardDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LibraryCardDTO {

    private Long id;

    @JsonProperty("reader_name")
    private String readerFullName;

    @JsonProperty("library_card_number")
    private String cardNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
}
