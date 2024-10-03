package library.code.dto.publisherDTO;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PublisherUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> address;
    private JsonNullable<String> phone;
}
