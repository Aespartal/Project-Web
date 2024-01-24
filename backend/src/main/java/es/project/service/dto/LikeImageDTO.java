package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.LikeImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeImageDTO implements Serializable {

    private Long id;

    /**
     * creationDate
     */
    @NotNull
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    private ImageDTO image;

    private ExtendedUserDTO extendedUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public ExtendedUserDTO getExtendedUser() {
        return extendedUser;
    }

    public void setExtendedUser(ExtendedUserDTO extendedUser) {
        this.extendedUser = extendedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeImageDTO)) {
            return false;
        }

        LikeImageDTO likeImageDTO = (LikeImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likeImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeImageDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", image=" + getImage() +
            ", extendedUser=" + getExtendedUser() +
            "}";
    }
}
