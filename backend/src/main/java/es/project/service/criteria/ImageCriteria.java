package es.project.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link es.project.domain.Image} entity. This class is used
 * in {@link es.project.web.rest.ImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter image;

    private StringFilter imageType;

    private InstantFilter creationDate;

    private InstantFilter modificationDate;

    private BooleanFilter isPrivate;

    private LongFilter commentariesId;

    private LongFilter extendedUserId;

    private LongFilter likeImageId;

    private Boolean distinct;

    public ImageCriteria() {}

    public ImageCriteria(ImageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.imageType = other.imageType == null ? null : other.imageType.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modificationDate = other.modificationDate == null ? null : other.modificationDate.copy();
        this.isPrivate = other.isPrivate == null ? null : other.isPrivate.copy();
        this.commentariesId = other.commentariesId == null ? null : other.commentariesId.copy();
        this.extendedUserId = other.extendedUserId == null ? null : other.extendedUserId.copy();
        this.likeImageId = other.likeImageId == null ? null : other.likeImageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImageCriteria copy() {
        return new ImageCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getImage() {
        return image;
    }

    public StringFilter image() {
        if (image == null) {
            image = new StringFilter();
        }
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public StringFilter getImageType() {
        return imageType;
    }

    public StringFilter imageType() {
        if (imageType == null) {
            imageType = new StringFilter();
        }
        return imageType;
    }

    public void setImageType(StringFilter imageType) {
        this.imageType = imageType;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public InstantFilter getModificationDate() {
        return modificationDate;
    }

    public InstantFilter modificationDate() {
        if (modificationDate == null) {
            modificationDate = new InstantFilter();
        }
        return modificationDate;
    }

    public void setModificationDate(InstantFilter modificationDate) {
        this.modificationDate = modificationDate;
    }

    public BooleanFilter getIsPrivate() {
        return isPrivate;
    }

    public BooleanFilter isPrivate() {
        if (isPrivate == null) {
            isPrivate = new BooleanFilter();
        }
        return isPrivate;
    }

    public void setIsPrivate(BooleanFilter isPrivate) {
        this.isPrivate = isPrivate;
    }

    public LongFilter getCommentariesId() {
        return commentariesId;
    }

    public LongFilter commentariesId() {
        if (commentariesId == null) {
            commentariesId = new LongFilter();
        }
        return commentariesId;
    }

    public void setCommentariesId(LongFilter commentariesId) {
        this.commentariesId = commentariesId;
    }

    public LongFilter getExtendedUserId() {
        return extendedUserId;
    }

    public LongFilter extendedUserId() {
        if (extendedUserId == null) {
            extendedUserId = new LongFilter();
        }
        return extendedUserId;
    }

    public void setExtendedUserId(LongFilter extendedUserId) {
        this.extendedUserId = extendedUserId;
    }

    public LongFilter getLikeImageId() {
        return likeImageId;
    }

    public LongFilter likeImageId() {
        if (likeImageId == null) {
            likeImageId = new LongFilter();
        }
        return likeImageId;
    }

    public void setLikeImageId(LongFilter likeImageId) {
        this.likeImageId = likeImageId;
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
        final ImageCriteria that = (ImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(image, that.image) &&
            Objects.equals(imageType, that.imageType) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modificationDate, that.modificationDate) &&
            Objects.equals(isPrivate, that.isPrivate) &&
            Objects.equals(commentariesId, that.commentariesId) &&
            Objects.equals(extendedUserId, that.extendedUserId) &&
            Objects.equals(likeImageId, that.likeImageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            image,
            imageType,
            creationDate,
            modificationDate,
            isPrivate,
            commentariesId,
            extendedUserId,
            likeImageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (image != null ? "image=" + image + ", " : "") +
            (imageType != null ? "imageType=" + imageType + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modificationDate != null ? "modificationDate=" + modificationDate + ", " : "") +
            (isPrivate != null ? "isPrivate=" + isPrivate + ", " : "") +
            (commentariesId != null ? "commentariesId=" + commentariesId + ", " : "") +
            (extendedUserId != null ? "extendedUserId=" + extendedUserId + ", " : "") +
            (likeImageId != null ? "likeImageId=" + likeImageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
