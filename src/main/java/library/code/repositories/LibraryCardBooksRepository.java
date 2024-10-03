package library.code.repositories;

import library.code.models.LibraryCardBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryCardBooksRepository extends JpaRepository<LibraryCardBooks, Long> {
}
