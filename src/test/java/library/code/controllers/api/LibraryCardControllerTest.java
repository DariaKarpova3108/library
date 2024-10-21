package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.LibraryCardDTO.LibraryCardUpdateDTO;
import library.code.models.LibraryCard;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.LibraryCardRepository;
import library.code.repositories.ReaderRepository;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
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
    private ModelGenerator modelGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private RoleRepository roleRepository;
    private LibraryCard libraryCard;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        var role = roleRepository.findByRoleName(RoleName.READER)
                .orElseThrow(() -> new RuntimeException("Role READER not found"));

        var reader = Instancio.of(modelGenerator.getReaderModel()).create();

        User user = new User();
        user.setEmail("reader@example.com");
        user.setPasswordDigest("hashedPassword");
        user.setRole(role);
        user.setReader(reader);
        userRepository.save(user);

        reader.setUser(user);
        readerRepository.save(reader);

        libraryCard = Instancio.of(modelGenerator.getLibraryCardModel()).create();
        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);

        reader.setLibraryCard(libraryCard);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetListLibraryCards() throws Exception {
        var request = get("/api/libraryCards");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
      @WithMockUser(roles = {"ADMIN"})
    public void testGetLibraryCard() throws Exception {
      var request = get("/api/libraryCards/" + libraryCard.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("reader_first_name").isEqualTo(libraryCard.getReader().getFirstName()))
                .and(n -> n.node("reader_surname").isEqualTo(libraryCard.getReader().getLastName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
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
    @WithMockUser(roles = {"ADMIN"})
    public void testDelete() throws Exception {
        var request = delete("/api/libraryCards/" + libraryCard.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(libraryCardRepository.findById(libraryCard.getId())).isEmpty();
    }
}
