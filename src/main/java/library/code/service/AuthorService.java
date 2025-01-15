package library.code.service;

import library.code.dto.authorDTO.AuthorCreateDTO;
import library.code.dto.authorDTO.AuthorDTO;
import library.code.dto.authorDTO.AuthorUpdateDTO;
import library.code.dto.specificationDTO.AuthorParamDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.AuthorMapper;
import library.code.models.Author;
import library.code.repositories.AuthorRepository;
import library.code.specification.AuthorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final AuthorSpecification authorSpecification;

    public List<AuthorDTO> getAllAuthors(AuthorParamDTO params, int page, String sort) {
        log.info("Fetching authors with parameters: {}, page: {}, sort: {}", params, page, sort);

        String[] sortParams = sort.split(", ");
        if (sortParams.length != 2 || !(sortParams[1].equals("asc") || sortParams[1].equals("desc"))) {
            log.error("Invalid sort direction in parameters: {}", sort);
            throw new IllegalArgumentException("Invalid sort direction");
        }

        Sort sortOrder = Sort.by(sortParams[1].equals("asc") ? Sort.Order.asc(sortParams[0].trim())
                : Sort.Order.desc(sortParams[0].trim()));

        Specification<Author> spec = authorSpecification.build(params, sortOrder);
        Pageable pageable = PageRequest.of(page - 1, 10, sortOrder);
        var authors = authorRepository.findAll(spec, pageable).stream()
                .map(authorMapper::map)
                .collect(Collectors.toList());

        log.info("Retrieved {} authors", authors.size());
        return authors;
    }

    public AuthorDTO getAuthor(Long id) {
        log.info("Fetching author with ID: {}", id);

        var book = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author with ID {} not found", id);
                    return new ResourceNotFoundException("Author with ID: " + id + " not found");
                });

        log.info("Successfully fetched author with ID: {}", id);
        return authorMapper.map(book);
    }

    public AuthorDTO createAuthor(AuthorCreateDTO createDTO) {
        log.info("Creating new author with data: {}", createDTO);

        var author = authorMapper.map(createDTO);
        authorRepository.save(author);

        log.info("Successfully created author with ID: {}", author.getId());
        return authorMapper.map(author);
    }

    public AuthorDTO updateAuthor(AuthorUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update author with ID: {}", id);

        var author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author with ID {} not found", id);
                    return new ResourceNotFoundException("Author with ID: " + id + " not found");
                });

        authorMapper.update(updateDTO, author);
        authorRepository.save(author);

        log.info("Successfully updated author with ID: {}", id);
        return authorMapper.map(author);
    }

    public void deleteAuthor(Long id) {
        log.info("Attempting to delete author with ID: {}", id);
        authorRepository.deleteById(id);
        log.info("Successfully deleted author with ID: {}", id);
    }
}

