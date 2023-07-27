package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Project;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExtendedUserMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "extendedUser", source = "extendedUser", qualifiedByName = "extendedUserId")
    ProjectDTO toDto(Project s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
