package library.code.repositories;

import library.code.models.LibraryCardBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LibraryCardBooksRepository extends JpaRepository<LibraryCardBooks, Long> {
    List<LibraryCardBooks> findByLibraryCardId(Long id);

    List<LibraryCardBooks> findByExpectedReturn(LocalDate localDate);
}
