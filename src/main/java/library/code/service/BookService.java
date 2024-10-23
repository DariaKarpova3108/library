package library.code.service;

import library.code.dto.bookDTO.BookCreateDTO;
import library.code.dto.bookDTO.BookDTO;
import library.code.dto.bookDTO.BookUpdateDTO;
import library.code.dto.specificationDTO.BookParamDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.BookMapper;
import library.code.repositories.BookRepository;
import library.code.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecification bookSpecification;


    public List<BookDTO> getAllBooks(BookParamDTO params, @RequestParam(defaultValue = "1") int page) {
        var spec = bookSpecification.build(params);
        return bookRepository.findAll(spec, PageRequest.of(page - 1, 10)).stream()
                .map(bookMapper::map)
                .collect(Collectors.toList());
    }

    public BookDTO getBook(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
        return bookMapper.map(book);
    }

    public BookDTO createBook(BookCreateDTO createDTO) {
        var book = bookMapper.map(createDTO);
        bookRepository.save(book);
        return bookMapper.map(book);
    }

    public BookDTO updateBook(BookUpdateDTO updateDTO, Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found"));
        bookMapper.update(updateDTO, book);
        bookRepository.save(book);
        return bookMapper.map(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

