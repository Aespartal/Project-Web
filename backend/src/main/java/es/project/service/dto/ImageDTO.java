package es.project.service.dto;

import es.project.domain.Commentary;
import es.project.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Image} entity.
 */
@Schema(description = "The Image entity.\n@author alejandro.espartal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageDTO implements Serializable {

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
     * image
     */
    @Size(max = 3500)
    @Schema(description = "image")
    private String image;

    /**
     * imageType
     */
    @Schema(description = "imageType")
    private String imageType;

    /**
     * creationDate
     */
    @Schema(description = "creationDate", required = true)
    private Instant creationDate;

    /**
     * modificationDate
     */
    @Schema(description = "modificationDate")
    private Instant modificationDate;

    /**
     * isPrivate
     */
    @NotNull
    @Schema(description = "isPrivate", required = true)
    private Boolean isPrivate;

    @Lob
    @Schema(description = "imageBase64")
    private byte[] imageBase64;

    private ExtendedUserDTO extendedUser;

    private Set<CommentaryDTO> commentaries = new HashSet<>();

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public byte[] getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(byte[] imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public ExtendedUserDTO getExtendedUser() {
        return extendedUser;
    }

    public void setExtendedUser(ExtendedUserDTO extendedUser) {
        this.extendedUser = extendedUser;
    }

    public ImageDTO commentaries(Set<CommentaryDTO> commentaries) {
        this.setCommentaries(commentaries);
        return this;
    }

    public ImageDTO addCommentaries(CommentaryDTO commentary) {
        this.commentaries.add(commentary);
        commentary.setImage(this);
        return this;
    }

    public ImageDTO removeCommentaries(CommentaryDTO commentary) {
        this.commentaries.remove(commentary);
        commentary.setImage(null);
        return this;
    }

    public Set<CommentaryDTO> getCommentaries() {
        return this.commentaries;
    }

    public void setCommentaries(Set<CommentaryDTO> commentaries) {
        if (this.commentaries != null) {
            this.commentaries.forEach(i -> i.setImage(null));
        }
        if (commentaries != null) {
            commentaries.forEach(i -> i.setImage(this));
        }
        this.commentaries = commentaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageDTO)) {
            return false;
        }

        ImageDTO imageDTO = (ImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            ", extendedUser=" + getExtendedUser() +
            "}";
    }
}
