package library.code.dto.genreDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreCreateDTO {

    @JsonProperty("type_of_genre")
    @NotNull
    private String typeOfGenre;
}
