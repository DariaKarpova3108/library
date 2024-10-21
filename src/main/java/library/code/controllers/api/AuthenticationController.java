package library.code.controllers.api;

import library.code.dto.AuthRequest;
import library.code.repositories.UserRepository;
import library.code.unil.JWTUtils;
import lombok.RequiredArgsConstructor;
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
public class AuthenticationController {

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> create(@RequestBody AuthRequest authRequest) {

        try {
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authentication);

            var userDetails = (UserDetails) authenticate.getPrincipal();

            var user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var token = jwtUtils.generateToken(user);
            return ResponseEntity.ok()
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
