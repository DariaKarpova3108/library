package library.code.service;

import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.GenreMapper;
import library.code.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreDTO> getAllGenres() {
        log.info("Fetching all genres");

        var genres = genreRepository.findAll().stream()
                .map(genreMapper::map)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} genres", genres.size());
        return genres;
    }

    public GenreDTO getGenre(Long id) {
        log.info("Fetching genre with ID: {}", id);

        var genre = genreRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Genre with ID: {} not found", id);
                    return new ResourceNotFoundException("Genre with ID: " + id + " not found");
                });

        log.info("Successfully fetched genre with ID: {}", id);
        return genreMapper.map(genre);
    }

    public GenreDTO createGenre(GenreCreateDTO createDTO) {
        log.info("Creating new genre with data: {}", createDTO);

        var genre = genreMapper.map(createDTO);
        genreRepository.save(genre);

        log.info("Successfully created genre with ID: {}", genre.getId());
        return genreMapper.map(genre);
    }

    public GenreDTO updateGenre(GenreUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update genre with ID: {}", id);

        var genre = genreRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Genre with ID: {} not found", id);
                    return new ResourceNotFoundException("Genre with ID: " + id + " not found");
                });

        genreMapper.update(updateDTO, genre);
        genreRepository.save(genre);
        log.info("Successfully updated genre with ID: {}", id);
        return genreMapper.map(genre);
    }

    public void deleteGenre(Long id) {
        log.info("Attempting to delete genre with ID: {}", id);
        genreRepository.deleteById(id);
        log.info("Successfully deleted genre with ID: {}", id);
    }
}

