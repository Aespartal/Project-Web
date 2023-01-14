package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * The ExtendedUser entity.\n@author alejandro.espartal
 */
@Entity
@Table(name = "extended_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtendedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    /**
     * description
     */
    @Size(max = 3500)
    @Column(name = "description", length = 3500)
    private String description;

    /**
     * location
     */
    @Size(max = 50)
    @Column(name = "location", length = 50)
    private String location;

    /**
     * height
     */
    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "height", nullable = false)
    private Double height;

    /**
     * weight
     */
    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "weight", nullable = false)
    private Double weight;

    /**
     * birthDate
     */
    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "images", "extendedUsers" }, allowSetters = true)
    private LikeImage likeImage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commentaries", "extendedUsers" }, allowSetters = true)
    private LikeCommentary likeCommentary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExtendedUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public ExtendedUser description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public ExtendedUser location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getHeight() {
        return this.height;
    }

    public ExtendedUser height(Double height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return this.weight;
    }

    public ExtendedUser weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public ExtendedUser birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExtendedUser user(User user) {
        this.setUser(user);
        return this;
    }

    public LikeImage getLikeImage() {
        return this.likeImage;
    }

    public void setLikeImage(LikeImage likeImage) {
        this.likeImage = likeImage;
    }

    public ExtendedUser likeImage(LikeImage likeImage) {
        this.setLikeImage(likeImage);
        return this;
    }

    public LikeCommentary getLikeCommentary() {
        return this.likeCommentary;
    }

    public void setLikeCommentary(LikeCommentary likeCommentary) {
        this.likeCommentary = likeCommentary;
    }

    public ExtendedUser likeCommentary(LikeCommentary likeCommentary) {
        this.setLikeCommentary(likeCommentary);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUser)) {
            return false;
        }
        return id != null && id.equals(((ExtendedUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUser{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
