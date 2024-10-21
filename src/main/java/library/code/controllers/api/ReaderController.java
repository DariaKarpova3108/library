package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.LibraryCardDTO.LibraryCardDTO;
import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.service.ReaderService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReaderDTO>> getListReadersProfiles(ReaderParamDTO params,
                                                                  @RequestParam(defaultValue = "1") int page) {
        var result = readerService.getAllReaders(params, page);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public ReaderDTO getReaderProfile(@PathVariable Long id) {
        return readerService.getReader(id);
    }


    @GetMapping("/{readerId}/libraryCard")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#readerId)")
    public LibraryCardDTO getLibraryCard(@PathVariable Long readerId) {
        return readerService.getLibraryCardByReaderId(readerId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#userId)")
    public ReaderDTO completeReaderProfile(@PathVariable Long userId, @RequestBody @Valid ReaderCreateDTO createDTO) {
        return readerService.completeReaderProfile(userId, createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public ReaderDTO updateReaderProfile(@RequestBody @Valid ReaderUpdateDTO updateDTO, @PathVariable Long id) {
        return readerService.updateReader(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @readerUtils.checkCurrentReader(#id)")
    public void deleteReaderProfile(@PathVariable Long id) {
        readerService.deleteReader(id);
    }
}
