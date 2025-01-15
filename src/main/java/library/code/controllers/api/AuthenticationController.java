package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import library.code.dto.AuthRequest;
import library.code.repositories.UserRepository;
import library.code.unil.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер аутентификации",
        description = "Операции аутентификации пользователей, включая логин и получение JWT токена."
                + "Позволяет войти в систему после успешной проверки учетных данных")
public class AuthenticationController {

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Operation(summary = "Аутентификация пользователя",
            description = "Метод принимает данные для входа (email и пароль), "
                    + "аутентифицирует пользователя и возвращает JWT токен")
    @PostMapping("/login")
    public ResponseEntity<?> create(@RequestBody AuthRequest authRequest) {
        log.info("Attempting to authenticate user with email: {}", authRequest.getEmail());

        try {
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authentication);

            var userDetails = (UserDetails) authenticate.getPrincipal();

            var user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var token = jwtUtils.generateToken(user);

            log.info("Successfully authenticated user with email: {}", authRequest.getEmail());
            log.debug("Generated JWT token for user: {}", user.getEmail());

            return ResponseEntity.ok().body(token);

        } catch (BadCredentialsException ex) {
            log.warn("Failed authentication attempt for email: {} - Invalid username or password",
                    authRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
