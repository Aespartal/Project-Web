package es.project.service.criteria;

import es.project.domain.enumeration.FollowState;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link es.project.domain.Follow} entity. This class is used
 * in {@link es.project.web.rest.FollowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /follows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FollowCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FollowState
     */
    public static class FollowStateFilter extends Filter<FollowState> {

        public FollowStateFilter() {}

        public FollowStateFilter(FollowStateFilter filter) {
            super(filter);
        }

        @Override
        public FollowStateFilter copy() {
            return new FollowStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FollowStateFilter state;

    private InstantFilter creationDate;

    private InstantFilter acceptanceDate;

    private LongFilter followerId;

    private LongFilter followingId;

    private Boolean distinct;

    public FollowCriteria() {}

    public FollowCriteria(FollowCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.acceptanceDate = other.acceptanceDate == null ? null : other.acceptanceDate.copy();
        this.followerId = other.followerId == null ? null : other.followerId.copy();
        this.followingId = other.followingId == null ? null : other.followingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FollowCriteria copy() {
        return new FollowCriteria(this);
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

    public FollowStateFilter getState() {
        return state;
    }

    public FollowStateFilter state() {
        if (state == null) {
            state = new FollowStateFilter();
        }
        return state;
    }

    public void setState(FollowStateFilter state) {
        this.state = state;
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

    public InstantFilter getAcceptanceDate() {
        return acceptanceDate;
    }

    public InstantFilter acceptanceDate() {
        if (acceptanceDate == null) {
            acceptanceDate = new InstantFilter();
        }
        return acceptanceDate;
    }

    public void setAcceptanceDate(InstantFilter acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public LongFilter getFollowerId() {
        return followerId;
    }

    public LongFilter followerId() {
        if (followerId == null) {
            followerId = new LongFilter();
        }
        return followerId;
    }

    public void setFollowerId(LongFilter followerId) {
        this.followerId = followerId;
    }

    public LongFilter getFollowingId() {
        return followingId;
    }

    public LongFilter followingId() {
        if (followingId == null) {
            followingId = new LongFilter();
        }
        return followingId;
    }

    public void setFollowingId(LongFilter followingId) {
        this.followingId = followingId;
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
        final FollowCriteria that = (FollowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(state, that.state) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(acceptanceDate, that.acceptanceDate) &&
            Objects.equals(followerId, that.followerId) &&
            Objects.equals(followingId, that.followingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, creationDate, acceptanceDate, followerId, followingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FollowCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (acceptanceDate != null ? "acceptanceDate=" + acceptanceDate + ", " : "") +
            (followerId != null ? "followerId=" + followerId + ", " : "") +
            (followingId != null ? "followingId=" + followingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
