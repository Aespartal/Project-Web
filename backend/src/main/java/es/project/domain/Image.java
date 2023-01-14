package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * The Image entity.\n@author alejandro.espartal
 */
@Entity
@Table(name = "image")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * name
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * description
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "description", length = 3500, nullable = false)
    private String description;

    /**
     * image
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "image", length = 3500, nullable = false)
    private String image;

    /**
     * imageType
     */
    @NotNull
    @Column(name = "image_type", nullable = false)
    private String imageType;

    /**
     * creationDate
     */
    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    /**
     * modificationDate
     */
    @Column(name = "modification_date")
    private Instant modificationDate;

    /**
     * isPrivate
     */
    @NotNull
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @OneToMany(mappedBy = "image")
    @JsonIgnoreProperties(value = { "extendedUser", "image", "likeCommentary" }, allowSetters = true)
    private Set<Commentary> commentaries = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "likeImage", "likeCommentary" }, allowSetters = true)
    private ExtendedUser extendedUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "images", "extendedUsers" }, allowSetters = true)
    private LikeImage likeImage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Image id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Image name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Image description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public Image image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return this.imageType;
    }

    public Image imageType(String imageType) {
        this.setImageType(imageType);
        return this;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Image creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModificationDate() {
        return this.modificationDate;
    }

    public Image modificationDate(Instant modificationDate) {
        this.setModificationDate(modificationDate);
        return this;
    }

    public void setModificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public Image isPrivate(Boolean isPrivate) {
        this.setIsPrivate(isPrivate);
        return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Set<Commentary> getCommentaries() {
        return this.commentaries;
    }

    public void setCommentaries(Set<Commentary> commentaries) {
        if (this.commentaries != null) {
            this.commentaries.forEach(i -> i.setImage(null));
        }
        if (commentaries != null) {
            commentaries.forEach(i -> i.setImage(this));
        }
        this.commentaries = commentaries;
    }

    public Image commentaries(Set<Commentary> commentaries) {
        this.setCommentaries(commentaries);
        return this;
    }

    public Image addCommentaries(Commentary commentary) {
        this.commentaries.add(commentary);
        commentary.setImage(this);
        return this;
    }

    public Image removeCommentaries(Commentary commentary) {
        this.commentaries.remove(commentary);
        commentary.setImage(null);
        return this;
    }

    public ExtendedUser getExtendedUser() {
        return this.extendedUser;
    }

    public void setExtendedUser(ExtendedUser extendedUser) {
        this.extendedUser = extendedUser;
    }

    public Image extendedUser(ExtendedUser extendedUser) {
        this.setExtendedUser(extendedUser);
        return this;
    }

    public LikeImage getLikeImage() {
        return this.likeImage;
    }

    public void setLikeImage(LikeImage likeImage) {
        this.likeImage = likeImage;
    }

    public Image likeImage(LikeImage likeImage) {
        this.setLikeImage(likeImage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        return id != null && id.equals(((Image) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            "}";
    }
}
