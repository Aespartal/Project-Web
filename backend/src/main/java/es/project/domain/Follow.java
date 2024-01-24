package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.project.domain.enumeration.FollowState;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Follow.
 */
@Entity
@Table(name = "follow")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * state
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private FollowState state;

    /**
     * creationDate
     */
    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    /**
     * acceptanceDate
     */
    @Column(name = "acceptance_date")
    private Instant acceptanceDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser follower;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser following;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Follow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FollowState getState() {
        return this.state;
    }

    public Follow state(FollowState state) {
        this.setState(state);
        return this;
    }

    public void setState(FollowState state) {
        this.state = state;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Follow creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getAcceptanceDate() {
        return this.acceptanceDate;
    }

    public Follow acceptanceDate(Instant acceptanceDate) {
        this.setAcceptanceDate(acceptanceDate);
        return this;
    }

    public void setAcceptanceDate(Instant acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public ExtendedUser getFollower() {
        return this.follower;
    }

    public void setFollower(ExtendedUser extendedUser) {
        this.follower = extendedUser;
    }

    public Follow follower(ExtendedUser extendedUser) {
        this.setFollower(extendedUser);
        return this;
    }

    public ExtendedUser getFollowing() {
        return this.following;
    }

    public void setFollowing(ExtendedUser extendedUser) {
        this.following = extendedUser;
    }

    public Follow following(ExtendedUser extendedUser) {
        this.setFollowing(extendedUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Follow)) {
            return false;
        }
        return id != null && id.equals(((Follow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Follow{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", acceptanceDate='" + getAcceptanceDate() + "'" +
            "}";
    }
}
