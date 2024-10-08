package library.code.util;

import jakarta.annotation.PostConstruct;
import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.LibraryCard;
import library.code.models.LibraryCardBooks;
import library.code.models.Publisher;
import library.code.models.Reader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Getter
public class ModelGenerator {

    private final Faker faker;

    private Model<Reader> readerModel;
    private Model<LibraryCard> libraryCardModel;
    private Model<Author> authorModel;
    private Model<Publisher> publisherModel;
    private Model<Genre> genreModel;
    private Model<Book> bookModel;
    private Model<LibraryCardBooks> libraryCardBooksModel;

    @PostConstruct
    public void generateModel() {
        readerModel = Instancio.of(Reader.class)
                .ignore(Select.field(Reader::getId))
                .ignore(Select.field(Reader::getLibraryCard))
                .supply(Select.field(Reader::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(Reader::getLastName), () -> faker.name().lastName())
                .supply(Select.field(Reader::getAge), () -> faker.number().numberBetween(10, 100))
                .supply(Select.field(Reader::getPassportDetails), () -> faker.numerify("##########"))
                .supply(Select.field(Reader::getPhone), () -> faker.numerify("###########"))
                .supply(Select.field(Reader::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(Reader::getAddress), () -> faker.address().fullAddress())
                .toModel();

        libraryCardModel = Instancio.of(LibraryCard.class)
                .ignore(Select.field(LibraryCard::getId))
                .ignore(Select.field(LibraryCard::getReader))
                .ignore(Select.field(LibraryCard::getBorrowedBooks))
                .supply(Select.field(LibraryCard::getCardNumber), () -> faker.numerify("#############"))
                .toModel();

        authorModel = Instancio.of(Author.class)
                .ignore(Select.field(Author::getId))
                .supply(Select.field(Author::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(Author::getLastName), () -> faker.name().lastName())
                .ignore(Select.field(Author::getBooks))
                .toModel();

        publisherModel = Instancio.of(Publisher.class)
                .ignore((Select.field(Publisher::getId)))
                .supply(Select.field(Publisher::getTitle), () -> faker.name().title())
                .supply(Select.field(Publisher::getAddress), () -> faker.address().fullAddress())
                .supply(Select.field(Publisher::getPhone), () -> faker.numerify("###########"))
                .ignore(Select.field(Publisher::getBooks))
                .toModel();

        genreModel = Instancio.of(Genre.class)
                .ignore(Select.field(Genre::getId))
                .supply(Select.field(Genre::getTypeOfGenre), () -> "Adventures")
                .ignore(Select.field(Genre::getBooks))
                .toModel();

        bookModel = Instancio.of(Book.class)
                .ignore(Select.field(Book::getId))
                .supply(Select.field(Book::getBookTitle), () -> faker.name().title())
                .ignore(Select.field(Book::getAuthor))
                .ignore(Select.field(Book::getPublisher))
                .supply(Select.field(Book::getPublishedDate), () -> faker.date().past(10000, TimeUnit.DAYS)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .ignore(Select.field(Book::getGenres))
                .supply(Select.field(Book::getIsbn), () -> faker.numerify("#############"))
                .supply(Select.field(Book::getDirectionOfLiterature), () -> "Foreign literature")
                .toModel();

        libraryCardBooksModel = Instancio.of(LibraryCardBooks.class)
                .ignore(Select.field(LibraryCardBooks::getId))
                .ignore(Select.field(LibraryCardBooks::getBook))
                .ignore(Select.field(LibraryCardBooks::getLibraryCard))
                .supply(Select.field(LibraryCardBooks::getBorrowDate), () -> LocalDate.now())
                .supply(Select.field(LibraryCardBooks::getExpectedReturn), () -> LocalDate.now().plusWeeks(3))
                .supply(Select.field(LibraryCardBooks::getActualDate), () -> null)
                .toModel();
    }
}
