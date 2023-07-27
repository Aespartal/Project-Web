package es.project.service.criteria;

import es.project.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link es.project.domain.Notification} entity. This class is used
 * in {@link es.project.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private NotificationTypeFilter type;

    private InstantFilter creationDate;

    private LongFilter imageId;

    private LongFilter commentaryId;

    private LongFilter notifierId;

    private LongFilter notifyingId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.imageId = other.imageId == null ? null : other.imageId.copy();
        this.commentaryId = other.commentaryId == null ? null : other.commentaryId.copy();
        this.notifierId = other.notifierId == null ? null : other.notifierId.copy();
        this.notifyingId = other.notifyingId == null ? null : other.notifyingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getMessage() {
        return message;
    }

    public StringFilter message() {
        if (message == null) {
            message = new StringFilter();
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public NotificationTypeFilter getType() {
        return type;
    }

    public NotificationTypeFilter type() {
        if (type == null) {
            type = new NotificationTypeFilter();
        }
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
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

    public LongFilter getImageId() {
        return imageId;
    }

    public LongFilter imageId() {
        if (imageId == null) {
            imageId = new LongFilter();
        }
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getCommentaryId() {
        return commentaryId;
    }

    public LongFilter commentaryId() {
        if (commentaryId == null) {
            commentaryId = new LongFilter();
        }
        return commentaryId;
    }

    public void setCommentaryId(LongFilter commentaryId) {
        this.commentaryId = commentaryId;
    }

    public LongFilter getNotifierId() {
        return notifierId;
    }

    public LongFilter notifierId() {
        if (notifierId == null) {
            notifierId = new LongFilter();
        }
        return notifierId;
    }

    public void setNotifierId(LongFilter notifierId) {
        this.notifierId = notifierId;
    }

    public LongFilter getNotifyingId() {
        return notifyingId;
    }

    public LongFilter notifyingId() {
        if (notifyingId == null) {
            notifyingId = new LongFilter();
        }
        return notifyingId;
    }

    public void setNotifyingId(LongFilter notifyingId) {
        this.notifyingId = notifyingId;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(type, that.type) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(commentaryId, that.commentaryId) &&
            Objects.equals(notifierId, that.notifierId) &&
            Objects.equals(notifyingId, that.notifyingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, type, creationDate, imageId, commentaryId, notifierId, notifyingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (imageId != null ? "imageId=" + imageId + ", " : "") +
            (commentaryId != null ? "commentaryId=" + commentaryId + ", " : "") +
            (notifierId != null ? "notifierId=" + notifierId + ", " : "") +
            (notifyingId != null ? "notifyingId=" + notifyingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
