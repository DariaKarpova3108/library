package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.AuthorDTO.AuthorCreateDTO;
import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.AuthorDTO.AuthorUpdateDTO;
import library.code.models.Author;
import library.code.repositories.AuthorRepository;
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
public class AuthorControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private AuthorRepository authorRepository;
    private Author author;

    @BeforeEach
    public void setUp() {
        author = Instancio.of(modelGenerator.getAuthorModel()).create();
        authorRepository.save(author);
    }

    @Test
    public void testListGetAuthor() throws Exception {
        var request = get("/api/authors");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetAuthor() throws Exception {
        var request = get("/api/authors/" + author.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body)
                .and(n -> n.node("first_name").isEqualTo(author.getFirstName()))
                .and(n -> n.node("last_name").isEqualTo(author.getLastName()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        var createDTO = new AuthorCreateDTO();
        createDTO.setFirstName("Alexandr");
        createDTO.setLastName("Pushkin");

        var request = post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        var authorResponseDTO = mapper.readValue(responseBody, AuthorDTO.class);

        assertThat(authorResponseDTO.getFirstName()).isEqualTo("Alexandr");
        assertThat(authorResponseDTO.getLastName()).isEqualTo("Pushkin");
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        var updateDTO = new AuthorUpdateDTO();
        updateDTO.setFirstName(JsonNullable.of("Maxim"));

        var request = put("/api/authors/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        var authorResponseDTO = mapper.readValue(responseBody, AuthorDTO.class);

        assertThat(authorResponseDTO.getFirstName()).isEqualTo("Maxim");
        assertThat(authorResponseDTO.getLastName()).isEqualTo(author.getLastName());
    }

    @Test
    public void deleteAuthor() throws Exception {
        var request = delete("/api/authors/" + author.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(authorRepository.findById(author.getId())).isEmpty();
    }
}