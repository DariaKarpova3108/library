package library.code.component;

import jakarta.annotation.PostConstruct;
import library.code.models.Role;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByRoleName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setRoleName(roleName);
                System.out.println("Creating role: " + roleName);
                return roleRepository.save(role);
            });
        }

        var roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
        if (!userRepository.existsByEmail("admin@admin.library")) {
            System.out.println("Creating admin user...");
            User user = new User();
            user.setRole(roleAdmin);
            user.setEmail("admin@admin.library");
            user.setPasswordDigest(passwordEncoder.encode("admin"));
            userRepository.save(user);
        }
    }
}
