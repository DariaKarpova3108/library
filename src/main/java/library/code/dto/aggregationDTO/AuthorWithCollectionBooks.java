package library.code.dto.aggregationDTO;

import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.BookDTO.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorWithCollectionBooks {
    private AuthorDTO author;
    private List<BookDTO> books;
}
