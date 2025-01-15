package library.code.unil;

import library.code.exception.ResourceNotFoundException;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtils {

    private final UserRepository userRepository;

    public boolean checkCurrentUser(Long id) {
        log.info("Starting check for user with ID: {}", id);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Authentication failed for user with ID: {}", id);
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        var email = authentication.getName();
        log.debug("Authenticated user email: {}", email);

        var currentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        boolean isUser = currentUser.getEmail().equals(email);

        if (isUser) {
            log.info("Authenticated user with email: {} is the same as user with ID: {}", email, id);
        } else {
            log.warn("Authenticated user with email: {} does not match user with ID: {}", email, id);
        }

        return isUser;
    }
}
