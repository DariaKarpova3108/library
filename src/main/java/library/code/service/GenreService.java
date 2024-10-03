package library.code.service;

import library.code.dto.genreDTO.GenreCreateDTO;
import library.code.dto.genreDTO.GenreDTO;
import library.code.dto.genreDTO.GenreUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.GenreMapper;
import library.code.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;


    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::map)
                .collect(Collectors.toList());
    }

    public GenreDTO getGenre(Long id) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with id: " + id + " not found"));
        return genreMapper.map(genre);
    }

    public GenreDTO createGenre(GenreCreateDTO createDTO) {
        var genre = genreMapper.map(createDTO);
        genreRepository.save(genre);
        return genreMapper.map(genre);
    }

    public GenreDTO updateGenre(GenreUpdateDTO updateDTO, Long id) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with id: " + id + " not found"));
        genreMapper.update(updateDTO, genre);
        genreRepository.save(genre);
        return genreMapper.map(genre);
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}

