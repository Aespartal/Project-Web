package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LikeImage.
 */
@Entity
@Table(name = "like_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * creationDate
     */
    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @OneToMany(mappedBy = "likeImage")
    @JsonIgnoreProperties(value = { "commentaries", "extendedUser", "likeImage" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "likeImage")
    @JsonIgnoreProperties(value = { "user", "likeImage", "likeCommentary" }, allowSetters = true)
    private Set<ExtendedUser> extendedUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public LikeImage creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setLikeImage(null));
        }
        if (images != null) {
            images.forEach(i -> i.setLikeImage(this));
        }
        this.images = images;
    }

    public LikeImage images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public LikeImage addImage(Image image) {
        this.images.add(image);
        image.setLikeImage(this);
        return this;
    }

    public LikeImage removeImage(Image image) {
        this.images.remove(image);
        image.setLikeImage(null);
        return this;
    }

    public Set<ExtendedUser> getExtendedUsers() {
        return this.extendedUsers;
    }

    public void setExtendedUsers(Set<ExtendedUser> extendedUsers) {
        if (this.extendedUsers != null) {
            this.extendedUsers.forEach(i -> i.setLikeImage(null));
        }
        if (extendedUsers != null) {
            extendedUsers.forEach(i -> i.setLikeImage(this));
        }
        this.extendedUsers = extendedUsers;
    }

    public LikeImage extendedUsers(Set<ExtendedUser> extendedUsers) {
        this.setExtendedUsers(extendedUsers);
        return this;
    }

    public LikeImage addExtendedUser(ExtendedUser extendedUser) {
        this.extendedUsers.add(extendedUser);
        extendedUser.setLikeImage(this);
        return this;
    }

    public LikeImage removeExtendedUser(ExtendedUser extendedUser) {
        this.extendedUsers.remove(extendedUser);
        extendedUser.setLikeImage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeImage)) {
            return false;
        }
        return id != null && id.equals(((LikeImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeImage{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
