package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.User;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExtendedUser} and its DTO {@link ExtendedUserDTO}.
 */
@Mapper(componentModel = "spring", uses= { UserMapper.class })
public interface ExtendedUserMapper extends EntityMapper<ExtendedUserDTO, ExtendedUser> {

    @Mapping(source = "user.login", target = "userLogin")
    ExtendedUserDTO toDto(ExtendedUser s);

    ExtendedUser toEntity(ExtendedUserDTO dto);
}
