package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Commentary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentaryDTO implements Serializable {

    private Long id;

    /**
     * description
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "description", required = true)
    private String description;

    /**
     * creationDate
     */
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    private ExtendedUserDTO extendedUser;

    private ImageDTO image;

    private String extendedUserName;

    private String extendedUserLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ExtendedUserDTO getExtendedUser() {
        return extendedUser;
    }

    public void setExtendedUser(ExtendedUserDTO extendedUser) {
        this.extendedUser = extendedUser;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public String getExtendedUserName() {
        return extendedUserName;
    }

    public void setExtendedUserName(String extendedUserName) {
        this.extendedUserName = extendedUserName;
    }

    public String getExtendedUserLogin() {
        return extendedUserLogin;
    }

    public void setExtendedUserLogin(String extendedUserLogin) {
        this.extendedUserLogin = extendedUserLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentaryDTO)) {
            return false;
        }

        CommentaryDTO commentaryDTO = (CommentaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentaryDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", extendedUser=" + getExtendedUser() +
            ", image=" + getImage() +
            "}";
    }
}
