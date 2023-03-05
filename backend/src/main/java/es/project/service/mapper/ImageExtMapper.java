package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.ImageExt;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring", uses= { ExtendedUserMapper.class, CommentaryMapper.class })
public interface ImageExtMapper extends EntityMapper<ImageDTO, ImageExt> {


    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserIdForImage")
    ImageDTO toDto(ImageExt s);

    @Mapping(target = "commentaries", ignore = true)
    @Mapping(target = "likes", ignore = true)
    ImageExt toEntity(ImageDTO dto);

    @Named("extendedUserIdForImage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
