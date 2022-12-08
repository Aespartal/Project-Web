package es.project.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link es.project.domain.ExtendedUser} entity. This class is used
 * in {@link es.project.web.rest.ExtendedUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extended-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtendedUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter web;

    private StringFilter location;

    private StringFilter profession;

    private LongFilter userId;

    private Boolean distinct;

    public ExtendedUserCriteria() {}

    public ExtendedUserCriteria(ExtendedUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.web = other.web == null ? null : other.web.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.profession = other.profession == null ? null : other.profession.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExtendedUserCriteria copy() {
        return new ExtendedUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getWeb() {
        return web;
    }

    public StringFilter web() {
        if (web == null) {
            web = new StringFilter();
        }
        return web;
    }

    public void setWeb(StringFilter web) {
        this.web = web;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getProfession() {
        return profession;
    }

    public StringFilter profession() {
        if (profession == null) {
            profession = new StringFilter();
        }
        return profession;
    }

    public void setProfession(StringFilter profession) {
        this.profession = profession;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExtendedUserCriteria that = (ExtendedUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(web, that.web) &&
            Objects.equals(location, that.location) &&
            Objects.equals(profession, that.profession) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, web, location, profession, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (web != null ? "web=" + web + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (profession != null ? "profession=" + profession + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
