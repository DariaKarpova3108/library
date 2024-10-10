package library.code.controllers.api;

import library.code.models.LibraryCard;
import library.code.models.Reader;
import library.code.repositories.LibraryCardRepository;
import library.code.repositories.ReaderRepository;
import library.code.util.ModelGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ReaderControllerTestWithParams {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @BeforeEach
    public void setUp() {
        var reader = new Reader();
        reader.setFirstName("Masha");
        reader.setLastName("Ivanova");
        reader.setPassportDetails("1230007779");
        reader.setEmail("masha@example.com");
        reader.setPhone("89007896769");
        reader.setAddress("Moscow, street Rad Street, h.1");
        reader.setAge(23);
        readerRepository.save(reader);
        var libraryCard = new LibraryCard();
        libraryCard.setCardNumber("0000000000011");
        libraryCard.setReader(reader);
        reader.setLibraryCard(libraryCard);
        libraryCardRepository.save(libraryCard);

        var reader2 = new Reader();
        reader2.setFirstName("Ivan");
        reader2.setLastName("Testov");
        reader2.setPassportDetails("2347009978");
        reader2.setEmail("IvaN@example.ru");
        reader2.setAge(25);
        reader2.setPhone("89117890054");
        reader2.setAddress("Moscow, street Blue Street, h.8");
        readerRepository.save(reader2);
        var libraryCard2 = new LibraryCard();
        libraryCard2.setCardNumber("1100000000123");
        libraryCard2.setReader(reader2);
        reader2.setLibraryCard(libraryCard2);
        libraryCardRepository.save(libraryCard2);
    }

    @Test
    public void testFilterWithFirstNameCont() throws Exception {
        var request = get("/api/readers?firstNameCont=mash");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("first_name").asString().containsIgnoringCase("mash")));
    }

    @Test
    public void testFilterWithLastNameCont() throws Exception {
        var request = get("/api/readers?lastNameCont=testov");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("last_name").asString().containsIgnoringCase("testov")));
    }

    @Test
    public void testFilterPassportDetails() throws Exception {
        var request = get("/api/readers?passportDetailsCont=700997");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("passport_details").asString().containsIgnoringCase("700997")));
    }

    @Test
    public void testFilterLibraryCardNumberCont() throws Exception {
        var request = get("/api/readers?libraryCardNumberCont=0000");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(2)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("library_card_number").asString().containsIgnoringCase("0000")));
    }

    @Test
    public void testFilterPhoneCont() throws Exception {
        var request = get("/api/readers?phoneCont=8900");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray()
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("phone").asString().containsIgnoringCase("8900")));
    }

    @Test
    public void testFilterEmailCont() throws Exception {
        var request = get("/api/readers?emailCont=ivan@example");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("email").asString().containsIgnoringCase("ivan@example")));
    }

    @Test
    public void testAllFilter() throws Exception {
        var request = get("/api/readers")
                .param("firstNameCont", "mash")
                .param("lastNameCont", "ivano")
                .param("passportDetailsCont", "1230")
                .param("libraryCardNumberCont", "000000")
                .param("phoneCont", "8900")
                .param("emailCont", "masha@example");

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThat(body).isNotNull();
        assertThatJson(body).isArray().hasSize(1)
                .anySatisfy(element -> assertThatJson(element)
                        .and(n -> n.node("first_name").asString().containsIgnoringCase("Masha"))
                        .and(n -> n.node("last_name").asString().containsIgnoringCase("Ivanova"))
                        .and(n -> n.node("passport_details").asString().containsIgnoringCase("1230"))
                        .and(n -> n.node("library_card_number").asString().containsIgnoringCase("000000"))
                        .and(n -> n.node("phone").asString().containsIgnoringCase("8900"))
                        .and(n -> n.node("email").asString().containsIgnoringCase("masha@example.com")));
    }
}
