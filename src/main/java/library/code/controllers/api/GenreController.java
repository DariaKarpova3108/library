package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getListGenres() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(genreService.getAllGenres().size()))
                .body(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO getGenre(@PathVariable Long id) {
        return genreService.getGenre(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDTO createNewGenre(@RequestBody @Valid GenreCreateDTO createDTO) {
        return genreService.createGenre(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO updateGenre(@RequestBody @Valid GenreUpdateDTO updateDTO, @PathVariable Long id) {
        return genreService.updateGenre(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }


}
