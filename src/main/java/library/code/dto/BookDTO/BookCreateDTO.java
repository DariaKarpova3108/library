package library.code.dto.BookDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BookCreateDTO {
    @NotNull
    @JsonProperty("book_title")
    private String bookTitle;

    @NotNull
    @JsonProperty("author_id")
    private Long authorId;

    @NotNull
    @JsonProperty("publisher_id")
    private Long publisherId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("published_date")
    private LocalDate publishedDate;

    @NotNull
    @JsonProperty("genre_types")
    private Set<String> genreTypes;

    @NotNull
    @JsonProperty("ISBN")
    private String isbn;

    @JsonProperty("direction_of_literature")
    private String directionOfLiterature;
}
