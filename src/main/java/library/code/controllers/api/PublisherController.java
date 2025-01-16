package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.service.PublisherService;
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
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления издательствами",
        description = "Позволяет выполнять CRUD операции с издательствами")
public class PublisherController {

    private final PublisherService publisherService;


    @Operation(
            summary = "Получение списка всех издательств",
            description = "Возвращает список всех издательств. Доступно для пользователей с ролью ADMIN или READER"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<PublisherDTO>> getListPublishers() {

        log.info("Received request to fetch all publishers");
        var allPublishers = publisherService.getAllPublishers();
        log.info("Total number of publishers fetched: {}", allPublishers.size());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allPublishers.size()))
                .body(allPublishers);
    }

    @Operation(
            summary = "Получение информации об издательстве",
            description = "Возвращает информацию о конкретном издательстве по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или READER"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public PublisherDTO getPublisher(@PathVariable Long id) {
        log.info("Received request to fetch publisher with ID: {}", id);
        var publisher = publisherService.getPublisher(id);
        log.info("Publisher with ID {} found", id);
        return publisher;
    }

    @Operation(
            summary = "Создание нового издательства",
            description = "Позволяет создать нового издателя. Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PublisherDTO createNewPublisher(@RequestBody @Valid PublisherCreateDTO createDTO) {
        log.info("Received request to create a new publisher");
        var publisher = publisherService.createPublisher(createDTO);
        log.info("New publisher with ID {} created successfully", publisher.getId());
        return publisher;
    }

    @Operation(
            summary = "Обновление информации об издательстве",
            description = "Позволяет обновить данные об издателе по его уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PublisherDTO updatePublisher(@RequestBody @Valid PublisherUpdateDTO updateDTO, @PathVariable Long id) {
        log.info("Received request to update publisher with ID: {}", id);
        var updatePublisher = publisherService.updatePublisher(updateDTO, id);
        log.info("Publisher with ID {} updated successfully", id);
        return updatePublisher;
    }

    @Operation(
            summary = "Удаление издательства",
            description = "Позволяет удалить издателя по его уникальному идентификатору. "
                    + "Доступно только для пользователей с ролью ADMIN"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublisher(@PathVariable Long id) {
        log.info("Received request to delete publisher with ID: {}", id);
        publisherService.deletePublisher(id);
        log.info("Publisher with ID {} deleted successfully", id);
    }
}
