package library.code.dto.specificationDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookParamDTO {
    private String bookCont;

    private String authorFirstNameCont;

    private String authorSurnameCont;

    private String publisherTitleCont;

    private Set<String> genreTypes;

    private String directionOfLiterature;
}
