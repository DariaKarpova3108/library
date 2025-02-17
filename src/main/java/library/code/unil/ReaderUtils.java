package library.code.unil;

import library.code.exception.ResourceNotFoundException;
import library.code.repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReaderUtils {

    private final ReaderRepository readerRepository;

    public boolean checkCurrentReader(Long id) {
        log.info("Starting check for reader with ID: {}", id);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Authentication failed for user with ID: {}", id);
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        var email = authentication.getName();
        log.debug("Authenticated user email: {}", email);

        var currentReader = readerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reader with ID: {} not found", id);
                    return new ResourceNotFoundException("Reader with id: " + id + " not found");
                });

        boolean isReader = currentReader.getUser().getEmail().equals(email);

        if (isReader) {
            log.info("User with email: {} is a valid reader with ID: {}", email, id);
        } else {
            log.warn("User with email: {} does not match reader with ID: {}", email, id);
        }

        return isReader;
    }
}
