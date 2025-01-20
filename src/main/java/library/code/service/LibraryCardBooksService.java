package library.code.service;

import library.code.dto.libraryCardBooksDTO.LibraryCardBookCreateDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookDTO;
import library.code.dto.libraryCardBooksDTO.LibraryCardBookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardBookMapper;
import library.code.models.LibraryCardBooks;
import library.code.models.NotificationStatus;
import library.code.models.NotificationStatusName;
import library.code.repositories.LibraryCardBooksRepository;
import library.code.repositories.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryCardBooksService {
    private final LibraryCardBooksRepository cardBooksRepository;
    private final LibraryCardBookMapper cardBookMapper;
    private final NotificationStatusRepository notificationStatusRepository;

    public List<LibraryCardBookDTO> getAllBooksInCardBooks(Long libraryCardId) {
        log.info("Fetching all books in library card books");
        var borrowedBooks = cardBooksRepository.findByLibraryCardId(libraryCardId).stream()
                .map(cardBookMapper::map)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} all books in library card books", borrowedBooks.size());
        return borrowedBooks;
    }

    public LibraryCardBookDTO getBooksInCardBooks(Long id) {
        log.info("Fetching the book in library card book with ID: {}", id);

        var cardBooks = cardBooksRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("The book in library card book with ID: {} not found", id);
                    return new ResourceNotFoundException("The book with ID: "
                            + id + "in library card not found");
                });

        log.info("Successfully fetched the book with ID: {} from library card", id);
        return cardBookMapper.map(cardBooks);
    }


    public LibraryCardBookDTO createNewRecordInCardBooks(LibraryCardBookCreateDTO createDTO) {
        log.info("Creating a new record about book in a library data card: {}", createDTO);
        var cardBooks = cardBookMapper.map(createDTO);

        LocalDate expectedReturnDate = cardBooks.getBorrowDate().plusWeeks(4);
        NotificationStatus status = notificationStatusRepository.findByStatusName(NotificationStatusName.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("notification status 'pending'"
                        + " not found"));

        cardBooks.setExpectedReturn(expectedReturnDate);
        cardBooks.setNotificationStatus(status);
        cardBooksRepository.save(cardBooks);

        log.info("The record library card was successfully created with an expected return date: {}",
                expectedReturnDate);
        return cardBookMapper.map(cardBooks);
    }

    public LibraryCardBookDTO updateRecordInCardBooks(LibraryCardBookUpdateDTO updateDTO, Long id) {
        log.info("Attempting to update the record about book in library card book with ID: {}", id);

        var cardBooks = cardBooksRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("The book in library card book with ID: {} not found for update", id);
                    return new ResourceNotFoundException("Library card with books with ID: "
                            + id + " not found");
                });

        cardBookMapper.update(updateDTO, cardBooks);
        cardBooksRepository.save(cardBooks);

        log.info("Successfully updated the record in library card book with ID: {}", id);
        return cardBookMapper.map(cardBooks);
    }

    public void deleteBookFromCardBooks(Long id) {
        log.info("Attempting to delete the record about book in library card book with ID: {}", id);
        cardBooksRepository.deleteById(id);
        log.info("Successfully deleted the record in library card book with ID: {}", id);
    }

    public boolean isReaderOwnerOfBook(Long libraryCardBookId, Long readerId) {
        log.info("Checking if reader with ID: {} is owner of library card book with ID: {}",
                readerId, libraryCardBookId);

        LibraryCardBooks cardBooks = cardBooksRepository.findById(libraryCardBookId)
                .orElseThrow(() -> {
                    log.error("Library card book with ID: {} not found", libraryCardBookId);
                    return new ResourceNotFoundException("Library card with books not found");
                });
        boolean isOwner = cardBooks.getLibraryCard().getReader().getId().equals(readerId);

        if (isOwner) {
            log.info("Reader with ID: {} is the owner of library card book with ID: {}",
                    readerId, libraryCardBookId);
        } else {
            log.warn("Reader with ID: {} is NOT the owner of library card book with ID: {}",
                    readerId, libraryCardBookId);
        }

        return isOwner;
    }
}
