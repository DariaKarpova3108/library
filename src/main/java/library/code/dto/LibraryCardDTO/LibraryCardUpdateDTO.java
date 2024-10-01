package library.code.dto.LibraryCardDTO;

import jakarta.validation.constraints.NotNull;
import library.code.models.Book;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class LibraryCardUpdateDTO {
    @NotNull
    private JsonNullable<Book> book;

    @NotNull
    private JsonNullable<LocalDate> borrowDate;
    @NotNull
    private JsonNullable<LocalDate> expectedReturn;

    @NotNull
    private JsonNullable<String> cardNumber;
}
