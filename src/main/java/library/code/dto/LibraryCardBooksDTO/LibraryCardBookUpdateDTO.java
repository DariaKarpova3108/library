package library.code.dto.LibraryCardBooksDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class LibraryCardBookUpdateDTO {
    @JsonProperty("book_id")
    private JsonNullable<Long> bookId;

    @JsonProperty("library_card_number")
    private JsonNullable<String> libraryCardNumber;

    @JsonProperty("borrow_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private JsonNullable<LocalDate> borrowDate;

    @JsonProperty("expected_return_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private JsonNullable<LocalDate> expectedReturn;

    @JsonProperty("actual_return_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private JsonNullable<LocalDate> actualDate;

}
