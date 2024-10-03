package library.code.dto.LibraryCardDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class LibraryCardDTO {

    private Long id;

    @JsonProperty("reader_first_name")
    private String readerFirstName;

    @JsonProperty("reader_surname")
    private String readerSurname;

    @JsonProperty("library_card_number")
    private String cardNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @JsonProperty("borrowed_books")
    private Set<LibraryCardBookDTO> borrowedBooks; //ДОБАВИЛА В МАППЕР - ПРОВЕРИТЬ РАБОТУ) проверить нужно ли добавлять по этому полю преобразование в маппер
}
