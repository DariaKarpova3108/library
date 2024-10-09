package library.code.mapper;

import library.code.dto.LibraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.models.Book;
import library.code.models.LibraryCard;
import library.code.models.LibraryCardBooks;
import library.code.repositories.BookRepository;
import library.code.repositories.LibraryCardRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class LibraryCardBookMapper {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @Mapping(target = "book", source = "bookId", qualifiedByName = "findBook")
    @Mapping(target = "libraryCard", source = "libraryCardNumber", qualifiedByName = "cardNumberToModel")
    public abstract LibraryCardBooks map(LibraryCardBookCreateDTO createDTO);

    @Mapping(target = "libraryCardNumber", source = "libraryCard.cardNumber")
    @Mapping(target = "bookTitle", source = "book.bookTitle")
    public abstract LibraryCardBookDTO map(LibraryCardBooks libraryCardBooks);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "findBook")
    @Mapping(target = "libraryCard", source = "libraryCardNumber")
    public abstract void update(LibraryCardBookUpdateDTO updateDTO, @MappingTarget LibraryCardBooks libraryCardBooks);

    @Named("cardNumberToModel")
    public LibraryCard findModelByCardNumber(String cardNumber) {
        return libraryCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with number: "
                        + cardNumber + " not found"));
    }

    @Named("findBook")
    public Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found"));
    }
}
