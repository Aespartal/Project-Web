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

    private StringFilter location;

    private DoubleFilter height;

    private DoubleFilter weight;

    private InstantFilter birthDate;

    private IntegerFilter totalFollowers;

    private IntegerFilter totalFollowing;

    private IntegerFilter totalImages;

    private IntegerFilter totalNotifications;

    private LongFilter userId;

    private Boolean distinct;

    public ExtendedUserCriteria() {}

    public ExtendedUserCriteria(ExtendedUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.totalFollowers = other.totalFollowers == null ? null : other.totalFollowers.copy();
        this.totalFollowing = other.totalFollowing == null ? null : other.totalFollowing.copy();
        this.totalImages = other.totalImages == null ? null : other.totalImages.copy();
        this.totalNotifications = other.totalNotifications == null ? null : other.totalNotifications.copy();
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

    public DoubleFilter getHeight() {
        return height;
    }

    public DoubleFilter height() {
        if (height == null) {
            height = new DoubleFilter();
        }
        return height;
    }

    public void setHeight(DoubleFilter height) {
        this.height = height;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public DoubleFilter weight() {
        if (weight == null) {
            weight = new DoubleFilter();
        }
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public InstantFilter birthDate() {
        if (birthDate == null) {
            birthDate = new InstantFilter();
        }
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public IntegerFilter getTotalFollowers() {
        return totalFollowers;
    }

    public IntegerFilter totalFollowers() {
        if (totalFollowers == null) {
            totalFollowers = new IntegerFilter();
        }
        return totalFollowers;
    }

    public void setTotalFollowers(IntegerFilter totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public IntegerFilter getTotalFollowing() {
        return totalFollowing;
    }

    public IntegerFilter totalFollowing() {
        if (totalFollowing == null) {
            totalFollowing = new IntegerFilter();
        }
        return totalFollowing;
    }

    public void setTotalFollowing(IntegerFilter totalFollowing) {
        this.totalFollowing = totalFollowing;
    }

    public IntegerFilter getTotalImages() {
        return totalImages;
    }

    public IntegerFilter totalImages() {
        if (totalImages == null) {
            totalImages = new IntegerFilter();
        }
        return totalImages;
    }

    public void setTotalImages(IntegerFilter totalImages) {
        this.totalImages = totalImages;
    }

    public IntegerFilter getTotalNotifications() {
        return totalNotifications;
    }

    public IntegerFilter totalNotifications() {
        if (totalNotifications == null) {
            totalNotifications = new IntegerFilter();
        }
        return totalNotifications;
    }

    public void setTotalNotifications(IntegerFilter totalNotifications) {
        this.totalNotifications = totalNotifications;
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
            Objects.equals(location, that.location) &&
            Objects.equals(height, that.height) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(totalFollowers, that.totalFollowers) &&
            Objects.equals(totalFollowing, that.totalFollowing) &&
            Objects.equals(totalImages, that.totalImages) &&
            Objects.equals(totalNotifications, that.totalNotifications) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            location,
            height,
            weight,
            birthDate,
            totalFollowers,
            totalFollowing,
            totalImages,
            totalNotifications,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (totalFollowers != null ? "totalFollowers=" + totalFollowers + ", " : "") +
            (totalFollowing != null ? "totalFollowing=" + totalFollowing + ", " : "") +
            (totalImages != null ? "totalImages=" + totalImages + ", " : "") +
            (totalNotifications != null ? "totalNotifications=" + totalNotifications + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
