package library.code.service;

import library.code.dto.libraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardBookMapper;
import library.code.models.LibraryCardBooks;
import library.code.repositories.LibraryCardBooksRepository;
import library.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryCardBooksService {
    private final LibraryCardBooksRepository cardBooksRepository;
    private final LibraryCardBookMapper cardBookMapper;

    private final UserRepository userRepository;

    public List<LibraryCardBookDTO> getAllCardBooks() {
        return cardBooksRepository.findAll().stream()
                .map(cardBookMapper::map)
                .collect(Collectors.toList());
    }

    public LibraryCardBookDTO getCardBooks(Long id) {
        var cardBooks = cardBooksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with books with id: "
                        + id + " not found"));
        return cardBookMapper.map(cardBooks);
    }


    public LibraryCardBookDTO createCardBooks(LibraryCardBookCreateDTO createDTO) {
        var cardBooks = cardBookMapper.map(createDTO);
        cardBooksRepository.save(cardBooks);
        LocalDate expectedReturnDate = cardBooks.getBorrowDate().plusWeeks(4);
        cardBooks.setExpectedReturn(expectedReturnDate);
        return cardBookMapper.map(cardBooks);
    }

    public LibraryCardBookDTO updateCardBooks(LibraryCardBookUpdateDTO updateDTO, Long id) {
        var cardBooks = cardBooksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with books with id: "
                        + id + " not found"));
        cardBookMapper.update(updateDTO, cardBooks);
        cardBooksRepository.save(cardBooks);
        return cardBookMapper.map(cardBooks);
    }

    public void deleteCardBooks(Long id) {
        cardBooksRepository.deleteById(id);
    }

    public boolean isReaderOwnerOfBook(Long libraryCardBookId, Long readerId) {
        LibraryCardBooks cardBooks = cardBooksRepository.findById(libraryCardBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with books not found"));
        return cardBooks.getLibraryCard().getReader().getId().equals(readerId);
    }
}
