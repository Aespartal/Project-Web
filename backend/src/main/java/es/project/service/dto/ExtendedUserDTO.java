package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.ExtendedUser} entity.
 */
@Schema(description = "The ExtendedUser entity.\n@author alejandro.espartal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtendedUserDTO implements Serializable {

    private Long id;

    /**
     * description
     */
    @Size(max = 3500)
    @Schema(description = "description")
    private String description;

    /**
     * location
     */
    @Size(max = 50)
    @Schema(description = "location")
    private String location;

    /**
     * height
     */
    @NotNull
    @DecimalMin(value = "0")
    @Schema(description = "height", required = true)
    private Double height;

    /**
     * weight
     */
    @NotNull
    @DecimalMin(value = "0")
    @Schema(description = "weight", required = true)
    private Double weight;

    /**
     * birthDate
     */
    @NotNull
    @Schema(description = "birthDate", required = true)
    private Instant birthDate;

    private UserDTO user;

    private LikeImageDTO likeImage;

    private LikeCommentaryDTO likeCommentary;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LikeImageDTO getLikeImage() {
        return likeImage;
    }

    public void setLikeImage(LikeImageDTO likeImage) {
        this.likeImage = likeImage;
    }

    public LikeCommentaryDTO getLikeCommentary() {
        return likeCommentary;
    }

    public void setLikeCommentary(LikeCommentaryDTO likeCommentary) {
        this.likeCommentary = likeCommentary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUserDTO)) {
            return false;
        }

        ExtendedUserDTO extendedUserDTO = (ExtendedUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extendedUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", birthDate='" + getBirthDate() + "'" +
            ", user=" + getUser() +
            ", likeImage=" + getLikeImage() +
            ", likeCommentary=" + getLikeCommentary() +
            "}";
    }
}
