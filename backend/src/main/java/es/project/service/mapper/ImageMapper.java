package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring", uses= { ExtendedUserMapper.class, CommentaryMapper.class })
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {

    @Mapping(target = "removeCommentaries", ignore = true)
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserIdForImage")
    ImageDTO toDto(Image s);

    @Mapping(target = "removeCommentaries", ignore = true)
    Image toEntity(ImageDTO dto);

    @Named("extendedUserIdForImage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
