package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.Image} entity.
 */
@Schema(description = "The Image entity.\n@author alejandro.espartal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageDTO implements Serializable {

    private Long id;

    /**
     * title
     */
    @NotNull
    @Size(max = 100)
    @Schema(description = "title", required = true)
    private String title;

    /**
     * description
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "description", required = true)
    private String description;

    /**
     * fileName
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "fileName", required = true)
    private String fileName;

    /**
     * path
     */
    @NotNull
    @Size(max = 3500)
    @Schema(description = "path", required = true)
    private String path;

    /**
     * creationDate
     */
    @NotNull
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

    /**
     * totalLikes
     */
    @Min(value = 0)
    @Schema(description = "totalLikes")
    private Integer totalLikes;

    /**
     * totalCommentaries
     */
    @Min(value = 0)
    @Schema(description = "totalCommentaries")
    private Integer totalCommentaries;

    private ExtendedUserDTO extendedUser;

    private Boolean isFavourited;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalCommentaries() {
        return totalCommentaries;
    }

    public void setTotalCommentaries(Integer totalCommentaries) {
        this.totalCommentaries = totalCommentaries;
    }

    public ExtendedUserDTO getExtendedUser() {
        return extendedUser;
    }

    public void setExtendedUser(ExtendedUserDTO extendedUser) {
        this.extendedUser = extendedUser;
    }

    public Boolean getFavourited() {
        return isFavourited;
    }

    public void setFavourited(Boolean favourited) {
        isFavourited = favourited;
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
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", path='" + getPath() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            ", totalLikes=" + getTotalLikes() +
            ", totalCommentaries=" + getTotalCommentaries() +
            ", extendedUser=" + getExtendedUser() +
            "}";
    }
}
