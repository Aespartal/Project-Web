package es.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.project.domain.ExtendedUser} entity.
 */
@Schema(description = "The ExtendedUser entity.\n@author alejandro.espartal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtendedUserDTO implements Serializable {

    private Long id;

    /**
     * description
     */
    @Size(max = 3500)
    @Schema(description = "description")
    private String description;

    /**
     * web
     */
    @Size(max = 100)
    @Schema(description = "web")
    private String web;

    /**
     * location
     */
    @Size(max = 50)
    @Schema(description = "location")
    private String location;

    /**
     * profession
     */
    @Size(max = 50)
    @Schema(description = "profession")
    private String profession;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUserDTO)) {
            return false;
        }

        ExtendedUserDTO extendedUserDTO = (ExtendedUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extendedUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", web='" + getWeb() + "'" +
            ", location='" + getLocation() + "'" +
            ", profession='" + getProfession() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
