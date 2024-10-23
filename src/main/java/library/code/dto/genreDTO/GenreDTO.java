package library.code.dto.genreDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import library.code.dto.bookDTO.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GenreDTO {
    private Long id;

    @JsonProperty("type_of_genre")
    private String typeOfGenre;

    @JsonProperty("books_of_the_same_genre")
    private Set<BookDTO> booksOfSameGenre;
}
