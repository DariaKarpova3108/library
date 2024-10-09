package library.code.dto.LibraryCardBooksDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LibraryCardBookCreateDTO {
    @NotNull
    @JsonProperty("book_id")
    private Long bookId;

    @NotNull
    @JsonProperty("library_card_number")
    private String libraryCardNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    @JsonProperty("expected_return_date")
    private LocalDate expectedReturn;
}
