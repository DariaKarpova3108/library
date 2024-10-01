package library.code.dto.LibraryCardDTO;

import jakarta.validation.constraints.NotNull;
import library.code.models.Book;
import library.code.models.Reader;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class LibraryCardCreateDTO {
    @NotNull
    private Book book;

    @NotNull
    private Reader reader;

    @NotNull
    private LocalDate borrowDate;

    @NotNull
    private LocalDate expectedReturn;
    @NotNull
    private String cardNumber;
}
