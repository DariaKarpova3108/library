package library.code.dto.BookDTO;

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

    private JsonNullable<String> bookTitle;

    @JsonProperty("author_name")
    private JsonNullable<String> authorFullName;

    @JsonProperty("publisher")
    private JsonNullable<String> publisherTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private JsonNullable<LocalDate> publishedDate;

    @JsonProperty("genre_types")
    private JsonNullable<Set<String>> genreTypes;

    private JsonNullable<String> ISBN;
    private JsonNullable<String> directionOfLiterature;
}
