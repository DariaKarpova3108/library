package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.service.PublisherService;
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
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public ResponseEntity<List<PublisherDTO>> getListPublishers() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(publisherService.getAllPublishers().size()))
                .body(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public PublisherDTO getPublisher(@PathVariable Long id) {
        return publisherService.getPublisher(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PublisherDTO createNewPublisher(@RequestBody @Valid PublisherCreateDTO createDTO) {
        return publisherService.createPublisher(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PublisherDTO updatePublisher(@RequestBody @Valid PublisherUpdateDTO updateDTO, @PathVariable Long id) {
        return publisherService.updatePublisher(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }
}
