package library.code.dto.BookDTO;

import jakarta.validation.constraints.NotNull;
import library.code.models.Author;
import library.code.models.Genre;
import library.code.models.Publisher;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
@Getter
@Setter
public class BookUpdateDTO {
    @NotNull
    private JsonNullable<String> bookTitle;

    @NotNull
    private JsonNullable<Author> author;

    @NotNull
    private JsonNullable<Publisher> publisher;

    private JsonNullable<LocalDate> publishedDate;

    @NotNull
    private JsonNullable<Genre> genre;
    @NotNull
    private JsonNullable<String> ISBN;
}
