package library.code.component;

import jakarta.annotation.PostConstruct;
import library.code.models.NotificationStatus;
import library.code.models.NotificationStatusName;
import library.code.models.Role;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.NotificationStatusRepository;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationStatusRepository notificationStatusRepository;

    @PostConstruct
    public void init() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByRoleName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setRoleName(roleName);
                log.info("Creating role: {}", roleName);
                return roleRepository.save(role);
            });
        }

        for (NotificationStatusName statusName : NotificationStatusName.values()) {
            notificationStatusRepository.findByStatusName(statusName)
                    .orElseGet(() -> {
                        NotificationStatus status = new NotificationStatus();
                        status.setStatusName(statusName);
                        log.info("Creating notification_status: {}", statusName);
                        return notificationStatusRepository.save(status);
                    });
        }

        var roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> {
                    log.error("Role ADMIN not found");
                    return new RuntimeException("Role ADMIN not found");
                });
        if (!userRepository.existsByEmail("admin@admin.library")) {
            log.info("Creating admin user...");
            User user = new User();
            user.setRole(roleAdmin);
            user.setEmail("admin@admin.library");
            user.setPasswordDigest(passwordEncoder.encode("admin"));
            userRepository.save(user);
        }
    }
}
