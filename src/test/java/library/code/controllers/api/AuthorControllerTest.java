package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.authorDTO.AuthorCreateDTO;
import library.code.dto.authorDTO.AuthorDTO;
import library.code.dto.authorDTO.AuthorUpdateDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        author = Instancio.of(modelGenerator.getAuthorModel()).create();
        authorRepository.save(author);

        var author1 = Instancio.of(modelGenerator.getAuthorModel()).create();
        author1.setFirstName("Alexandr");
        author1.setLastName("Pushkin");
        authorRepository.save(author1);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void testListGetAuthor() throws Exception {
        var request = get("/api/authors");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void testGetAuthor() throws Exception {
        var request = get("/api/authors/" + author.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("first_name").isEqualTo(author.getFirstName()))
                .and(n -> n.node("last_name").isEqualTo(author.getLastName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
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
        assertThat(responseBody).isNotNull();

        var authorResponseDTO = mapper.readValue(responseBody, AuthorDTO.class);

        assertThat(authorResponseDTO.getFirstName()).isEqualTo("Alexandr");
        assertThat(authorResponseDTO.getLastName()).isEqualTo("Pushkin");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
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

        assertThat(responseBody).isNotNull();
        assertThat(authorResponseDTO.getFirstName()).isEqualTo("Maxim");
        assertThat(authorResponseDTO.getLastName()).isEqualTo(author.getLastName());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deleteAuthor() throws Exception {
        var request = delete("/api/authors/" + author.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(authorRepository.findById(author.getId())).isEmpty();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void getListAuthorsWithParamName() throws Exception {
        var request = get("/api/authors?firstNameCont=alex");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray()
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("first_name").asString().containsIgnoringCase("alex")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void getListAuthorParamSurname() throws Exception {
        var request = get("/api/authors?lastNameCont=push");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray()
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("last_name").asString().containsIgnoringCase("push")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READER"})
    public void getListAuthorsWithAllParams() throws Exception {
        var request = get("/api/authors?firstNameCont=alex&lastNameCont=push");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray()
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("first_name").asString().containsIgnoringCase("alex"))
                        .and(n -> n.node("last_name").asString().containsIgnoringCase("push")));
    }
}
