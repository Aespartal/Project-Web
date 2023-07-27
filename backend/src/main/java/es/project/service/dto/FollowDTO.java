package es.project.service.dto;

import es.project.domain.enumeration.FollowState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Follow} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FollowDTO implements Serializable {

    private Long id;

    /**
     * state
     */
    @NotNull
    @Schema(description = "state", required = true)
    private FollowState state;

    /**
     * creationDate
     */
    @NotNull
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    /**
     * acceptanceDate
     */
    @Schema(description = "acceptanceDate")
    private Instant acceptanceDate;

    private ExtendedUserDTO follower;

    private ExtendedUserDTO following;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FollowState getState() {
        return state;
    }

    public void setState(FollowState state) {
        this.state = state;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Instant acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public ExtendedUserDTO getFollower() {
        return follower;
    }

    public void setFollower(ExtendedUserDTO follower) {
        this.follower = follower;
    }

    public ExtendedUserDTO getFollowing() {
        return following;
    }

    public void setFollowing(ExtendedUserDTO following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FollowDTO)) {
            return false;
        }

        FollowDTO followDTO = (FollowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, followDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FollowDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", acceptanceDate='" + getAcceptanceDate() + "'" +
            ", follower=" + getFollower() +
            ", following=" + getFollowing() +
            "}";
    }
}
