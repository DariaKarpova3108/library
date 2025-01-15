package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.authorDTO.AuthorCreateDTO;
import library.code.dto.authorDTO.AuthorDTO;
import library.code.dto.authorDTO.AuthorUpdateDTO;
import library.code.dto.specificationDTO.AuthorParamDTO;
import library.code.service.AuthorService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления авторами",
        description = "Позволяет проводить CRUD операции с авторами")
public class AuthorController {

    private final AuthorService authorService;


    @Operation(
            summary = "Получение списка авторов",
            description = "Возвращает список авторов с возможностью фильтрации, пагинации и сортировки"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<AuthorDTO>> getListAuthors(AuthorParamDTO params,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "id, asc") String sort) {

        log.info("Fetching authors list with params: {}, page: {}, sort: {}", params, page, sort);
        var authorList = authorService.getAllAuthors(params, page, sort);
        log.info("Found {} authors", authorList.size());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(authorList.size()))
                .body(authorList);
    }


    @Operation(
            summary = "Получение информации об авторе",
            description = "Возвращает информацию об авторе по его уникальному идентификатору"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public AuthorDTO getAuthor(@PathVariable Long id) {
        log.info("Fetching author with ID: {}", id);
        var author = authorService.getAuthor(id);
        log.info("Found author: {}", author);
        return author;
    }

    @Operation(
            summary = "Создание нового автора",
            description = "Создаёт нового автора в системе"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AuthorDTO createNewAuthor(@RequestBody @Valid AuthorCreateDTO createDTO) {
        log.info("Creating new author with data: {}", createDTO);
        var author = authorService.createAuthor(createDTO);
        log.info("Successfully created author: {}", author);
        return author;
    }

    @Operation(
            summary = "Обновление информации об авторе",
            description = "Обновляет данные об авторе по его уникальному идентификатору"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public AuthorDTO updateAuthor(@RequestBody @Valid AuthorUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Updating author with ID: {} and data: {}", id, updateDTO);
        var updatedAuthor = authorService.updateAuthor(updateDTO, id);
        log.info("Successfully updated author: {}", updatedAuthor);
        return updatedAuthor;
    }

    @Operation(
            summary = "Удаление автора",
            description = "Удаляет автора по его уникальному идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAuthor(@PathVariable Long id) {
        log.info("Deleting author with ID: {}", id);
        authorService.deleteAuthor(id);
        log.info("Successfully deleted author with ID: {}", id);
    }
}
