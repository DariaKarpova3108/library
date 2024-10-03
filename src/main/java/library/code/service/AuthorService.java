package library.code.service;

import library.code.dto.AuthorDTO.AuthorCreateDTO;
import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.AuthorDTO.AuthorUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.AuthorMapper;
import library.code.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::map)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthor(Long id) {
        var book = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + id + " not found"));
        return authorMapper.map(book);
    }

    public AuthorDTO createAuthor(AuthorCreateDTO createDTO) {
        var author = authorMapper.map(createDTO);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public AuthorDTO updateAuthor(AuthorUpdateDTO updateDTO, Long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + id + " not found"));
        authorMapper.update(updateDTO, author);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}

