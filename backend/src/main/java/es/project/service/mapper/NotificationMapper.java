package es.project.service.mapper;

import es.project.domain.ExtendedUser;
import es.project.domain.Notification;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "image", source = "image", qualifiedByName = "extendedUserId")
    @Mapping(target = "commentary", source = "commentary", qualifiedByName = "extendedUserId")
    @Mapping(target = "notifier", source = "notifier", qualifiedByName = "extendedUserId")
    @Mapping(target = "notifying", source = "notifying", qualifiedByName = "extendedUserId")
    NotificationDTO toDto(Notification s);

    @Named("extendedUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExtendedUserDTO toDtoExtendedUserId(ExtendedUser extendedUser);
}
