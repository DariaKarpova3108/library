package library.code.dto.publisherDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import library.code.dto.BookDTO.BookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublisherDTO {
    private Long id;
    private String title;
    private String address;
    private String phone;

    @JsonProperty("books_from_the_same_publisher")
    private List<BookDTO> booksFromSamePublisher;
}
