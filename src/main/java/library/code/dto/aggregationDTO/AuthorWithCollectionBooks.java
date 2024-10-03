package library.code.dto.aggregationDTO;

import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.BookDTO.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorWithCollectionBooks { //добавить маппер емли потребуется этот дто класс
    private AuthorDTO author;
    private List<BookDTO> books; //проверить нужно ли добавлять по этому полю преобразование в маппер
}
