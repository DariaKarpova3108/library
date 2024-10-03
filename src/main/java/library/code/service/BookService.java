package library.code.service;

import library.code.dto.BookDTO.BookCreateDTO;
import library.code.dto.BookDTO.BookDTO;
import library.code.dto.BookDTO.BookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.BookMapper;
import library.code.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
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

