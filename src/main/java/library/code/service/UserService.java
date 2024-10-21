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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }

    public boolean isAdmin(User user) {
        return user.getEmail().endsWith("@admin.library");
    }

    public UserDTO createNewUser(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        if (!userExists(userCreateDTO.getEmail())) {
            if (isAdmin(user)) {
                var roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
                user.setRole(roleAdmin);
            } else {
                var readerRole = roleRepository.findByRoleName(RoleName.READER)
                        .orElseThrow(() -> new ResourceNotFoundException("Reader role not found"));
                user.setRole(readerRole);
            }
            userRepository.save(user);
            return userMapper.map(user);
        } else {
            throw new ResourceAlreadyExistsException("The resource you are trying to create already exists");
        }
    }

    public UserDTO getUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not with id: " + id + " not found"));
        return userMapper.map(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO userUpdate(UserUpdateDTO updateDTO, Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not with id: " + id + " not found"));
        userMapper.update(updateDTO, user);
        userRepository.save(user);
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
        return userRepository.existsByEmail(email);
    }
}
