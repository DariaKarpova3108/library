package library.code.mapper;

import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.models.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class, UserMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ReaderMapper {
    public abstract Reader map(ReaderCreateDTO createDTO);

    @Mapping(target = "libraryCard", source = "libraryCard.cardNumber")
    @Mapping(target = "email", source = "user.email")
    public abstract ReaderDTO map(Reader reader);

    public abstract void update(ReaderUpdateDTO updateDTO, @MappingTarget Reader reader);
}
