package library.code.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import library.code.dto.userDTO.UserCreateDTO;
import library.code.dto.userDTO.UserDTO;
import library.code.dto.userDTO.UserUpdateDTO;
import library.code.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер управления пользователями",
        description = "Позволяет выполнять CRUD операции с пользователями, "
                + "включая регистрацию, обновление, получение и удаление профилей")

public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя с переданными данными. "
                    + "Возвращает данные о созданном пользователе"
    )
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        log.info("Received request to register a new user with data: {}", createDTO);
        var newUser = userService.createNewUser(createDTO);
        log.info("New user registered successfully with ID: {}", newUser.getId());
        return newUser;
    }

    @Operation(
            summary = "Получение информации о пользователе",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого пользователя"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public UserDTO getUser(@PathVariable Long id) {
        log.info("Received request to fetch user with ID: {}", id);
        var user = userService.getUser(id);
        log.info("User with ID {} found", id);
        return user;
    }

    @Operation(
            summary = "Обновление данных пользователя",
            description = "Позволяет обновить данные пользователя по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого пользователя"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO updateDTO) {
        log.info("Received request to update user with ID: {}", id);
        var updatedUser = userService.userUpdate(updateDTO, id);
        log.info("User with ID {} updated successfully", id);
        return updatedUser;
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Позволяет удалить пользователя по его уникальному идентификатору. "
                    + "Доступно для пользователей с ролью ADMIN или для самого пользователя"
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public void deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteUserById(id);
        log.info("User with ID {} deleted successfully", id);
    }
}
