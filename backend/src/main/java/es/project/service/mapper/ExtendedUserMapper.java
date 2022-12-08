package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.User;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExtendedUser} and its DTO {@link ExtendedUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExtendedUserMapper extends EntityMapper<ExtendedUserDTO, ExtendedUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ExtendedUserDTO toDto(ExtendedUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
