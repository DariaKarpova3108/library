package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.LibraryCardDTO.LibraryCardUpdateDTO;
import library.code.models.LibraryCard;
import library.code.models.Reader;
import library.code.repositories.LibraryCardRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LibraryCardControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LibraryCardRepository libraryCardRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    private LibraryCard libraryCard;
    private Reader reader;

    @BeforeEach
    public void setUp() {
        reader = Instancio.of(modelGenerator.getReaderModel()).create(); // Создаем Reader
        libraryCard = Instancio.of(modelGenerator.getLibraryCardModel()).create();
        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);
    }

    ///     api/libraryCards

    @Test
    public void testGetListLibraryCards() throws Exception {
        var request = get("/api/libraryCards");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testGetLibraryCard() throws Exception {
        var request = get("/api/libraryCards/" + libraryCard.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        System.out.println(body);

        assertThatJson(body)
                .and(n -> n.node("reader_first_name").isEqualTo(libraryCard.getReader().getFirstName()))
                .and(n -> n.node("reader_surname").isEqualTo(libraryCard.getReader().getLastName()));
    }

    @Test
    public void testUpdateLibraryCard() throws Exception {
        var updateDTo = new LibraryCardUpdateDTO();
        updateDTo.setCardNumber(JsonNullable.of("1111111111111"));

        var request = put("/api/libraryCards/" + libraryCard.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTo));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updateCard = libraryCardRepository.findById(libraryCard.getId());
        assertThat(updateCard).isPresent();
        assertThat(updateCard.get().getCardNumber()).isEqualTo("1111111111111");
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/libraryCards/" + libraryCard.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(libraryCardRepository.findById(libraryCard.getId())).isEmpty();
    }
}
