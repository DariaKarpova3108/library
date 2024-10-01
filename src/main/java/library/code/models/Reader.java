package library.code.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "readers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty(message = "Имя не может быть пустым")
    @Size(max = 50)
    @ToString.Include
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "Фамилия не может быть пустой")
    @Size(max = 50)
    @ToString.Include
    @Column(name = "last_name", nullable = false)
    private String lastName;


    @NotEmpty(message = "Номер паспорта не может быть пустым")
    @Pattern(regexp = "^[0-9]{10}$")
    @ToString.Include
    @Column(unique = true, name = "passport_details", nullable = false)
    private String passportDetails;

    @Min(10)
    @Max(100)
    @Column(name = "age")
    private int age;

    @Pattern(regexp = "\\+[0-9]{11}")
    @ToString.Include
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Email
    @ToString.Include
    @Column(name = "email", nullable = false)
    private String email;

    @NotEmpty(message = "Адрес должен содержать место фактического проживания: ул., дом, кв.")
    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate updatedAt;
}
