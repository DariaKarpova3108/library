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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository adminRepository;
    private final AdministratorMapper administratorMapper;
    private final UserRepository userRepository;

    public AdministratorDTO completeAdminProfile(Long userId, AdminCreateDTO createDTO) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + "not found"));

        if (user.getRole().getRoleName() != RoleName.ADMIN) {
            throw new IllegalArgumentException("User is not a admin");
        }

        if (user.getAdmin() != null) {
            throw new IllegalArgumentException("Admin profile already exists for this user");
        }

        var admin = administratorMapper.map(createDTO);
        admin.setUser(user);
        adminRepository.save(admin);
        return administratorMapper.map(admin);
    }

    public List<AdministratorDTO> getAllAdmin() {
        return adminRepository.findAll().stream()
                .map(administratorMapper::map)
                .collect(Collectors.toList());
    }

    public AdministratorDTO getAdmin(Long id) {
        var admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id: " + id + " not found"));
        return administratorMapper.map(admin);
    }

    public AdministratorDTO updateAdmin(AdminUpdateDTO updateDTO, Long id) {
        var admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id: " + id + " not found"));
        administratorMapper.update(updateDTO, admin);
        adminRepository.save(admin);
        return administratorMapper.map(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}
