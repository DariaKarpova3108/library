package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.LibraryCardDTO.LibraryCardDTO;
import library.code.dto.LibraryCardDTO.LibraryCardUpdateDTO;
import library.code.service.LibraryCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/libraryCards")
@RequiredArgsConstructor
public class LibraryCardController {

    private final LibraryCardService libraryCardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LibraryCardDTO>> getListLibraryCards() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(libraryCardService.getAllCards().size()))
                .body(libraryCardService.getAllCards());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardDTO getLibraryCard(@PathVariable Long id) {
        return libraryCardService.getLibraryCard(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardDTO updateLibraryCard(@RequestBody @Valid LibraryCardUpdateDTO updateDTO, @PathVariable Long id) {
        return libraryCardService.updateLibraryCard(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLibraryCard(@PathVariable Long id) {
        libraryCardService.deleteLibraryCard(id);
    }
}
