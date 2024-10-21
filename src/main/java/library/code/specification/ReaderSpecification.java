package library.code.specification;

import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.models.Reader;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReaderSpecification {
    public Specification<Reader> build(ReaderParamDTO params) {
        return withFirstNameCont(params.getFirstNameCont())
                .and(withLastNameCont(params.getLastNameCont()))
                .and(withPassportDetailsCont(params.getPassportDetailsCont()))
                .and(withLibraryCardCont(params.getLibraryCardNumberCont()))
                .and(withPhoneCont(params.getPhoneCont()));
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
