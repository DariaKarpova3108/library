package library.code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @ToString.Include
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "Фамилия автора не может быть пустой")
    @ToString.Include
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
