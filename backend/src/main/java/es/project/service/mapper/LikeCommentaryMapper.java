package es.project.service.mapper;

import es.project.domain.LikeCommentary;
import es.project.service.dto.LikeCommentaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeCommentary} and its DTO {@link LikeCommentaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeCommentaryMapper extends EntityMapper<LikeCommentaryDTO, LikeCommentary> {}
