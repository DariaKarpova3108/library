package library.code.repositories;

import library.code.models.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long> {
    Optional<LibraryCard> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
}
