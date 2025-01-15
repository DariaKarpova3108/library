package library.code.service;

import library.code.dto.userDTO.UserCreateDTO;
import library.code.dto.userDTO.UserDTO;
import library.code.dto.userDTO.UserUpdateDTO;
import library.code.exception.ResourceAlreadyExistsException;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.UserMapper;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email: {} not found", email);
                    return new UsernameNotFoundException("User with email: " + email + " not found");
                });
    }

    public boolean isAdmin(User user) {
        boolean isAdmin = user.getEmail().endsWith("@admin.library");
        log.debug("Checking if user {} is admin: {}", user.getEmail(), isAdmin);
        return isAdmin;
    }

    public UserDTO createNewUser(UserCreateDTO userCreateDTO) {
        log.info("Attempting to create new user with email: {}", userCreateDTO.getEmail());

        var user = userMapper.map(userCreateDTO);

        if (!userExists(userCreateDTO.getEmail())) {
            if (isAdmin(user)) {
                log.info("Assigning 'ADMIN' role to user with email: {}", userCreateDTO.getEmail());
                var roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
                user.setRole(roleAdmin);
            } else {
                log.info("Assigning 'READER' role to user with email: {}", userCreateDTO.getEmail());
                var readerRole = roleRepository.findByRoleName(RoleName.READER)
                        .orElseThrow(() -> new ResourceNotFoundException("Reader role not found"));
                user.setRole(readerRole);
            }

            log.info("Successfully created user with email: {}", userCreateDTO.getEmail());
            userRepository.save(user);
            return userMapper.map(user);

        } else {
            log.error("User with email: {} already exists", userCreateDTO.getEmail());
            throw new ResourceAlreadyExistsException("The resource you are trying to create already exists");
        }
    }

    public UserDTO getUser(Long id) {
        log.info("Fetching user with ID: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", id);
                    return new ResourceNotFoundException("User not with id: " + id + " not found");
                });
        log.info("Successfully fetched user with ID: {}", id);
        return userMapper.map(user);
    }

    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }

    public UserDTO userUpdate(UserUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update user with ID: {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", id);
                    return new ResourceNotFoundException("User not with id: " + id + " not found");
                });

        userMapper.update(updateDTO, user);
        userRepository.save(user);
        log.info("Successfully updated user with ID: {}", id);
        return userMapper.map(user);
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String email) {
        log.debug("Checking if user with email: {} exists", email);
        return userRepository.existsByEmail(email);
    }
}
