package library.code.specification;

import jakarta.persistence.criteria.Order;
import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.models.Reader;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReaderSpecification {
    public Specification<Reader> build(ReaderParamDTO params, Sort sort) {
        Specification<Reader> specification = Specification.where(null);

        specification = specification.and(withFirstNameCont(params.getFirstNameCont())
                .and(withLastNameCont(params.getLastNameCont()))
                .and(withPassportDetailsCont(params.getPassportDetailsCont()))
                .and(withLibraryCardCont(params.getLibraryCardNumberCont()))
                .and(withPhoneCont(params.getPhoneCont())));

        if (sort != null && !sort.isEmpty()) {
            final Specification<Reader> finalSpecification = specification;
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

    private Specification<Reader> withFirstNameCont(String firstNameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (firstNameCont == null || firstNameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                    "%" + firstNameCont.toLowerCase() + "%");
        });
    }

    private Specification<Reader> withLastNameCont(String lastNameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (lastNameCont == null || lastNameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                    "%" + lastNameCont.toLowerCase() + "%");
        });
    }

    private Specification<Reader> withPassportDetailsCont(String passportDetailsCont) {
        return ((root, query, criteriaBuilder) -> {
            if (passportDetailsCont == null || passportDetailsCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("passportDetails")),
                    "%" + passportDetailsCont.toLowerCase() + "%");
        });
    }

    private Specification<Reader> withLibraryCardCont(String libraryCardNumberCont) {
        return ((root, query, criteriaBuilder) -> {
            if (libraryCardNumberCont == null || libraryCardNumberCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("libraryCard").get("cardNumber")),
                    "%" + libraryCardNumberCont.toLowerCase() + "%");
        });
    }

    private Specification<Reader> withPhoneCont(String phoneCont) {
        return ((root, query, criteriaBuilder) -> {
            if (phoneCont == null || phoneCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")),
                    "%" + phoneCont.toLowerCase() + "%");
        });
    }
}
