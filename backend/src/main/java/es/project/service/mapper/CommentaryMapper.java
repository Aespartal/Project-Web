package es.project.service.mapper;

import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.LikeCommentary;
import es.project.service.dto.CommentaryDTO;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import es.project.service.dto.LikeCommentaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commentary} and its DTO {@link CommentaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentaryMapper extends EntityMapper<CommentaryDTO, Commentary> {
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserId")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageId")
    @Mapping(target = "likeCommentary", source = "likeCommentary", qualifiedByName = "likeCommentaryId")
    CommentaryDTO toDto(Commentary s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);

    @Named("imageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImageDTO toDtoImageId(Image image);

    @Named("likeCommentaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LikeCommentaryDTO toDtoLikeCommentaryId(LikeCommentary likeCommentary);
}
