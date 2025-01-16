package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.libraryCardDTO.LibraryCardDTO;
import library.code.dto.readerDTO.ReaderCreateDTO;
import library.code.dto.readerDTO.ReaderDTO;
import library.code.dto.readerDTO.ReaderUpdateDTO;
import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.service.ReaderService;
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
@RequestMapping("/api/readers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления профилями читателей",
        description = "Позволяет выполнять CRUD операции с профилями читателей, а также считывать информацию об"
                + " их читательских билетах")
public class ReaderController {

    private final ReaderService readerService;

    @Operation(
            summary = "Получение списка всех профилей читателей",
            description = "Возвращает список всех профилей читателей с параметрами сортировки и пагинации. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReaderDTO>> getListReadersProfiles(ReaderParamDTO params,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "id, asc") String sort) {

        log.info("Received request to fetch reader profiles with parameters: page = {}, sort = {}", page, sort);
        var allReaders = readerService.getAllReaders(params, page, sort);
        log.info("Total number of reader profiles fetched: {}", allReaders.size());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allReaders.size()))
                .body(allReaders);
    }


    @Operation(
            summary = "Получение профиля читателя",
            description = "Возвращает информацию о конкретном профиле читателя по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого читателя"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public ReaderDTO getReaderProfile(@PathVariable Long id) {
        log.info("Received request to fetch reader profile with ID: {}", id);
        var reader = readerService.getReader(id);
        log.info("Reader profile with ID {} found", id);
        return reader;
    }

    @Operation(
            summary = "Получение читательского билета читателя",
            description = "Возвращает информацию о читательском билете для указанного читателя. "
                    + "Доступно для пользователей с ролью ADMIN или для самого читателя"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{readerId}/libraryCard")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#readerId)")
    public LibraryCardDTO getLibraryCard(@PathVariable Long readerId) {
        log.info("Received request to fetch library card for reader with ID: {}", readerId);
        var libraryCard = readerService.getLibraryCardByReaderId(readerId);
        log.info("Library card for reader with ID {} found", readerId);
        return libraryCard;
    }

    @Operation(
            summary = "Заполнение профиля читателя",
            description = "Позволяет завершить заполнение профиля читателя для пользователя с указанным ID."
                    + " Доступно для пользователя с ролью ADMIN или для самого читателя"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#userId)")
    public ReaderDTO completeReaderProfile(@PathVariable Long userId, @RequestBody @Valid ReaderCreateDTO createDTO) {
        log.info("Received request to complete profile for user with ID: {}", userId);
        var reader = readerService.completeReaderProfile(userId, createDTO);
        log.info("Reader profile for user with ID {} completed successfully", userId);
        return reader;
    }

    @Operation(
            summary = "Обновление профиля читателя",
            description = "Позволяет обновить профиль читателя по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого читателя"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public ReaderDTO updateReaderProfile(@RequestBody @Valid ReaderUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Received request to update reader profile with ID: {}", id);
        var updatedReader = readerService.updateReader(updateDTO, id);
        log.info("Reader profile with ID {} updated successfully", id);
        return updatedReader;
    }


    @Operation(
            summary = "Удаление профиля читателя",
            description = "Позволяет удалить профиль читателя по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого читателя"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public void deleteReaderProfile(@PathVariable Long id) {
        log.info("Received request to delete reader profile with ID: {}", id);
        readerService.deleteReader(id);
        log.info("Reader profile with ID {} deleted successfully", id);
    }
}
