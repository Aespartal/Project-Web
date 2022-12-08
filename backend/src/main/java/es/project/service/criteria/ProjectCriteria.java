package es.project.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link es.project.domain.Project} entity. This class is used
 * in {@link es.project.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter link;

    private IntegerFilter order;

    private InstantFilter creationDate;

    private BooleanFilter isPrivate;

    private BooleanFilter active;

    private LongFilter extendedUserId;

    private Boolean distinct;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.isPrivate = other.isPrivate == null ? null : other.isPrivate.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.extendedUserId = other.extendedUserId == null ? null : other.extendedUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
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

    public StringFilter getLink() {
        return link;
    }

    public StringFilter link() {
        if (link == null) {
            link = new StringFilter();
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
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

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(link, that.link) &&
            Objects.equals(order, that.order) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(isPrivate, that.isPrivate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(extendedUserId, that.extendedUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link, order, creationDate, isPrivate, active, extendedUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (isPrivate != null ? "isPrivate=" + isPrivate + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (extendedUserId != null ? "extendedUserId=" + extendedUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
