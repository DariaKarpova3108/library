package library.code.specification;

import jakarta.persistence.criteria.Order;
import library.code.dto.specificationDTO.AuthorParamDTO;
import library.code.models.Author;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorSpecification {
    public Specification<Author> build(AuthorParamDTO params, Sort sort) {
        Specification<Author> specification = Specification.where(null);

        specification = specification.and(withNameCont(params.getFirstNameCont())
                .and(withSurname(params.getLastNameCont())));

        if (sort != null && !sort.isEmpty()) {
            final Specification<Author> finalSpecification = specification;
            return ((root, query, criteriaBuilder) -> {
                List<Order> orders = sort.stream()
                        .map(order -> order.isAscending() ? criteriaBuilder.asc(root.get(order.getProperty()))
                                : criteriaBuilder.desc(root.get(order.getProperty())))
                        .collect(Collectors.toList());
                assert query != null;
                query.orderBy(orders);

                return finalSpecification.toPredicate(root, query, criteriaBuilder);
            });
        }
        return specification;
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
