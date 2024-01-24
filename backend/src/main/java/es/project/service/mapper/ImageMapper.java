package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { ExtendedUserMapper.class })
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {

    @Mapping(target = "extendedUserName", source = "extendedUser.user.firstName")
    @Mapping(target = "extendedUserLogin", source = "extendedUser.user.login")
    ImageDTO toDto(Image s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
