package library.code.mapper;

import library.code.dto.LibraryCardDTO.LibraryCardCreateDTO;
import library.code.dto.LibraryCardDTO.LibraryCardDTO;
import library.code.dto.LibraryCardDTO.LibraryCardUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.models.LibraryCard;
import library.code.models.Reader;
import library.code.repositories.ReaderRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class, LibraryCardBookMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LibraryCardMapper {

    @Autowired
    private ReaderRepository readerRepository;

    @Mapping(target = "reader", source = "readerId", qualifiedByName = "findReader")
    public abstract LibraryCard map(LibraryCardCreateDTO createDTO);

    @Mapping(target = "readerFirstName", source = "reader.firstName")
    @Mapping(target = "readerSurname", source = "reader.lastName")
    @Mapping(target = "borrowedBooks", source = "borrowedBooks")
    public abstract LibraryCardDTO map(LibraryCard libraryCard);

    @Mapping(target = "reader", source = "readerId", qualifiedByName = "findReader")
    public abstract void update(LibraryCardUpdateDTO updateDTO, @MappingTarget LibraryCard libraryCard);

    @Named("findReader")
    public Reader findReader(Long readerId) {
        return readerRepository.findById(readerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader with id: " + readerId + " not found"));
    }
}
