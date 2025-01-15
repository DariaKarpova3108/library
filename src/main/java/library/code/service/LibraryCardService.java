package library.code.service;

import library.code.dto.libraryCardDTO.LibraryCardDTO;
import library.code.dto.libraryCardDTO.LibraryCardUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardMapper;
import library.code.models.LibraryCard;
import library.code.models.Reader;
import library.code.repositories.LibraryCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryCardService {

    private final LibraryCardRepository libraryCardRepository;
    private final LibraryCardMapper libraryCardMapper;

    public List<LibraryCardDTO> getAllCards() {
        log.info("Fetching all library cards");
        var libraryCards =  libraryCardRepository.findAll().stream()
                .map(libraryCardMapper::map)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} library cards", libraryCards.size());
        return libraryCards;
    }

    public LibraryCardDTO getLibraryCard(Long id) {
        log.info("Fetching library card with ID: {}", id);

        var card = libraryCardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Library card with ID: {} not found", id);
                    return new ResourceNotFoundException("Library card with ID: " + id + " not found");
                });

        log.info("Successfully fetched library card with ID: {}", id);
        return libraryCardMapper.map(card);
    }

    public LibraryCard createLibraryNumberCard(Reader reader) {
        log.info("Creating a new library card for reader with ID: {}", reader.getId());
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setCardNumber(generateUniqueNumberCard());
        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);

        log.info("Successfully created new library card with number: {}", libraryCard.getCardNumber());
        return libraryCard;
    }

    private String generateUniqueNumberCard() {
        log.debug("Generating unique library card number");
        Random random = new Random();
        StringBuilder numberCard;
        do {
            numberCard = new StringBuilder();
            for (int i = 0; i < 13; i++) {
                numberCard.append(random.nextInt(0, 10));
            }
        } while (libraryCardRepository.existsByCardNumber(numberCard.toString()));

        log.debug("Generated unique library card number: {}", numberCard);
        return numberCard.toString();
    }

    public LibraryCardDTO updateLibraryCard(LibraryCardUpdateDTO updateDTO, Long id) {
        log.info("Updating library card with ID: {}", id);

        var card = libraryCardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Library card with ID: {} not found for update", id);
                    return new ResourceNotFoundException("Library card with ID: " + id + " not found");
                });

        libraryCardMapper.update(updateDTO, card);
        libraryCardRepository.save(card);

        log.info("Successfully updated library card with ID: {}", id);
        return libraryCardMapper.map(card);
    }

    public void deleteLibraryCard(Long id) {
        log.info("Attempting to delete library card with ID: {}", id);
        libraryCardRepository.deleteById(id);
        log.info("Successfully deleted library card with ID: {}", id);
    }
}
