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
@Mapper(componentModel = "spring", uses= { ExtendedUserMapper.class, ImageMapper.class})
public interface LikeImageMapper extends EntityMapper<LikeImageDTO, LikeImage> {

    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserIdForLikeImage")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageIdForLikeImage")
    LikeImageDTO toDto(LikeImage s);

    @Named("imageIdForLikeImage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImageDTO toDtoImageId(Image image);

    @Named("extendedUserIdForLikeImage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
