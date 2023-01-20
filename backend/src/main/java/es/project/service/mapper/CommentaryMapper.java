package es.project.service.mapper;

import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.service.dto.CommentaryDTO;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commentary} and its DTO {@link CommentaryDTO}.
 */
@Mapper(componentModel = "spring", uses= { ExtendedUserMapper.class })
public interface CommentaryMapper extends EntityMapper<CommentaryDTO, Commentary> {
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserIdForCommentary")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageIdForCommentary")
    CommentaryDTO toDto(Commentary s);

    @Named("extendedUserIdForCommentary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);

    @Named("imageIdForCommentary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImageDTO toDtoImageId(Image image);
}
