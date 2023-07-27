package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Follow;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.FollowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Follow} and its DTO {@link FollowDTO}.
 */
@Mapper(componentModel = "spring")
public interface FollowMapper extends EntityMapper<FollowDTO, Follow> {
    @Mapping(target = "follower", source = "follower", qualifiedByName = "extendedUserId")
    @Mapping(target = "following", source = "following", qualifiedByName = "extendedUserId")
    FollowDTO toDto(Follow s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
