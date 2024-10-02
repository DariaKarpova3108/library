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
    private String bookTitle;

    @NotNull
    @JsonProperty("author_name")
    private String authorFullName;

    @NotNull
    @JsonProperty("publisher")
    private String publisherTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate publishedDate;

    @NotNull
    @JsonProperty("genre_types")
    private Set<String> genreTypes;

    @NotNull
    private String ISBN;

    private String directionOfLiterature;
}
