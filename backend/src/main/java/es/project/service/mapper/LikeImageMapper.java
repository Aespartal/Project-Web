package es.project.service.mapper;

import es.project.domain.LikeImage;
import es.project.service.dto.LikeImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeImage} and its DTO {@link LikeImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeImageMapper extends EntityMapper<LikeImageDTO, LikeImage> {}
