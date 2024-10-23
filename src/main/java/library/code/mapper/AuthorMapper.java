package library.code.mapper;

import library.code.dto.authorDTO.AuthorCreateDTO;
import library.code.dto.authorDTO.AuthorDTO;
import library.code.dto.authorDTO.AuthorUpdateDTO;
import library.code.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

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
