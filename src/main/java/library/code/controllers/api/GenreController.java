package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.service.GenreService;
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
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<GenreDTO>> getListGenres() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(genreService.getAllGenres().size()))
                .body(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public GenreDTO getGenre(@PathVariable Long id) {
        return genreService.getGenre(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDTO createNewGenre(@RequestBody @Valid GenreCreateDTO createDTO) {
        return genreService.createGenre(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDTO updateGenre(@RequestBody @Valid GenreUpdateDTO updateDTO, @PathVariable Long id) {
        return genreService.updateGenre(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
