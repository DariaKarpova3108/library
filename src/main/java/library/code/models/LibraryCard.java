package library.code.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "library_cards")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LibraryCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

  /*  @ManyToMany(

    )*/
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "id", nullable = false)
    private Reader reader;

    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ToString.Include
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ToString.Include
    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturn;


    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ToString.Include
    @Column(name = "actual_return_date")
    private LocalDate actualDate;

    @Pattern(regexp = "[0-9]{13}")
    @ToString.Include
    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;
}
