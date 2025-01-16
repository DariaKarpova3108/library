package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.libraryCardDTO.LibraryCardDTO;
import library.code.dto.libraryCardDTO.LibraryCardUpdateDTO;
import library.code.service.LibraryCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Контроллер управления читательскими билетами",
        description = "Позволяет проводить CRUD операции с читательскими билетами")
public class LibraryCardController {

    private final LibraryCardService libraryCardService;

    @Operation(
            summary = "Получение списка всех читательских билетов",
            description = "Возвращает список всех читательских билетов. Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LibraryCardDTO>> getListLibraryCards() {

        log.info("Request to get a list of all library cards");
        var allLibraryCard = libraryCardService.getAllCards();
        log.info("Total number of library cards fetched: {}", allLibraryCard.size());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allLibraryCard.size()))
                .body(allLibraryCard);
    }

    @Operation(
            summary = "Получение информации о читательском билете",
            description = "Возвращает информацию о конкретном читательском билете по его уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardDTO getLibraryCard(@PathVariable Long id) {
        log.info("Request received to fetch library card with ID: {}", id);
        var libraryCard = libraryCardService.getLibraryCard(id);
        log.info("Library card with ID {} found", id);
        return libraryCard;
    }

    @Operation(
            summary = "Обновление информации о читательском билете",
            description = "Позволяет обновить данные о читательском билете по его уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardDTO updateLibraryCard(@RequestBody @Valid LibraryCardUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Request received to update library card with ID: {}", id);
        var updatedLibraryCard = libraryCardService.updateLibraryCard(updateDTO, id);
        log.info("Library card with ID {} successfully updated", id);
        return updatedLibraryCard;
    }

    @Operation(
            summary = "Удаление читательского билета",
            description = "Позволяет удалить читательский билет по его уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLibraryCard(@PathVariable Long id) {
        log.info("Request received to delete library card with ID: {}", id);
        libraryCardService.deleteLibraryCard(id);
        log.info("Library card with ID {} successfully deleted", id);
    }
}
