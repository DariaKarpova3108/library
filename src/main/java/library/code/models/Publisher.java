package library.code.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "publishers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @ToString.Include
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @ToString.Include
    @Column(name = "address", nullable = false)
    private String address;


    @Pattern(regexp = "\\+[0-9]{11}")
    @ToString.Include
    @Column(name = "phone", unique = true)
    private String phone;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books;
}
