package library.code.unil;

import library.code.exception.ResourceNotFoundException;
import library.code.repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReaderUtils {

    private final ReaderRepository readerRepository;

    public boolean checkCurrentReader(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        var email = authentication.getName();

        var currentReader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader with id: " + id + " not found"));
        return currentReader.getUser().getEmail().equals(email);
    }
}
