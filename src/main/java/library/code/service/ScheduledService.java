package library.code.service;

import library.code.exception.ResourceNotFoundException;
import library.code.models.LibraryCardBooks;
import library.code.models.NotificationStatusName;
import library.code.repositories.LibraryCardBooksRepository;
import library.code.repositories.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final EmailNotificationService emailNotificationService;
    private final LibraryCardBooksRepository libraryCardBooksRepository;
    private final NotificationStatusRepository notificationStatusRepository;

    @Async("emailTaskExecutor")
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendNotificationsForThreeDays() {
        LocalDate notificationDate = LocalDate.now().plusDays(3);
        List<LibraryCardBooks> listOfBorrowedBooks = libraryCardBooksRepository.findByExpectedReturn(notificationDate);

        if (listOfBorrowedBooks.isEmpty()) {
            log.info("There are no books with return date {}", notificationDate);
        }

        for (var rentalOfBook : listOfBorrowedBooks) {
            try {
                String email = rentalOfBook.getLibraryCard().getReader().getUser().getEmail();
                String subject = "Напоминание о возврате книги";
                String message = String.format("Уважаем %s, срок возврата книги %s истекает %s. "
                                + "Пожалуйста, продлите аренду или верните книгу",
                        rentalOfBook.getLibraryCard().getReader().getFirstName(),
                        rentalOfBook.getBook().getBookTitle(),
                        rentalOfBook.getExpectedReturn()
                );
                emailNotificationService.sendEmail(email, subject, message);

                var notificationStatusSuccess =
                        notificationStatusRepository.findByStatusName(NotificationStatusName.SUCCESS)
                                .orElseThrow(() -> new ResourceNotFoundException("notification status 'success'"
                                        + " not found"));
                rentalOfBook.setNotificationStatus(notificationStatusSuccess);
                libraryCardBooksRepository.save(rentalOfBook);
            } catch (Exception ex) {
                var notificationStatusFailed =
                        notificationStatusRepository.findByStatusName(NotificationStatusName.FAILED)
                                .orElseThrow(() -> new ResourceNotFoundException("notification status 'failed'"
                                        + " not found"));
                rentalOfBook.setNotificationStatus(notificationStatusFailed);
                libraryCardBooksRepository.save(rentalOfBook);
                log.error("Error sending email for book ID {}: {}", rentalOfBook.getBook().getId(), ex.getMessage());
                throw ex;
            }
        }
    }
}
