package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Контроллер управления жанрами книг",
        description = "Позволяет проводить CRUD операции с жанрами книг")
public class GenreController {

    private final GenreService genreService;

    @Operation(
            summary = "Получение списка жанров",
            description = "Возвращает список всех жанров с возможностью пагинации и сортировки"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<GenreDTO>> getListGenres() {
        log.info("Fetching list of genres");
        var genresList = genreService.getAllGenres();
        log.info("Found {} genres", genresList.size());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(genresList.size()))
                .body(genresList);
    }

    @Operation(
            summary = "Получение жанра по ID",
            description = "Возвращает информацию о жанре по его уникальному идентификатору"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public GenreDTO getGenre(@PathVariable Long id) {
        log.info("Fetching genre with ID: {}", id);
        var genre = genreService.getGenre(id);
        log.info("Found genre: {}", genre);
        return genre;
    }

    @Operation(
            summary = "Создание нового жанра",
            description = "Создаёт новый жанр на основе переданных данных"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDTO createNewGenre(@RequestBody @Valid GenreCreateDTO createDTO) {
        log.info("Creating new genre with data: {}", createDTO);
        var genre = genreService.createGenre(createDTO);
        log.info("Successfully created genre: {}", genre);
        return genre;
    }


    @Operation(
            summary = "Обновление жанра",
            description = "Обновляет информацию о жанре по его уникальному идентификатору"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDTO updateGenre(@RequestBody @Valid GenreUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Updating genre with ID: {} and data: {}", id, updateDTO);
        var updateGenre = genreService.updateGenre(updateDTO, id);
        log.info("Successfully updated genre: {}", updateGenre);
        return updateGenre;
    }


    @Operation(
            summary = "Удаление жанра",
            description = "Удаляет жанр по его уникальному идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGenre(@PathVariable Long id) {
        log.info("Deleting genre with id: {}", id);
        genreService.deleteGenre(id);
        log.info("Successfully deleted genre with id: {}", id);
    }
}
