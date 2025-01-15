package library.code.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import library.code.dto.specificationDTO.BookParamDTO;
import library.code.models.Book;
import library.code.models.Genre;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookSpecification {
    public Specification<Book> build(BookParamDTO params, Sort sort) {
        Specification<Book> specification = Specification.where(null);

        specification = specification
                .and(withBookTitleCont(params.getBookCont())
                        .and(withContAuthorFirstName(params.getAuthorFirstNameCont()))
                        .and(withContAuthorLastName(params.getAuthorSurnameCont()))
                        .and(withPublisherTitleCont(params.getPublisherTitleCont()))
                        .and(withContGenreTypes(params.getGenreTypes()))
                        .and(withDirectionOfLiterature(params.getDirectionOfLiterature())));

        if (sort != null && !sort.isEmpty()) {
            final Specification<Book> finalSpecification = specification;
            return ((root, query, criteriaBuilder) -> {
                List<Order> orders = sort.stream()
                        .map(order -> order.isAscending()
                                ? criteriaBuilder.asc(root.get(order.getProperty()))
                                : criteriaBuilder.desc(root.get(order.getProperty())))
                        .collect(Collectors.toList());
                assert query != null;
                query.orderBy(orders);
                return finalSpecification.toPredicate(root, query, criteriaBuilder);
            });
        }
        return specification;
    }

    private Specification<Book> withBookTitleCont(String bookTitleCont) {
        return ((root, query, criteriaBuilder) -> {
            if (bookTitleCont == null || bookTitleCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("bookTitle")), "%"
                    + bookTitleCont.toLowerCase() + "%"
            );
        });
    }

    private Specification<Book> withContAuthorFirstName(String authorFirstNameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (authorFirstNameCont == null || authorFirstNameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("firstName")),
                    "%" + authorFirstNameCont.toLowerCase() + "%");
        });
    }

    private Specification<Book> withContAuthorLastName(String authorSurnameCont) {
        return ((root, query, criteriaBuilder) -> {
            if (authorSurnameCont == null || authorSurnameCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("lastName")),
                    "%" + authorSurnameCont.toLowerCase() + "%");
        });
    }

    private Specification<Book> withPublisherTitleCont(String publisherTitleCont) {
        return ((root, query, criteriaBuilder) -> {
            if (publisherTitleCont == null || publisherTitleCont.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher").get("title")),
                    "%" + publisherTitleCont.toLowerCase() + "%");
        });
    }

    private Specification<Book> withContGenreTypes(Set<String> genreTypes) {
        return ((root, query, criteriaBuilder) -> {
            if (genreTypes == null || genreTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Book> bookRoot = subquery.from(Book.class);
            Join<Book, Genre> bookGenreJoin = bookRoot.join("genres");

            subquery.select(bookRoot.get("id"))
                    .where(criteriaBuilder.and(
                            criteriaBuilder.equal(bookRoot.get("id"), root.get("id")),
                            bookGenreJoin.get("typeOfGenre").in(genreTypes)
                    ));
            return criteriaBuilder.exists(subquery);
        });
    }

    private Specification<Book> withDirectionOfLiterature(String directionOfLiterature) {
        return ((root, query, criteriaBuilder) -> {
            if (directionOfLiterature == null || directionOfLiterature.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("directionOfLiterature")),
                    "%" + directionOfLiterature.toLowerCase() + "%");
        });
    }
}
