package library.code.dto.libraryCardDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LibraryCardUpdateDTO {

    @JsonProperty("reader_id")
    private JsonNullable<Long> readerId;

    @JsonProperty("library_card_number")
    private JsonNullable<String> cardNumber;
}
