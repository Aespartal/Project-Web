package es.project.domain;

import java.io.Serializable;
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
     * web
     */
    @Size(max = 100)
    @Column(name = "web", length = 100)
    private String web;

    /**
     * location
     */
    @Size(max = 50)
    @Column(name = "location", length = 50)
    private String location;

    /**
     * profession
     */
    @Size(max = 50)
    @Column(name = "profession", length = 50)
    private String profession;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

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

    public String getWeb() {
        return this.web;
    }

    public ExtendedUser web(String web) {
        this.setWeb(web);
        return this;
    }

    public void setWeb(String web) {
        this.web = web;
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

    public String getProfession() {
        return this.profession;
    }

    public ExtendedUser profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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
            ", web='" + getWeb() + "'" +
            ", location='" + getLocation() + "'" +
            ", profession='" + getProfession() + "'" +
            "}";
    }
}
