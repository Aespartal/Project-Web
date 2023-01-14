package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LikeCommentary.
 */
@Entity
@Table(name = "like_commentary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeCommentary implements Serializable {

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

    @OneToMany(mappedBy = "likeCommentary")
    @JsonIgnoreProperties(value = { "extendedUser", "image", "likeCommentary" }, allowSetters = true)
    private Set<Commentary> commentaries = new HashSet<>();

    @OneToMany(mappedBy = "likeCommentary")
    @JsonIgnoreProperties(value = { "user", "likeImage", "likeCommentary" }, allowSetters = true)
    private Set<ExtendedUser> extendedUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeCommentary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public LikeCommentary creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Commentary> getCommentaries() {
        return this.commentaries;
    }

    public void setCommentaries(Set<Commentary> commentaries) {
        if (this.commentaries != null) {
            this.commentaries.forEach(i -> i.setLikeCommentary(null));
        }
        if (commentaries != null) {
            commentaries.forEach(i -> i.setLikeCommentary(this));
        }
        this.commentaries = commentaries;
    }

    public LikeCommentary commentaries(Set<Commentary> commentaries) {
        this.setCommentaries(commentaries);
        return this;
    }

    public LikeCommentary addCommentary(Commentary commentary) {
        this.commentaries.add(commentary);
        commentary.setLikeCommentary(this);
        return this;
    }

    public LikeCommentary removeCommentary(Commentary commentary) {
        this.commentaries.remove(commentary);
        commentary.setLikeCommentary(null);
        return this;
    }

    public Set<ExtendedUser> getExtendedUsers() {
        return this.extendedUsers;
    }

    public void setExtendedUsers(Set<ExtendedUser> extendedUsers) {
        if (this.extendedUsers != null) {
            this.extendedUsers.forEach(i -> i.setLikeCommentary(null));
        }
        if (extendedUsers != null) {
            extendedUsers.forEach(i -> i.setLikeCommentary(this));
        }
        this.extendedUsers = extendedUsers;
    }

    public LikeCommentary extendedUsers(Set<ExtendedUser> extendedUsers) {
        this.setExtendedUsers(extendedUsers);
        return this;
    }

    public LikeCommentary addExtendedUser(ExtendedUser extendedUser) {
        this.extendedUsers.add(extendedUser);
        extendedUser.setLikeCommentary(this);
        return this;
    }

    public LikeCommentary removeExtendedUser(ExtendedUser extendedUser) {
        this.extendedUsers.remove(extendedUser);
        extendedUser.setLikeCommentary(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeCommentary)) {
            return false;
        }
        return id != null && id.equals(((LikeCommentary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeCommentary{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
