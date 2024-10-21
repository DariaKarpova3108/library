package library.code.mapper;

import library.code.dto.roleDTO.RoleDTO;
import library.code.dto.userDTO.UserCreateDTO;
import library.code.dto.userDTO.UserDTO;
import library.code.dto.userDTO.UserUpdateDTO;
import library.code.models.Role;
import library.code.models.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {ReferenceMapper.class, JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO createDTO);

    @Mapping(target = "role", source = "role")
    public abstract UserDTO map(User user);

    @Mapping(target = "passwordDigest", source = "password")
    public abstract void update(UserUpdateDTO updateDTO, @MappingTarget User user);

    @Mapping(target = "roleName", source = "roleName")
    public abstract RoleDTO toRoleDTO(Role role);

    @BeforeMapping
    public void hashPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void hashPasswordToUpdate(UserUpdateDTO updateDTO, @MappingTarget User user) {
        if (updateDTO.getPassword() != null && updateDTO.getPassword().isPresent()) {
            var password = updateDTO.getPassword().get();
            user.setPasswordDigest(passwordEncoder.encode(password));
        }
    }
}
