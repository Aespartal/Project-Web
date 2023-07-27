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

    private StringFilter title;

    private StringFilter description;

    private StringFilter fileName;

    private StringFilter path;

    private InstantFilter creationDate;

    private InstantFilter modificationDate;

    private BooleanFilter isPrivate;

    private IntegerFilter totalLikes;

    private IntegerFilter totalCommentaries;

    private LongFilter commentariesId;

    private LongFilter extendedUserId;

    private Boolean distinct;

    public ImageCriteria() {}

    public ImageCriteria(ImageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.modificationDate = other.modificationDate == null ? null : other.modificationDate.copy();
        this.isPrivate = other.isPrivate == null ? null : other.isPrivate.copy();
        this.totalLikes = other.totalLikes == null ? null : other.totalLikes.copy();
        this.totalCommentaries = other.totalCommentaries == null ? null : other.totalCommentaries.copy();
        this.commentariesId = other.commentariesId == null ? null : other.commentariesId.copy();
        this.extendedUserId = other.extendedUserId == null ? null : other.extendedUserId.copy();
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getPath() {
        return path;
    }

    public StringFilter path() {
        if (path == null) {
            path = new StringFilter();
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
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

    public IntegerFilter getTotalLikes() {
        return totalLikes;
    }

    public IntegerFilter totalLikes() {
        if (totalLikes == null) {
            totalLikes = new IntegerFilter();
        }
        return totalLikes;
    }

    public void setTotalLikes(IntegerFilter totalLikes) {
        this.totalLikes = totalLikes;
    }

    public IntegerFilter getTotalCommentaries() {
        return totalCommentaries;
    }

    public IntegerFilter totalCommentaries() {
        if (totalCommentaries == null) {
            totalCommentaries = new IntegerFilter();
        }
        return totalCommentaries;
    }

    public void setTotalCommentaries(IntegerFilter totalCommentaries) {
        this.totalCommentaries = totalCommentaries;
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
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(path, that.path) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(modificationDate, that.modificationDate) &&
            Objects.equals(isPrivate, that.isPrivate) &&
            Objects.equals(totalLikes, that.totalLikes) &&
            Objects.equals(totalCommentaries, that.totalCommentaries) &&
            Objects.equals(commentariesId, that.commentariesId) &&
            Objects.equals(extendedUserId, that.extendedUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            fileName,
            path,
            creationDate,
            modificationDate,
            isPrivate,
            totalLikes,
            totalCommentaries,
            commentariesId,
            extendedUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (path != null ? "path=" + path + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (modificationDate != null ? "modificationDate=" + modificationDate + ", " : "") +
            (isPrivate != null ? "isPrivate=" + isPrivate + ", " : "") +
            (totalLikes != null ? "totalLikes=" + totalLikes + ", " : "") +
            (totalCommentaries != null ? "totalCommentaries=" + totalCommentaries + ", " : "") +
            (commentariesId != null ? "commentariesId=" + commentariesId + ", " : "") +
            (extendedUserId != null ? "extendedUserId=" + extendedUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
