package library.code.dto.BookDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BookDTO {
    private Long id;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("author_first_name")
    private String authorFirstName;

    @JsonProperty("author_surname")
    private String authorSurname;

    @JsonProperty("publisher")
    private String publisherTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("published_date")
    private LocalDate publishedDate;

    @JsonProperty("genre_types")
    private Set<String> genreTypes;

    private String ISBN;

    @JsonProperty("direction_of_literature")
    private String directionOfLiterature;
}
