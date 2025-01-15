package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.service.LibraryCardBooksService;
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
@RequestMapping("/api/{libraryCardId}/libraryCardBooks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления книгами, записанных в читательский билет",
        description = "Позволяет проводить CRUD операции с книгами, добавленными в читательский билет")
public class LibraryCardBooksController {

    private final LibraryCardBooksService cardBooksService;

    @Operation(
            summary = "Получение списка всех записей о книгах в читательском билете",
            description = "Возвращает список всех книг, добавленных в читательский билет. "
                    + "Доступно только для пользователей с ролью ADMIN или "
                    + "читателю, которому принадлежит читательский билет"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('READER') "
            + "and @libraryCardBooksService.isReaderOwnerOfBook(#id, authentication.principal.id))")
    public ResponseEntity<List<LibraryCardBookDTO>> getListLibraryBooks(@PathVariable Long libraryCardId) {

        log.info("Fetching list of all books in library card with ID: {}", libraryCardId);
        var allBooksInLibraryCard = cardBooksService.getAllBooksInCardBooks(libraryCardId);
        log.info("Found {} books in library cards", allBooksInLibraryCard.size());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allBooksInLibraryCard.size()))
                .body(allBooksInLibraryCard);
    }


    @Operation(
            summary = "Получение информации о книге в читательском билете",
            description = "Возвращает информацию о книге, добавленной в читательский билет, "
                    + "по её уникальному идентификатору. Доступно пользователям с ролью ADMIN или "
                    + "читателю, которому принадлежит читательский билет"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('READER') "
            + "and @libraryCardBooksService.isReaderOwnerOfBook(#id, authentication.principal.id))")
    public LibraryCardBookDTO getLibraryBook(@PathVariable Long id) {
        log.info("Fetching book with ID: {} , from library card", id);
        var book = cardBooksService.getBooksInCardBooks(id);
        log.info("Found book in library card with ID: {}", id);
        return book;
    }

    @Operation(
            summary = "Создание новой записи о книге в читательском билете",
            description = "Создаёт новую запись о книге в читательском билете. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public LibraryCardBookDTO createNewLibraryBook(@RequestBody @Valid LibraryCardBookCreateDTO createDTO) {
        log.info("Creating new library card book with data: {}", createDTO);
        var newRecordWithBookInLibraryCard = cardBooksService.createNewRecordInCardBooks(createDTO);
        log.info("Successfully created new library card book: {}", newRecordWithBookInLibraryCard);
        return newRecordWithBookInLibraryCard;
    }

    @Operation(
            summary = "Обновление записи о книге в читательском билете",
            description = "Обновляет информацию о книге в читательском билете по её уникальному идентификатору. "
                    + "Доступно пользователям с ролью ADMIN или читателю, которому принадлежит читательский билет"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('READER') "
            + "and @libraryCardBooksService.isReaderOwnerOfBook(#id, authentication.principal.id))")
    public LibraryCardBookDTO updateLibraryBook(@RequestBody @Valid LibraryCardBookUpdateDTO updateDTO,
                                                @PathVariable Long id) {
        log.info("Updating in library card the record about book with ID: {} and data: {}", id, updateDTO);
        var updateRecordWithBookInLibraryCard = cardBooksService.updateRecordInCardBooks(updateDTO, id);
        log.info("Successfully updated in library card the record about book with ID: {}", id);
        return updateRecordWithBookInLibraryCard;
    }

    @Operation(
            summary = "Удаление записи о книге из читательского билета",
            description = "Удаляет запись о книге из читательского билета по её уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLibraryBook(@PathVariable Long id) {
        log.info("Deleting in library card the record about book with ID: {}", id);
        cardBooksService.deleteBookFromCardBooks(id);
        log.info("Successfully deleted in library card the record about book with ID: {}", id);
    }
}

