package library.code.util;

import jakarta.annotation.PostConstruct;
import library.code.models.LibraryCard;
import library.code.models.Reader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class ModelGenerator {

    private final Faker faker;

    private Model<Reader> readerModel;
    private Model<LibraryCard> libraryCardModel;

    @PostConstruct
    public void generateModel() {
        readerModel = Instancio.of(Reader.class)
                .ignore(Select.field(Reader::getId))
                .ignore(Select.field(Reader::getLibraryCard))
                .supply(Select.field(Reader::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(Reader::getLastName), () -> faker.name().lastName())
                .supply(Select.field(Reader::getAge), () -> faker.number().numberBetween(10, 100))
                .supply(Select.field(Reader::getPassportDetails), () -> faker.numerify("##########"))
                .supply(Select.field(Reader::getPhone), () -> faker.numerify("###########"))
                .supply(Select.field(Reader::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(Reader::getAddress), () -> faker.address().fullAddress())
                .toModel();

        libraryCardModel = Instancio.of(LibraryCard.class)
                .ignore(Select.field(LibraryCard::getId))
                .ignore(Select.field(LibraryCard::getReader))
                .ignore(Select.field(LibraryCard::getBorrowedBooks))
                .supply(Select.field(LibraryCard::getCardNumber), () -> faker.numerify("#############"))
                .toModel();
    }
}
