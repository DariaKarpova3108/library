package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.administatorDTO.AdminCreateDTO;
import library.code.dto.administatorDTO.AdminUpdateDTO;
import library.code.dto.administatorDTO.AdministratorDTO;
import library.code.service.AdministratorService;
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
@RequestMapping("/api/administrations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления администраторами",
        description = "Позволяет проводить CRUD операции с администраторами")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Operation(summary = "Получить всех администраторов",
            description = "Возвращает список всех администраторов. "
                    + "Доступно только пользователям с ролью ADMIN")
    @SecurityRequirement(name = "JWT")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdministratorDTO>> getAllAdmins() {
        log.info("Request to get all administrators received");
        var admins = administratorService.getAllAdmin();
        log.info("Successfully retrieved {} administrators", admins.size());
        return ResponseEntity.ok()
                .header("X-Total_count", String.valueOf(admins.size()))
                .body(admins);
    }


    @Operation(summary = "Получить администратора по ID",
            description = "Возвращает подробную информацию об администраторе по его ID. "
                    + "Доступно только пользователям с ролью ADMIN")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO getAdmin(@PathVariable Long id) {
        log.info("Request to get administrator with ID: {}", id);
        var admin = administratorService.getAdmin(id);
        log.info("Successfully retrieved administrator with ID: {}", id);
        return admin;
    }

    @Operation(summary = "Заполнение профиля администратора",
            description = "Заполняет профиль администратора для пользователя с указанным ID. "
                    + "Доступно только пользователям с ролью ADMIN")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO completeAdminProfile(@PathVariable Long userId,
                                                 @RequestBody @Valid AdminCreateDTO createDTO) {
        log.info("Request to complete admin profile for user with ID: {}", userId);
        var adminProfile = administratorService.completeAdminProfile(userId, createDTO);
        log.info("Successfully completed profile for user with ID: {}", userId);
        return adminProfile;
    }

    @Operation(summary = "Обновить информацию о администраторе",
            description = "Обновляет информацию об администраторе по его ID. "
                    + "Доступно только пользователю с ролью ADMIN и пользователю чей ID совпадает с запрашиваемым")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') and @adminUtils.checkCurrentAdmin(#id)")
    public AdministratorDTO updateAdmin(@PathVariable Long id,
                                        @RequestBody @Valid AdminUpdateDTO updateDTO) {
        log.info("Request to update administrator with ID: {}", id);
        var adminUpdate = administratorService.updateAdmin(updateDTO, id);
        log.info("Successfully updated administrator with ID: {}", id);
        return adminUpdate;
    }

    @Operation(summary = "Удалить администратора",
            description = "Удаляет администратора по его ID. "
                    + "Доступно только пользователям с ролью ADMIN и пользователю чей ID совпадает с запрашиваемым")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') and @adminUtils.checkCurrentAdmin(#id)")
    public void deleteAdmin(@PathVariable Long id) {
        log.info("Request to delete administrator with ID: {}", id);
        administratorService.deleteAdmin(id);
        log.info("Successfully deleted administrator with ID: {}", id);
    }
}
