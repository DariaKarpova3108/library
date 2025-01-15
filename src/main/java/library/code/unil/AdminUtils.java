package library.code.unil;

import library.code.exception.ResourceNotFoundException;
import library.code.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUtils {
    private final AdministratorRepository administratorRepository;

    public boolean checkCurrentAdmin(Long id) {
        log.info("Starting check for admin with ID: {}", id);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Authentication failed for user with ID: {}", id);
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        var email = authentication.getName();
        log.debug("Authenticated user email: {}", email);

        var admin = administratorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Admin with ID: {} not found", id);
                    return new ResourceNotFoundException("Admin with ID: " + id + " not found");
                });

        boolean isAdmin = admin.getUser().getEmail().equals(email);

        if (isAdmin) {
            log.info("User with email: {} is a valid admin with ID: {}", email, id);
        } else {
            log.warn("User with email: {} does not match admin with ID: {}", email, id);
        }

        return isAdmin;
    }
}
