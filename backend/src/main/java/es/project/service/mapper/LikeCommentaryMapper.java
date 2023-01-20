package es.project.service.mapper;

import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.LikeCommentary;
import es.project.service.dto.CommentaryDTO;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.LikeCommentaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeCommentary} and its DTO {@link LikeCommentaryDTO}.
 */
@Mapper(componentModel = "spring", uses= { CommentaryMapper.class, ExtendedUserMapper.class })
public interface LikeCommentaryMapper extends EntityMapper<LikeCommentaryDTO, LikeCommentary> {

    @Mapping(target = "commentary", source = "commentary", qualifiedByName = "commentaryIdForLikeCommentary")
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserIdForLikeCommentary")
    LikeCommentaryDTO toDto(LikeCommentary s);


    @Named("commentaryIdForLikeCommentary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommentaryDTO toDtoCommentaryId(Commentary commentary);

    @Named("extendedUserIdForLikeCommentary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
