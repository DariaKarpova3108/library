package library.code.dto.bookDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BookUpdateDTO {

    @JsonProperty("book_title")
    private JsonNullable<String> bookTitle;

    @JsonProperty("author_id")
    private JsonNullable<Long> authorId;

    @JsonProperty("publisher_id")
    private JsonNullable<Long> publisherId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("published_date")
    private JsonNullable<LocalDate> publishedDate;

    @JsonProperty("genre_types")
    private JsonNullable<Set<String>> genreTypes;

    @JsonProperty("ISBN")
    private JsonNullable<String> isbn;

    @JsonProperty("direction_of_literature")
    private JsonNullable<String> directionOfLiterature;
}
