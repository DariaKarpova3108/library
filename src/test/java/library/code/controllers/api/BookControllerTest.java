package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.BookDTO.BookCreateDTO;
import library.code.dto.BookDTO.BookUpdateDTO;
import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.Publisher;
import library.code.repositories.AuthorRepository;
import library.code.repositories.BookRepository;
import library.code.repositories.PublisherRepository;
import library.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
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
public class BookControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    private Book book;
    private Author author;
    private Publisher publisher;
    private Genre genre;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        author = Instancio.of(modelGenerator.getAuthorModel()).create();
        publisher = Instancio.of(modelGenerator.getPublisherModel()).create();
        genre = Instancio.of(modelGenerator.getGenreModel()).create();
        book = Instancio.of(modelGenerator.getBookModel()).create();
        publisherRepository.save(publisher);
        authorRepository.save(author);
        book.setPublisher(publisher);
        book.setAuthor(author);
        book.setGenres(new HashSet<>(Set.of(genre)));
        bookRepository.save(book);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void testGetListBooks() throws Exception {
        var request = get("/api/books");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void testGetBook() throws Exception {
        var request = get("/api/books/" + book.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("book_title").isEqualTo(book.getBookTitle()))
                .and(n -> n.node("author_first_name").isEqualTo(book.getAuthor().getFirstName()))
                .and(n -> n.node("author_surname").isEqualTo(book.getAuthor().getLastName()))
                .and(n -> n.node("publisher").isEqualTo(book.getPublisher().getTitle()))
                .and(n -> n.node("direction_of_literature").isEqualTo(book.getDirectionOfLiterature()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateBook() throws Exception {
        var createDTO = new BookCreateDTO();
        createDTO.setBookTitle("Book");
        createDTO.setAuthorId(author.getId());
        createDTO.setPublisherId(publisher.getId());
        createDTO.setGenreTypes(Set.of(genre.getTypeOfGenre()));
        createDTO.setIsbn(String.valueOf(1111111111));

        var result = post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        mockMvc.perform(result)
                .andExpect(status().isCreated());

        var savedBook = bookRepository.findByIsbn("1111111111").get();
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo(createDTO.getBookTitle());
        assertThat(savedBook.getIsbn()).isEqualTo(createDTO.getIsbn());
        assertThat(savedBook.getPublisher().getId()).isEqualTo(createDTO.getPublisherId());
        assertThat(savedBook.getAuthor().getId()).isEqualTo(createDTO.getAuthorId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateBook() throws Exception {
        var updateDTO = new BookUpdateDTO();
        updateDTO.setBookTitle(JsonNullable.of("newBook"));
        updateDTO.setIsbn(JsonNullable.of("22222222222"));

        var result = put("/api/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(result)
                .andExpect(status().isOk());

        var updatedBook = bookRepository.findById(book.getId()).get();
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getBookTitle()).isEqualTo("newBook");
        assertThat(updatedBook.getIsbn()).isEqualTo("22222222222");
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(book.getAuthor().getId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBook() throws Exception {
        var request = delete("/api/books/" + book.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}
