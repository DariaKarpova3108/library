package library.code.mapper;

import library.code.dto.administatorDTO.AdminCreateDTO;
import library.code.dto.administatorDTO.AdminUpdateDTO;
import library.code.dto.administatorDTO.AdministratorDTO;
import library.code.models.Administrator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class, UserMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class AdministratorMapper {
    public abstract Administrator map(AdminCreateDTO adminCreateDTO);

    @Mapping(target = "email", source = "user.email")
    public abstract AdministratorDTO map(Administrator admin);

    public abstract void update(AdminUpdateDTO updateDTO, @MappingTarget Administrator admin);
}
