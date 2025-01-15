package library.code.service;

import library.code.dto.bookDTO.BookCreateDTO;
import library.code.dto.bookDTO.BookDTO;
import library.code.dto.bookDTO.BookUpdateDTO;
import library.code.dto.specificationDTO.BookParamDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.BookMapper;
import library.code.models.Book;
import library.code.repositories.BookRepository;
import library.code.specification.BookSpecification;
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
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecification bookSpecification;


    public List<BookDTO> getAllBooks(BookParamDTO params, int page, String sort) {
        log.info("Fetching books with parameters: {}, page: {}, sort: {}", params, page, sort);

        String[] sortParams = sort.split(", ");

        if (sortParams.length != 2 || !(sortParams[1].equals("asc") || sortParams[1].equals("desc"))) {
            log.error("Invalid sort direction in parameters: {}", sort);
            throw new IllegalArgumentException("Invalid sort direction");
        }

        Sort sortOrder = Sort.by(sortParams[1].equals("asc") ? Sort.Order.asc(sortParams[0].trim())
                : Sort.Order.desc(sortParams[0].trim()));

        Specification<Book> spec = bookSpecification.build(params, sortOrder);
        Pageable pageable = PageRequest.of(page - 1, 10, sortOrder);

        var books = bookRepository.findAll(spec, pageable).stream()
                .map(bookMapper::map)
                .collect(Collectors.toList());

        log.info("Retrieved {} books", books.size());
        return books;
    }

    public BookDTO getBook(Long id) {
        log.info("Fetching book with ID: {}", id);

        var book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new ResourceNotFoundException("Book with ID: " + id + " not found");
                });

        log.info("Successfully fetched book with ID: {}", id);
        return bookMapper.map(book);
    }

    public BookDTO createBook(BookCreateDTO createDTO) {
        log.info("Creating new book with data: {}", createDTO);

        var book = bookMapper.map(createDTO);
        bookRepository.save(book);

        log.info("Successfully created book with ID: {}", book.getId());
        return bookMapper.map(book);
    }

    public BookDTO updateBook(BookUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update book with ID: {}", id);

        var book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new ResourceNotFoundException("Book with ID: " + id + " not found");
                });
        bookMapper.update(updateDTO, book);
        bookRepository.save(book);

        log.info("Successfully updated book with ID: {}", id);
        return bookMapper.map(book);
    }

    public void deleteBook(Long id) {
        log.info("Attempting to delete book with ID: {}", id);
        bookRepository.deleteById(id);
        log.info("Successfully deleted book with ID: {}", id);
    }
}

