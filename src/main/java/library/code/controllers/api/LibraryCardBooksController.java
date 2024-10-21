package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.service.LibraryCardBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/libraryCardBooks")
@RequiredArgsConstructor
public class LibraryCardBooksController {

    private final LibraryCardBooksService cardBooksService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LibraryCardBookDTO>> getListLibraryBooks() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(cardBooksService.getAllCardBooks().size()))
                .body(cardBooksService.getAllCardBooks());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('READER') "
            + "and @libraryCardBooksService.isReaderOwnerOfBook(#id, authentication.principal.id))")
    public LibraryCardBookDTO getLibraryBook(@PathVariable Long id) {
        return cardBooksService.getCardBooks(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardBookDTO createNewLibraryBook(@RequestBody @Valid LibraryCardBookCreateDTO createDTO) {
        return cardBooksService.createCardBooks(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('READER') "
            + "and @libraryCardBooksService.isReaderOwnerOfBook(#id, authentication.principal.id))")
    public LibraryCardBookDTO updateLibraryBook(@RequestBody @Valid LibraryCardBookUpdateDTO updateDTO,
                                                @PathVariable Long id) {
        return cardBooksService.updateCardBooks(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLibraryBook(@PathVariable Long id) {
        cardBooksService.deleteCardBooks(id);
    }
}

