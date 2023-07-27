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

    /**
     * totalFollowers
     */
    @Min(value = 0)
    @Schema(description = "totalFollowers")
    private Integer totalFollowers;

    /**
     * totalFollowing
     */
    @Min(value = 0)
    @Schema(description = "totalFollowing")
    private Integer totalFollowing;

    /**
     * totalImages
     */
    @NotNull
    @Min(value = 0)
    @Schema(description = "totalImages", required = true)
    private Integer totalImages;

    /**
     * totalNotifications
     */
    @NotNull
    @Min(value = 0)
    @Schema(description = "totalNotifications", required = true)
    private Integer totalNotifications;

    private AdminUserDTO user;

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

    public Integer getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Integer totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Integer getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(Integer totalFollowing) {
        this.totalFollowing = totalFollowing;
    }

    public Integer getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(Integer totalImages) {
        this.totalImages = totalImages;
    }

    public Integer getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(Integer totalNotifications) {
        this.totalNotifications = totalNotifications;
    }

    public AdminUserDTO getUser() {
        return user;
    }

    public void setUser(AdminUserDTO user) {
        this.user = user;
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
            ", totalFollowers=" + getTotalFollowers() +
            ", totalFollowing=" + getTotalFollowing() +
            ", totalImages=" + getTotalImages() +
            ", totalNotifications=" + getTotalNotifications() +
            ", user=" + getUser() +
            "}";
    }
}
