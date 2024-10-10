package library.code.specification;

import library.code.dto.specificationDTO.AuthorParamDTO;
import library.code.models.Author;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecification {

    public Specification<Author> build(AuthorParamDTO params) {
        return withNameCont(params.getFirstNameCont())
                .and(withSurname(params.getLastNameCont()));
    }

    private Specification<Author> withNameCont(String firstNameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (firstNameCont == null || firstNameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                    "%" + firstNameCont.toLowerCase() + "%");
        });
    }

    private Specification<Author> withSurname(String lastNameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (lastNameCont == null || lastNameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                    "%" + lastNameCont.toLowerCase() + "%");
        });
    }
}
