package library.code.repositories;

import library.code.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByTypeOfGenre(String typeOfGenre);
}
