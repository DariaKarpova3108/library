package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.bookDTO.BookCreateDTO;
import library.code.dto.bookDTO.BookDTO;
import library.code.dto.bookDTO.BookUpdateDTO;
import library.code.dto.specificationDTO.BookParamDTO;
import library.code.service.BookService;
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
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления книжным фондом",
        description = "Позволяет проводить CRUD операции с книгами")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Получение списка книг",
            description = "Возвращает список книг с возможностью фильтрации, пагинации и сортировки"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<BookDTO>> getListBooks(BookParamDTO params,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "id, asc") String sort) {

        log.info("Fetching books list with params: {}, page: {}, sort: {}", params, page, sort);
        var bookList = bookService.getAllBooks(params, page, sort);
        log.info("Found {} books", bookList.size());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(bookList.size()))
                .body(bookList);
    }

    @Operation(
            summary = "Получение книги по ID",
            description = "Возвращает информацию о книге по её уникальному идентификатору"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public BookDTO getBook(@PathVariable Long id) {
        log.info("Fetching book with ID: {}", id);
        var book = bookService.getBook(id);
        log.info("Found book: {}", book);
        return book;
    }

    @Operation(
            summary = "Создание новой книги",
            description = "Создаёт новую книгу на основе переданных данных"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BookDTO createNewBook(@RequestBody @Valid BookCreateDTO createDTO) {
        log.info("Creating new book with data: {}", createDTO);
        var newBook = bookService.createBook(createDTO);
        log.info("Successfully created book: {}", newBook);
        return newBook;
    }

    @Operation(
            summary = "Обновление книги",
            description = "Обновляет информацию о книге по её уникальному идентификатору"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BookDTO updateBook(@RequestBody @Valid BookUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Updating book with ID: {} and data: {}", id, updateDTO);
        var updateBook = bookService.updateBook(updateDTO, id);
        log.info("Successfully updated book: {}", updateBook);
        return updateBook;
    }

    @Operation(
            summary = "Удаление книги",
            description = "Удаляет книгу по её уникальному идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        log.info("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        log.info("Successfully deleted book with ID: {}", id);
    }
}
