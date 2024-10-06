package library.code.mapper;

import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.models.Publisher;
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
public abstract class PublisherMapper {
    public abstract Publisher map(PublisherCreateDTO createDTO);

    @Mapping(target = "booksFromSamePublisher", source = "books")
    public abstract PublisherDTO map(Publisher publisher);

    public abstract void update(PublisherUpdateDTO updateDTO, @MappingTarget Publisher publisher);
}
