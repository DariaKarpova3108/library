package library.code.dto.BookDTO;

import jakarta.validation.constraints.NotNull;
import library.code.models.Author;
import library.code.models.Genre;
import library.code.models.Publisher;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class BookDTO {
    private Long id;

    @NotNull
    private String bookTitle;

    @NotNull
    private Author author;

    @NotNull
    private Publisher publisher;

    private LocalDate publishedDate;

    @NotNull
    private Genre genre;
    @NotNull
    private String ISBN;
}
