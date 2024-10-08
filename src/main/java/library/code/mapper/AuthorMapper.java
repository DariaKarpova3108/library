package library.code.mapper;

import library.code.dto.AuthorDTO.AuthorCreateDTO;
import library.code.dto.AuthorDTO.AuthorDTO;
import library.code.dto.AuthorDTO.AuthorUpdateDTO;
import library.code.models.Author;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class, BookMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AuthorMapper {
    public abstract Author map(AuthorCreateDTO createDTO);

    @Mapping(target = "bookList", source = "books")
    public abstract AuthorDTO map(Author author);

    public abstract void update(AuthorUpdateDTO updateDTO, @MappingTarget Author author);

}
