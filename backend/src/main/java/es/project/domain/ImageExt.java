package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * The Image entity.\n@author alejandro.espartal
 */
@Entity
public class ImageExt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String fileName;


    @Column(name = "path")
    private String path;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modification_date")
    private Instant modificationDate;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "is_favourited")
    private Boolean isFavourited;

    @OneToMany(mappedBy = "image")
    @JsonIgnoreProperties(value = { "extendedUser", "image" }, allowSetters = true)
    private Set<Commentary> commentaries = new HashSet<>();

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser extendedUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageExt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ImageExt title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public ImageExt description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return this.fileName;
    }

    public ImageExt fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return this.path;
    }

    public ImageExt path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ImageExt creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getModificationDate() {
        return this.modificationDate;
    }

    public ImageExt modificationDate(Instant modificationDate) {
        this.setModificationDate(modificationDate);
        return this;
    }

    public void setModificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public ImageExt isPrivate(Boolean isPrivate) {
        this.setIsPrivate(isPrivate);
        return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Set<Commentary> getCommentaries() {
        return this.commentaries;
    }

    public ExtendedUser getExtendedUser() {
        return this.extendedUser;
    }

    public void setExtendedUser(ExtendedUser extendedUser) {
        this.extendedUser = extendedUser;
    }

    public ImageExt extendedUser(ExtendedUser extendedUser) {
        this.setExtendedUser(extendedUser);
        return this;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getFavourited() {
        return isFavourited;
    }

    public void setFavourited(Boolean favourited) {
        isFavourited = favourited;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageExt)) {
            return false;
        }
        return id != null && id.equals(((ImageExt) o).id);
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
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", path='" + getPath() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            "}";
    }
}
