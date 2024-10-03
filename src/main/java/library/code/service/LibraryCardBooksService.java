package library.code.service;

import library.code.dto.LibraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.LibraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardBookMapper;
import library.code.repositories.LibraryCardBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryCardBooksService {
    private final LibraryCardBooksRepository cardBooksRepository;
    private final LibraryCardBookMapper cardBookMapper;


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
}
