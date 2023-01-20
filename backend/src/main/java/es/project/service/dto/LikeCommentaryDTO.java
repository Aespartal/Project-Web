package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.LikeCommentary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeCommentaryDTO implements Serializable {

    private Long id;

    /**
     * creationDate
     */
    @NotNull
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    private CommentaryDTO commentary;

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

    public CommentaryDTO getCommentary() {
        return commentary;
    }

    public void setCommentary(CommentaryDTO commentary) {
        this.commentary = commentary;
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
        if (!(o instanceof LikeCommentaryDTO)) {
            return false;
        }

        LikeCommentaryDTO likeCommentaryDTO = (LikeCommentaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likeCommentaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeCommentaryDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", commentary=" + getCommentary() +
            ", extendedUser=" + getExtendedUser() +
            "}";
    }
}
