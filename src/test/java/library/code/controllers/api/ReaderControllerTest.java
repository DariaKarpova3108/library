package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.models.Reader;
import library.code.models.Role;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LibraryCardRepository libraryCardRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ModelGenerator modelGenerator;
    private Reader reader;
    private User user;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Role readerRole = roleRepository.findByRoleName(RoleName.READER)
                .orElseThrow(() -> new RuntimeException("Role READER not found"));

        var libraryCard = Instancio.of(modelGenerator.getLibraryCardModel()).create();
        reader = Instancio.of(modelGenerator.getReaderModel()).create();

        user = new User();
        user.setEmail("reader@example.com");
        user.setPasswordDigest("hashedPassword");
        user.setRole(readerRole);
        userRepository.save(user);

        reader.setUser(user);
        readerRepository.save(reader);

        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);
        reader.setLibraryCard(libraryCard);

        token = jwt().jwt(builder -> builder
                .subject(reader.getUser().getEmail())
                .claim("role", reader.getUser().getRole().getRoleName().name())
                .build());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
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
        var request = get("/api/readers/" + reader.getId())
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("first_name").isEqualTo(reader.getFirstName()));
    }

    @Test
    public void testCompleteReaderProfile() throws Exception {
        var createDTO = new ReaderCreateDTO();
        createDTO.setPassportDetails("1234123456");
        createDTO.setAge(18);
        createDTO.setFirstName("testFirstName");
        createDTO.setLastName("testLastName");
        createDTO.setPhone("11111111111");
        createDTO.setAddress("Moscow-city, street Test, h.42");

        var request = post("/api/readers/" + user.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdReader = readerRepository.findByPassportDetails("1234123456").get();

        assertThat(createdReader).isNotNull();
        assertThat(createdReader.getFirstName()).isEqualTo("testFirstName");
        assertThat(createdReader.getLibraryCard()).isNotNull();
        assertThat(createdReader.getLibraryCard().getCardNumber()).isNotNull();
    }

    @Test
    public void testUpdateReader() throws Exception {
        var updateDTO = new ReaderUpdateDTO();
        updateDTO.setLastName(JsonNullable.of("LastNameUpdate"));
        updateDTO.setPhone(JsonNullable.of("12345676543"));

        var request = put("/api/readers/" + reader.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updateReader = readerRepository.findById(reader.getId()).get();

        assertThat(updateReader).isNotNull();
        assertThat(updateReader.getPhone()).isEqualTo("12345676543");
        assertThat(updateReader.getLastName()).isEqualTo("LastNameUpdate");
        assertThat(updateReader.getPassportDetails()).isEqualTo(reader.getPassportDetails());
        assertThat(updateReader.getFirstName()).isEqualTo(reader.getFirstName());
    }

    @Test
    public void testDeleteReader() throws Exception {
        var request = delete("/api/readers/" + reader.getId())
                .with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(readerRepository.findById(reader.getId())).isEmpty();
    }

    @Test
    public void testGetLibraryCard() throws Exception {
        var request = get("/api/readers/" + reader.getId() + "/libraryCard").with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        System.out.println(body);

        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("reader_first_name").isEqualTo(reader.getFirstName()));

    }
}
