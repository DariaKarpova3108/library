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
    private String bookTitle;

    @JsonProperty("author_name")
    private String authorFullName;

    @JsonProperty("publisher")
    private String publisherTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate publishedDate;

    @JsonProperty("genre_types")
    private Set<String> genreTypes;
    private String ISBN;
    private String directionOfLiterature;
}
