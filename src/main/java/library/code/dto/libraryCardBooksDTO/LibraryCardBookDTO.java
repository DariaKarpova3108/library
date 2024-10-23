package library.code.dto.libraryCardBooksDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LibraryCardBookDTO {
    private Long id;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("library_card_number")
    private String libraryCardNumber;

    @JsonProperty("borrow_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate borrowDate;

    @JsonProperty("expected_return_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate expectedReturn;

    @JsonProperty("actual_return_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate actualDate;
}
