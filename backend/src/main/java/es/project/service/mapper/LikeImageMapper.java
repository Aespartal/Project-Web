package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.LikeImage;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import es.project.service.dto.LikeImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeImage} and its DTO {@link LikeImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeImageMapper extends EntityMapper<LikeImageDTO, LikeImage> {
    @Mapping(target = "image", source = "image", qualifiedByName = "imageId")
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserId")
    LikeImageDTO toDto(LikeImage s);

    @Named("imageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImageDTO toDtoImageId(Image image);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
