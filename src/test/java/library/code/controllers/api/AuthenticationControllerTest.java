package library.code.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import library.code.dto.AuthRequest;
import library.code.models.User;
import library.code.unil.JWTUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JWTUtils jwtUtils;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
        AuthRequest userData = new AuthRequest();
        userData.setEmail("admin@admin.library");
        userData.setPassword("admin");

        var mockUser = org.springframework.security.core.userdetails.User
                .withUsername(userData.getEmail())
                .password(userData.getPassword())
                .roles("ADMIN")
                .build();


        var token = "mocked-jwt-token";

        Mockito.when(jwtUtils.generateToken(Mockito.any(User.class))).thenReturn(token);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities()));

        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(token));

    }

    @Test
    public void shouldReturnTokenWhenLoginIsFail() throws Exception {
        AuthRequest userDataWrong = new AuthRequest();
        userDataWrong.setEmail("admin@admin.library");
        userDataWrong.setPassword("wrong-password");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDataWrong));

        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
