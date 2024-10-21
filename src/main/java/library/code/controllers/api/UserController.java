package library.code.controllers.api;

import jakarta.validation.Valid;
import library.code.dto.userDTO.UserCreateDTO;
import library.code.dto.userDTO.UserDTO;
import library.code.dto.userDTO.UserUpdateDTO;
import library.code.service.UserService;
import lombok.RequiredArgsConstructor;
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
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    public UserDTO createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        return userService.createNewUser(createDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO updateDTO) {
        return userService.userUpdate(updateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @userUtils.checkCurrentUser(#id)")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
