package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Project} entity.
 */
@Schema(description = "The Project entity.\n@author alejandro.espartal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectDTO implements Serializable {

    private Long id;

    /**
     * name
     */
    @NotNull
    @Size(max = 100)
    @Schema(description = "name", required = true)
    private String name;

    /**
     * description
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "description", required = true)
    private String description;

    /**
     * link
     */
    @NotNull
    @Size(max = 100)
    @Schema(description = "link", required = true)
    private String link;

    /**
     * image
     */
    @Schema(description = "image")
    @Lob
    private byte[] image;

    private String imageContentType;

    /**
     * order
     */
    @NotNull
    @Schema(description = "order", required = true)
    private Integer order;

    /**
     * creationDate
     */
    @NotNull
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    /**
     * isPrivate
     */
    @NotNull
    @Schema(description = "isPrivate", required = true)
    private Boolean isPrivate;

    /**
     * active
     */
    @NotNull
    @Schema(description = "active", required = true)
    private Boolean active;

    private ExtendedUserDTO extendedUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", image='" + getImage() + "'" +
            ", order=" + getOrder() +
            ", creationDate='" + getCreationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            ", active='" + getActive() + "'" +
            ", extendedUser=" + getExtendedUser() +
            "}";
    }
}
