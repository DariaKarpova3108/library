package library.code.service;

import library.code.dto.administatorDTO.AdminCreateDTO;
import library.code.dto.administatorDTO.AdminUpdateDTO;
import library.code.dto.administatorDTO.AdministratorDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.AdministratorMapper;
import library.code.models.RoleName;
import library.code.repositories.AdministratorRepository;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministratorService {

    private final AdministratorRepository adminRepository;
    private final AdministratorMapper administratorMapper;
    private final UserRepository userRepository;

    public AdministratorDTO completeAdminProfile(Long userId, AdminCreateDTO createDTO) {
        log.info("Attempting to complete admin profile for user with ID: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("User with id: " + userId + "not found");
                });

        if (user.getRole().getRoleName() != RoleName.ADMIN) {
            log.error("User with ID {} is not an admin", userId);
            throw new IllegalArgumentException("User is not a admin");
        }

        if (user.getAdmin() != null) {
            log.error("Admin profile already exists for user with ID: {}", userId);
            throw new IllegalArgumentException("Admin profile already exists for this user");
        }

        var admin = administratorMapper.map(createDTO);
        admin.setUser(user);
        adminRepository.save(admin);

        log.info("Successfully completed admin profile for user with ID: {}", userId);
        return administratorMapper.map(admin);
    }

    public List<AdministratorDTO> getAllAdmin() {
        log.info("Fetching all administrators");

        List<AdministratorDTO> admins = adminRepository.findAll().stream()
                .map(administratorMapper::map)
                .collect(Collectors.toList());

        log.info("Retrieved {} administrators", admins.size());
        return admins;
    }

    public AdministratorDTO getAdmin(Long id) {
        log.info("Fetching administrator with ID: {}", id);

        var admin = adminRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Admin with ID {} not found", id);
                    return new ResourceNotFoundException("Admin with ID: " + id + " not found");
                });

        log.info("Successfully fetched administrator with ID: {}", id);
        return administratorMapper.map(admin);
    }

    public AdministratorDTO updateAdmin(AdminUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update admin with ID: {}", id);

        var admin = adminRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Admin with ID {} not found", id);
                    return new ResourceNotFoundException("Admin with ID: " + id + " not found");
                });

        administratorMapper.update(updateDTO, admin);
        adminRepository.save(admin);

        log.info("Successfully updated admin with ID: {}", id);

        return administratorMapper.map(admin);
    }

    public void deleteAdmin(Long id) {
        log.info("Attempting to delete admin with ID: {}", id);
        adminRepository.deleteById(id);
        log.info("Successfully deleted admin with ID: {}", id);
    }
}
