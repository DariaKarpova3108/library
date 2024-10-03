package library.code.mapper;

import library.code.dto.BookDTO.BookCreateDTO;
import library.code.dto.BookDTO.BookDTO;
import library.code.dto.BookDTO.BookUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.models.Author;
import library.code.models.Book;
import library.code.models.Genre;
import library.code.models.Publisher;
import library.code.repositories.AuthorRepository;
import library.code.repositories.GenreRepository;
import library.code.repositories.PublisherRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class BookMapper {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Mapping(target = "author", source = "authorId", qualifiedByName = "findAuthor")
    @Mapping(target = "publisher", source = "publisherId", qualifiedByName = "findPublisher")
    @Mapping(target = "genres", source = "genreTypes", qualifiedByName = "typeGenreToModel")
    public abstract Book map(BookCreateDTO createDTO);

    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorSurname", source = "author.lastName")
    @Mapping(target = "publisherTitle", source = "publisher.title")
    @Mapping(target = "genreTypes", source = "genres", qualifiedByName = "modelGenresToTitleOfTypes")
    public abstract BookDTO map(Book book);

    @Mapping(target = "author", source = "authorId", qualifiedByName = "findAuthor")
    @Mapping(target = "publisher", source = "publisherId", qualifiedByName = "findPublisher")
    @Mapping(target = "genres", source = "genreTypes", qualifiedByName = "typeGenreToModel")
    public abstract void update(BookUpdateDTO updateDTO, @MappingTarget Book book);

    @Named("findAuthor")
    public Author findAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + authorId + " not found"));
    }

    @Named("findPublisher")
    public Publisher findPublisherById(Long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with id: " + publisherId + " not found"));
    }

    @Named("typeGenreToModel")
    public Set<Genre> performTypeGenreToModel(Set<String> genreTypes) {
        return genreTypes.stream()
                .map(genreType -> genreRepository.findByTypeOfGenre(genreType))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Named("modelGenresToTitleOfTypes")
    public Set<String> performModelGenresToTitleOfTypes(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getTypeOfGenre)
                .collect(Collectors.toSet());
    }

}
