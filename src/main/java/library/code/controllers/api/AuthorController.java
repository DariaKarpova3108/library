package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.AuthorDTO.AuthorCreateDTO;
import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.AuthorDTO.AuthorUpdateDTO;
import library.code.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getListAuthors() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(authorService.getAllAuthors().size()))
                .body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO createNewAuthor(@RequestBody @Valid AuthorCreateDTO createDTO) {
        return authorService.createAuthor(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO updateAuthor(@RequestBody @Valid AuthorUpdateDTO updateDTO, @PathVariable Long id) {
        return authorService.updateAuthor(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}
