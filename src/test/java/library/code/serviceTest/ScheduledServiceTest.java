package library.code.serviceTest;

import library.code.exception.ResourceNotFoundException;
import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.LibraryCard;
import library.code.models.LibraryCardBooks;
import library.code.models.NotificationStatusName;
import library.code.models.Publisher;
import library.code.models.Reader;
import library.code.models.Role;
import library.code.models.RoleName;
import library.code.models.User;
import library.code.repositories.AuthorRepository;
import library.code.repositories.BookRepository;
import library.code.repositories.GenreRepository;
import library.code.repositories.LibraryCardBooksRepository;
import library.code.repositories.LibraryCardRepository;
import library.code.repositories.NotificationStatusRepository;
import library.code.repositories.PublisherRepository;
import library.code.repositories.ReaderRepository;
import library.code.repositories.RoleRepository;
import library.code.repositories.UserRepository;
import library.code.service.EmailNotificationService;
import library.code.service.ScheduledService;
import library.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ScheduledServiceTest {
    @MockBean
    private EmailNotificationService emailNotificationService;
    @MockBean
    private LibraryCardBooksRepository libraryCardBooksRepository;
    @Autowired
    private NotificationStatusRepository notificationStatusRepository;
    @Autowired
    private ScheduledService scheduledService;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryCardRepository libraryCardRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private UserRepository userRepository;
    private LibraryCardBooks libraryCardBooksFromThreeDays;

    @BeforeEach
    public void setUp() {
        Publisher publisher = Instancio.of(modelGenerator.getPublisherModel()).create();
        publisherRepository.save(publisher);

        Genre genre = Instancio.of(modelGenerator.getGenreModel()).create();
        genreRepository.save(genre);

        Author author = Instancio.of(modelGenerator.getAuthorModel()).create();
        authorRepository.save(author);

        Role readerRole = roleRepository.findByRoleName(RoleName.READER)
                .orElseThrow(() -> new RuntimeException("Role READER not found"));

        User user = new User();
        user.setEmail("ivan@example.com");
        user.setPasswordDigest("hashedPassword");
        user.setRole(readerRole);
        userRepository.save(user);

        Reader reader = Instancio.of(modelGenerator.getReaderModel()).create();
        reader.setUser(user);
        readerRepository.save(reader);

        LibraryCard libraryCard = Instancio.of(modelGenerator.getLibraryCardModel()).create();
        libraryCard.setReader(reader);
        libraryCardRepository.save(libraryCard);

        var pendingStatus = notificationStatusRepository.findByStatusName(NotificationStatusName.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("notification status 'pending'"
                        + " not found"));

        LocalDate expectedReturnDate = LocalDate.now().plusDays(3);

        libraryCardBooksFromThreeDays = Instancio.of(modelGenerator.getLibraryCardBooksModel()).create();

        Book book = Instancio.of(modelGenerator.getBookModel()).create();
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGenres(new HashSet<>(Set.of(genre)));
        bookRepository.save(book);

        libraryCardRepository.save(libraryCard);
        libraryCardBooksFromThreeDays.setBook(book);
        libraryCardBooksFromThreeDays.setLibraryCard(libraryCard);
        libraryCardBooksFromThreeDays.setNotificationStatus(pendingStatus);
        libraryCardBooksFromThreeDays.setExpectedReturn(expectedReturnDate);
        libraryCardBooksRepository.save(libraryCardBooksFromThreeDays);
    }

    @Test
    public void testSendNotificationsForThreeDays() {
        LocalDate expectedReturnDate = LocalDate.now().plusDays(3);

        when(libraryCardBooksRepository.findByExpectedReturn(expectedReturnDate))
                .thenReturn(List.of(libraryCardBooksFromThreeDays));

        scheduledService.sendNotificationsForThreeDays();

        verify(emailNotificationService, times(1))
                .sendEmail(eq("ivan@example.com"),
                        eq("Напоминание о возврате книги"),
                        contains(String.format("Уважаем %s, срок возврата книги %s истекает %s. "
                                        + "Пожалуйста, продлите аренду или верните книгу",
                                libraryCardBooksFromThreeDays.getLibraryCard().getReader().getFirstName(),
                                libraryCardBooksFromThreeDays.getBook().getBookTitle(),
                                expectedReturnDate)));

    }
}
