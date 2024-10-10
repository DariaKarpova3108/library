package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.models.Genre;
import library.code.repositories.GenreRepository;
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
public class GenreControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    private Genre genre;

    @BeforeEach
    public void setUp() {
        genre = Instancio.of(modelGenerator.getGenreModel()).create();
        genreRepository.save(genre);
    }

    @Test
    public void testGetListGenres() throws Exception {
        var request = get("/api/genres");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetGenre() throws Exception {
        var request = get("/api/genres/" + genre.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("type_of_genre").isEqualTo(genre.getTypeOfGenre()));
    }

    @Test
    public void testCreateGenre() throws Exception {
        var createDTO = new GenreCreateDTO();
        createDTO.setTypeOfGenre("Detective");

        var request = post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        var genreResponseDTO = mapper.readValue(responseBody, GenreDTO.class);

        assertThat(genreResponseDTO.getTypeOfGenre()).isEqualTo(createDTO.getTypeOfGenre());
    }

    @Test
    public void testUpdateGenre() throws Exception {
        var updateDTO = new GenreUpdateDTO();
        updateDTO.setTypeOfGenre(JsonNullable.of("Novel"));

        var request = put("/api/genres/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedGenre = genreRepository.findByTypeOfGenre("Novel").get();

        assertThat(updatedGenre).isNotNull();
        assertThat(updatedGenre.getTypeOfGenre()).isEqualTo("Novel");
    }

    @Test
    public void testDeleteGenre() throws Exception {
        var request = delete("/api/genres/" + genre.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(genreRepository.findById(genre.getId())).isEmpty();
    }
}
