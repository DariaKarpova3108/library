package library.code.dto.genreDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class GenreUpdateDTO {
    @JsonProperty("type_of_genre")
    private JsonNullable<String> typeOfGenre;
}
