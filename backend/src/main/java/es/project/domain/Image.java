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
     * title
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    /**
     * description
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "description", length = 3500, nullable = false)
    private String description;

    /**
     * fileName
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "file_name", length = 3500, nullable = false)
    private String fileName;

    /**
     * path
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "path", length = 3500, nullable = false)
    private String path;

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

    /**
     * totalLikes
     */
    @Min(value = 0)
    @Column(name = "total_likes")
    private Integer totalLikes;

    /**
     * totalCommentaries
     */
    @Min(value = 0)
    @Column(name = "total_commentaries")
    private Integer totalCommentaries;

    @OneToMany(mappedBy = "image")
    @JsonIgnoreProperties(value = { "extendedUser", "image" }, allowSetters = true)
    private Set<Commentary> commentaries = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser extendedUser;

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

    public String getTitle() {
        return this.title;
    }

    public Image title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFileName() {
        return this.fileName;
    }

    public Image fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return this.path;
    }

    public Image path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Integer getTotalLikes() {
        return this.totalLikes;
    }

    public Image totalLikes(Integer totalLikes) {
        this.setTotalLikes(totalLikes);
        return this;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalCommentaries() {
        return this.totalCommentaries;
    }

    public Image totalCommentaries(Integer totalCommentaries) {
        this.setTotalCommentaries(totalCommentaries);
        return this;
    }

    public void setTotalCommentaries(Integer totalCommentaries) {
        this.totalCommentaries = totalCommentaries;
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
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", path='" + getPath() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            ", totalLikes=" + getTotalLikes() +
            ", totalCommentaries=" + getTotalCommentaries() +
            "}";
    }
}
