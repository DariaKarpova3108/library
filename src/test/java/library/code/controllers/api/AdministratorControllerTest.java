package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.administatorDTO.AdminCreateDTO;
import library.code.dto.administatorDTO.AdminUpdateDTO;
import library.code.dto.administatorDTO.AdministratorDTO;
import library.code.models.Administrator;
import library.code.models.Role;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.AdministratorRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdministratorControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdministratorRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    private Administrator admin;
    private User user;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Role role = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        admin = Instancio.of(modelGenerator.getAdministratorModel()).create();

        user = new User();
        user.setEmail("user@admin.library");
        user.setPasswordDigest("hashPassword");
        user.setRole(role);
        userRepository.save(user);

        admin.setUser(user);
        adminRepository.save(admin);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAllAdmins() throws Exception {
        var request = get("/api/administrations");

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAuthor() throws Exception {
        var request = get("/api/administrations/" + admin.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("jobTitle").isEqualTo(admin.getJobTitle()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCompleteAdminProfile() throws Exception {
        var createDTO = new AdminCreateDTO();
        createDTO.setJobTitle("Developer");
        createDTO.setPhone("89002000001");

        var request = post("/api/administrations/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).isNotNull();
        var responseAdminDTO = mapper.readValue(responseBody, AdministratorDTO.class);

        assertThat(responseAdminDTO.getJobTitle()).isEqualTo("Developer");
        assertThat(responseAdminDTO.getPhone()).isEqualTo("89002000001");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateAdmin() throws Exception {
        var updateDTO = new AdminUpdateDTO();
        updateDTO.setJobTitle(JsonNullable.of("newJobTitle"));

        var request = put("/api/administrations/" + admin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isNotNull();

        var responseAdminDTO = mapper.readValue(responseBody, AdministratorDTO.class);

        assertThat(responseAdminDTO.getJobTitle()).isEqualTo("newJobTitle");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteAdmin() throws Exception {
        var request = delete("/api/administrations/" + admin.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(adminRepository.findById(admin.getId())).isEmpty();
    }
}
