package es.project.service.dto;

import es.project.domain.enumeration.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    /**
     * message
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "message", required = true)
    private String message;

    /**
     * type
     */
    @NotNull
    @Schema(description = "type", required = true)
    private NotificationType type;

    /**
     * creationDate
     */
    @NotNull
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    private ExtendedUserDTO image;

    private ExtendedUserDTO commentary;

    private ExtendedUserDTO notifier;

    private ExtendedUserDTO notifying;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ExtendedUserDTO getImage() {
        return image;
    }

    public void setImage(ExtendedUserDTO image) {
        this.image = image;
    }

    public ExtendedUserDTO getCommentary() {
        return commentary;
    }

    public void setCommentary(ExtendedUserDTO commentary) {
        this.commentary = commentary;
    }

    public ExtendedUserDTO getNotifier() {
        return notifier;
    }

    public void setNotifier(ExtendedUserDTO notifier) {
        this.notifier = notifier;
    }

    public ExtendedUserDTO getNotifying() {
        return notifying;
    }

    public void setNotifying(ExtendedUserDTO notifying) {
        this.notifying = notifying;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", type='" + getType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", image=" + getImage() +
            ", commentary=" + getCommentary() +
            ", notifier=" + getNotifier() +
            ", notifying=" + getNotifying() +
            "}";
    }
}
