package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.models.Publisher;
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
public class PublisherControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private PublisherRepository publisherRepository;
    private Publisher publisher;

    @BeforeEach
    public void setUp() {
        publisher = Instancio.of(modelGenerator.getPublisherModel()).create();
        publisherRepository.save(publisher);
    }

    @Test
    public void testGetListPublisher() throws Exception {
        var request = get("/api/publishers");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetPublisher() throws Exception {
        var request = get("/api/publishers/" + publisher.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("title").isEqualTo(publisher.getTitle()));
    }

    @Test
    public void testCreatePublisher() throws Exception {
        var createDTO = new PublisherCreateDTO();
        createDTO.setTitle("newTitle");
        createDTO.setAddress("newAddress");
        var request = post("/api/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        var publisherResponseDTO = mapper.readValue(responseBody, PublisherDTO.class);

        var savedPublisher = publisherRepository.findById(publisherResponseDTO.getId()).get();
        assertThat(savedPublisher).isNotNull();
        assertThat(savedPublisher.getTitle()).isEqualTo("newTitle");
        assertThat(savedPublisher.getAddress()).isEqualTo("newAddress");
    }

    @Test
    public void testUpdatePublisher() throws Exception {
        var updateDTO = new PublisherUpdateDTO();
        updateDTO.setTitle(JsonNullable.of("updateTitle"));

        var request = put("/api/publishers/" + publisher.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedPublisher = publisherRepository.findById(publisher.getId()).get();
        assertThat(updatedPublisher.getTitle()).isEqualTo("updateTitle");
        assertThat(updatedPublisher.getPhone()).isEqualTo(publisher.getPhone());
        assertThat(updatedPublisher.getAddress()).isEqualTo(publisher.getAddress());
    }

    @Test
    public void testDeletePublisher() throws Exception {
        var request = delete("/api/publishers/" + publisher.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(publisherRepository.findById(publisher.getId())).isEmpty();
    }
}
