package es.project.web.rest;

import es.project.service.dto.NotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class NotificationWebSocketResource {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketResource(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyClients(NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSend("/topic/notifications", notificationDTO);
    }
}
