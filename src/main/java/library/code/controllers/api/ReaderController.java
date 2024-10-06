package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<List<ReaderDTO>> getListReaders() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(readerService.getAllReaders().size()))
                .body(readerService.getAllReaders());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO getReader(@PathVariable Long id) {
        return readerService.getReader(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderDTO createNewReader(@RequestBody @Valid ReaderCreateDTO createDTO) {
        return readerService.createReader(createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO updateReader(@RequestBody @Valid ReaderUpdateDTO updateDTO, @PathVariable Long id) {
        return readerService.updateReader(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
    }
}
