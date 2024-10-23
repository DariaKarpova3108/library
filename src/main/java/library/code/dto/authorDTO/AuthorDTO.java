package library.code.dto.authorDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import library.code.dto.bookDTO.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorDTO {

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("books_written_by_the_author")
    private List<BookDTO> bookList;
}
