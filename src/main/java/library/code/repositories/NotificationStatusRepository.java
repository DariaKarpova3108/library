package library.code.repositories;

import library.code.models.NotificationStatus;
import library.code.models.NotificationStatusName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    Optional<NotificationStatus> findByStatusName(NotificationStatusName statusName);
}
