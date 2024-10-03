package library.code.service;

import library.code.dto.LibraryCardDTO.LibraryCardCreateDTO;
import library.code.dto.LibraryCardDTO.LibraryCardDTO;
import library.code.dto.LibraryCardDTO.LibraryCardUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardMapper;
import library.code.repositories.LibraryCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryCardService {

    private final LibraryCardRepository libraryCardRepository;
    private final LibraryCardMapper libraryCardMapper;

    public List<LibraryCardDTO> getAllCards() {
        return libraryCardRepository.findAll().stream()
                .map(libraryCardMapper::map)
                .collect(Collectors.toList());
    }

    public LibraryCardDTO getLibraryCard(Long id) {
        var card = libraryCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with id: " + id + " not found"));
        return libraryCardMapper.map(card);
    }

    public LibraryCardDTO createLibraryCard(LibraryCardCreateDTO createDTO) {
        var card = libraryCardMapper.map(createDTO);
        libraryCardRepository.save(card);
        return libraryCardMapper.map(card);
    }

    public LibraryCardDTO updateLibraryCard(LibraryCardUpdateDTO updateDTO, Long id) {
        var card = libraryCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library card with id: " + id + " not found"));
        libraryCardMapper.update(updateDTO, card);
        libraryCardRepository.save(card);
        return libraryCardMapper.map(card);
    }

    public void deleteLibraryCard(Long id) {
        libraryCardRepository.deleteById(id);
    }
}
