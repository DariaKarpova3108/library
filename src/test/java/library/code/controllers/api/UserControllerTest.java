package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.userDTO.UserCreateDTO;
import library.code.dto.userDTO.UserDTO;
import library.code.dto.userDTO.UserUpdateDTO;
import library.code.models.Role;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper mapper;
    private User user;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Role role = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        user = new User();
        user.setEmail("user@admin.library");
        user.setPasswordDigest("hashedPassword");
        user.setRole(role);
        userRepository.save(user);

        token = jwt().jwt(builder -> builder
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleName().name())
                .build());
    }

    @Test
    public void testUserInitialization() {
        var user = userRepository.findByEmail("user@admin.library");
        assertNotNull(user);
        assertEquals("user@admin.library", user.get().getEmail());
    }

    @Test
    public void testGetUser() throws Exception {
        var request = get("/api/users/" + user.getId())
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();
        assertThatJson(body)
                .and(n -> n.node("email").isEqualTo(user.getEmail()));
    }

    @Test
    public void testCreateUser() throws Exception {
        var createDTO = new UserCreateDTO();

        createDTO.setEmail("testUser@test.com");
        createDTO.setPassword("123");

        var request = post("/api/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();

        System.out.println("Response Body: " + body);

        var responseUserDTO = mapper.readValue(body, UserDTO.class);
        assertThat(responseUserDTO.getEmail()).isEqualTo("testUser@test.com");
    }

    @Test
    public void testUpdateUser() throws Exception {
        var currentToken = jwt().jwt(builder -> builder
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleName()));

        var userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setEmail(JsonNullable.of("newEmail@example.ru"));

        var request = put("/api/users/" + user.getId())
                .with(currentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userUpdateDTO));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).isNotNull();

        var responseBody = mapper.readValue(body, UserDTO.class);
        assertThat(responseBody.getEmail()).isEqualTo("newEmail@example.ru");
    }

    @Test
    public void testDeleteUser() throws Exception {
        var currentToken = jwt().jwt(builder -> builder
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleName()));

        var request = delete("/api/users/" + user.getId())
                .with(currentToken);

        mockMvc.perform(request);
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
