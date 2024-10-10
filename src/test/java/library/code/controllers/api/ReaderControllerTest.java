package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.models.Reader;
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
public class ReaderControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ModelGenerator modelGenerator;
    private Reader reader;

    @BeforeEach
    public void setUp() {
        reader = Instancio.of(modelGenerator.getReaderModel()).create();
        readerRepository.save(reader);
    }

    @Test
    public void testGetList() throws Exception {
        var request = get("/api/readers");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetReader() throws Exception {
        var request = get("/api/readers/" + reader.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("email").isEqualTo(reader.getEmail()))
                .and(n -> n.node("first_name").isEqualTo(reader.getFirstName()));
    }

    @Test
    public void testCreateReader() throws Exception {
        var createDTO = new ReaderCreateDTO();
        createDTO.setEmail("test@test.com");
        createDTO.setPassportDetails("1234123456");
        createDTO.setAge(18);
        createDTO.setFirstName("testFirstName");
        createDTO.setLastName("testLastName");
        createDTO.setPhone("11111111111");
        createDTO.setAddress("Moscow-city, street Test, h.42");

        var request = post("/api/readers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdReader = readerRepository.findByPassportDetails("1234123456").get();

        assertThat(createdReader).isNotNull();
        assertThat(createdReader.getEmail()).isEqualTo("test@test.com");
        assertThat(createdReader.getLibraryCard()).isNotNull(); //локига на проверку генерации читательского билета
        assertThat(createdReader.getLibraryCard().getCardNumber()).isNotNull();
    }

    @Test
    public void testUpdateReader() throws Exception {  //добавить токен
        var updateDTO = new ReaderUpdateDTO();
        updateDTO.setEmail(JsonNullable.of("qwerty@test.com"));
        updateDTO.setPhone(JsonNullable.of("12345676543"));

        var request = put("/api/readers/" + reader.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updateReader = readerRepository.findById(reader.getId()).get();

        assertThat(updateReader).isNotNull();
        assertThat(updateReader.getPhone()).isEqualTo("12345676543");
        assertThat(updateReader.getEmail()).isEqualTo("qwerty@test.com");
        assertThat(updateReader.getPassportDetails()).isEqualTo(reader.getPassportDetails());
        assertThat(updateReader.getFirstName()).isEqualTo(reader.getFirstName());
        assertThat(updateReader.getLastName()).isEqualTo(reader.getLastName());
    }

    @Test
    public void testDeleteReader() throws Exception {  //добавить токен
        var request = delete("/api/readers/" + reader.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(readerRepository.findById(reader.getId())).isEmpty();
    }
}
