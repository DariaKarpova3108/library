package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.LibraryCard;
import library.code.models.LibraryCardBooks;
import library.code.models.Publisher;
import library.code.models.Reader;
import library.code.repositories.AuthorRepository;
import library.code.repositories.BookRepository;
import library.code.repositories.GenreRepository;
import library.code.repositories.LibraryCardBooksRepository;
import library.code.repositories.LibraryCardRepository;
import library.code.repositories.PublisherRepository;
import library.code.repositories.ReaderRepository;
import library.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LibraryCardBooksControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private LibraryCardBooksRepository cardBooksRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryCardRepository libraryCardRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ReaderRepository readerRepository;
    private Book book;
    private LibraryCard libraryCard;
    private LibraryCardBooks libraryCardBooks;

    @BeforeEach
    public void setUp() {
        Publisher publisher = Instancio.of(modelGenerator.getPublisherModel()).create();
        publisherRepository.save(publisher);

        Genre genre = Instancio.of(modelGenerator.getGenreModel()).create();
        genreRepository.save(genre);

        Author author = Instancio.of(modelGenerator.getAuthorModel()).create();
        authorRepository.save(author);

        Reader reader = Instancio.of(modelGenerator.getReaderModel()).create();
        readerRepository.save(reader);

        libraryCard = Instancio.of(modelGenerator.getLibraryCardModel()).create();
        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);

        libraryCardBooks = Instancio.of(modelGenerator.getLibraryCardBooksModel()).create();

        book = Instancio.of(modelGenerator.getBookModel()).create();
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGenres(new HashSet<>(Set.of(genre)));
        bookRepository.save(book);

        libraryCardRepository.save(libraryCard);
        libraryCardBooks.setBook(book);
        libraryCardBooks.setLibraryCard(libraryCard);
        cardBooksRepository.save(libraryCardBooks);
    }

    @Test
    public void getListLibraryBooks() throws Exception {
        var request = get("/api/libraryCardBooks");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    public void getLibraryBooks() throws Exception {
        var request = get("/api/libraryCardBooks/" + libraryCardBooks.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("book_title").isEqualTo(libraryCardBooks.getBook().getBookTitle()))
                .and(n -> n.node("borrow_date")
                        .isEqualTo(libraryCardBooks.getBorrowDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    }

    @Test
    public void createLibraryBooks() throws Exception {
        var createDTO = new LibraryCardBookCreateDTO();
        createDTO.setBookId(book.getId());
        createDTO.setLibraryCardNumber(libraryCard.getCardNumber());
        createDTO.setBorrowDate(LocalDate.now());
        createDTO.setExpectedReturn(LocalDate.now().plusWeeks(3));
        var request = post("/api/libraryCardBooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        var libraryCardBookDTOResponse = mapper.readValue(responseBody, LibraryCardBookDTO.class);

        assertThat(libraryCardBookDTOResponse).isNotNull();
        assertThat(libraryCardBookDTOResponse.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(libraryCardBookDTOResponse.getLibraryCardNumber()).isEqualTo(libraryCard.getCardNumber());
        assertThat(libraryCardBookDTOResponse.getBorrowDate()).isEqualTo(createDTO.getBorrowDate());
        assertThat(libraryCardBookDTOResponse.getExpectedReturn()).isEqualTo(createDTO.getExpectedReturn());
    }

    @Test
    public void updateLibraryBooks() throws Exception {
        var updateDTO = new LibraryCardBookUpdateDTO();
        var expectedReturn = LocalDate.now().plusMonths(2);
        updateDTO.setExpectedReturn(JsonNullable.of(expectedReturn));

        var request = put("/api/libraryCardBooks/" + libraryCardBooks.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedLibraryCardBooks = cardBooksRepository.findById(libraryCardBooks.getId()).get();

        assertThat(updatedLibraryCardBooks).isNotNull();
        assertThat(updatedLibraryCardBooks.getExpectedReturn()).isEqualTo(expectedReturn);
        assertThat(updatedLibraryCardBooks.getBorrowDate()).isEqualTo(libraryCardBooks.getBorrowDate());
        assertThat(updatedLibraryCardBooks.getBook().getId()).isEqualTo(libraryCardBooks.getBook().getId());
        assertThat(updatedLibraryCardBooks.getLibraryCard().getCardNumber())
                .isEqualTo(libraryCardBooks.getLibraryCard().getCardNumber());
    }

    @Test
    public void deleteLibraryBooks() throws Exception {
        var request = delete("/api/libraryCardBooks/" + libraryCardBooks.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(cardBooksRepository.findById(libraryCardBooks.getId())).isEmpty();
    }
}
