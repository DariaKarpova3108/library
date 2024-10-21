package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.administatorDTO.AdminCreateDTO;
import library.code.dto.administatorDTO.AdminUpdateDTO;
import library.code.dto.administatorDTO.AdministratorDTO;
import library.code.service.AdministratorService;
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
@RequestMapping("/api/administrations")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdministratorDTO>> getAllAdmins() {
        return ResponseEntity.ok()
                .header("X-Total_count", String.valueOf(administratorService.getAllAdmin().size()))
                .body(administratorService.getAllAdmin());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO getAdmin(@PathVariable Long id) {
        return administratorService.getAdmin(id);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO completeAdminProfile(@PathVariable Long userId,
                                                 @RequestBody @Valid AdminCreateDTO createDTO) {
        return administratorService.completeAdminProfile(userId, createDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO updateAdmin(@PathVariable Long id,
                                        @RequestBody @Valid AdminUpdateDTO updateDTO) {
        return administratorService.updateAdmin(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAdmin(@PathVariable Long id) {
        administratorService.deleteAdmin(id);
    }
}
