package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.LikeImage;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import es.project.service.dto.LikeImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserId")
    @Mapping(target = "likeImage", source = "likeImage", qualifiedByName = "likeImageId")
    ImageDTO toDto(Image s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);

    @Named("likeImageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LikeImageDTO toDtoLikeImageId(LikeImage likeImage);
}
