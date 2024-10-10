package library.code.controllers.api;

import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.Publisher;
import library.code.repositories.AuthorRepository;
import library.code.repositories.BookRepository;
import library.code.repositories.GenreRepository;
import library.code.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookControllerTestWithParams {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @BeforeEach
    public void setUp() {
        var author = new Author();
        author.setFirstName("Kathleen");
        author.setLastName("Rowling");
        authorRepository.save(author);

        var author2 = new Author();
        author2.setFirstName("Leo");
        author2.setLastName("Tolstoy");
        authorRepository.save(author2);

        var publisher = new Publisher();
        publisher.setTitle("Labyrinth");
        publisher.setAddress("Moscow, street Rad Street, h.1");
        publisher.setPhone("89112345698");
        publisherRepository.save(publisher);

        var publisher2 = new Publisher();
        publisher2.setTitle("AST");
        publisher2.setAddress("Moscow, street Lazareva, h.9");
        publisher2.setPhone("89765432112");
        publisherRepository.save(publisher2);

        var genre = new Genre();
        genre.setTypeOfGenre("Fantasy");
        genreRepository.save(genre);

        var genre2 = new Genre();
        genre2.setTypeOfGenre("Novel");
        genreRepository.save(genre2);

        var genre3 = new Genre();
        genre3.setTypeOfGenre("Horror");
        genreRepository.save(genre3);

        var book = new Book();
        book.setBookTitle("Harry Potter");
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGenres(new HashSet<>(Set.of(genre, genre3)));
        book.setIsbn("1111111111");
        book.setDirectionOfLiterature("Foreign literature");
        bookRepository.save(book);

        var book2 = new Book();
        book2.setBookTitle("War and Peace");
        book2.setAuthor(author2);
        book2.setPublisher(publisher2);
        book2.setGenres(new HashSet<>(Set.of(genre2)));
        book2.setIsbn("2222222222");
        book2.setDirectionOfLiterature("Domestic literature");

        var book3 = new Book();
        book3.setBookTitle("Harry Potter2");
        book3.setAuthor(author);
        book3.setPublisher(publisher);
        book3.setGenres(new HashSet<>(Set.of(genre, genre3)));
        book3.setIsbn("1122222278");
        book3.setDirectionOfLiterature("Foreign literature");

        bookRepository.save(book);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    @Test
    public void testFilterGetListBookWithBookTitleCont() throws Exception {
        var request = get("/api/books?bookCont=Harry Potter");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(2)
                .allSatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("book_title").asString().containsIgnoringCase("Harry Potter")));
    }

    @Test
    public void testFilterBooksByAuthorFirstAndLastName() throws Exception {
        var request = get("/api/books?authorFirstNameCont=Leo&authorSurnameCont=Tolstoy");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .allSatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("author_first_name").asString().containsIgnoringCase("Leo"))
                        .and(n -> n.node("author_surname").asString().containsIgnoringCase("Tolstoy")));
    }

    @Test
    public void testFilterWithPublisherTitleCont() throws Exception {
        var request = get("/api/books?publisherTitleCont=AST");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .allSatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("publisher").isEqualTo("AST")));
    }

    @Test
    public void testWithContGenreTypes() throws Exception {
        var request = get("/api/books?genreTypes=Fantasy&genreTypes=Horror");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(2)
                .allSatisfy(element -> assertThatJson(element)
                        .node("genre_types").isArray()
                        .contains("Fantasy")
                        .contains("Horror"));
    }

    @Test
    public void testFilterBookWithDirectionOfLiterature() throws Exception {
        var request = get("/api/books?directionOfLiterature=Domestic literature");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray()
                .allSatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("direction_of_literature").isEqualTo("Domestic literature")));
    }

    @Test
    public void testWithAllFilterForBook() throws Exception {
        var request = get("/api/books")
                .param("bookCont", "War")
                .param("authorFirstNameCont", "Leo")
                .param("publisherTitleCont", "AST")
                .param("genreTypes", "Novel")
                .param("directionOfLiterature", "Domestic literature");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .allSatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("book_title").isEqualTo("War and Peace"))
                        .and(n -> n.node("author_first_name").isEqualTo("Leo"))
                        .and(n -> n.node("author_surname").isEqualTo("Tolstoy"))
                        .and(n -> n.node("publisher").isEqualTo("AST"))
                        .and(n -> n.node("genre_types").isArray().contains("Novel"))
                        .and(n -> n.node("direction_of_literature").isEqualTo("Domestic literature")));
    }
}
