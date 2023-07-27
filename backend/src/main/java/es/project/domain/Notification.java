package es.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.project.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * message
     */
    @NotNull
    @Size(max = 3500)
    @Column(name = "message", length = 3500, nullable = false)
    private String message;

    /**
     * type
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    /**
     * creationDate
     */
    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser image;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser commentary;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser notifier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ExtendedUser notifying;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Notification creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ExtendedUser getImage() {
        return this.image;
    }

    public void setImage(ExtendedUser extendedUser) {
        this.image = extendedUser;
    }

    public Notification image(ExtendedUser extendedUser) {
        this.setImage(extendedUser);
        return this;
    }

    public ExtendedUser getCommentary() {
        return this.commentary;
    }

    public void setCommentary(ExtendedUser extendedUser) {
        this.commentary = extendedUser;
    }

    public Notification commentary(ExtendedUser extendedUser) {
        this.setCommentary(extendedUser);
        return this;
    }

    public ExtendedUser getNotifier() {
        return this.notifier;
    }

    public void setNotifier(ExtendedUser extendedUser) {
        this.notifier = extendedUser;
    }

    public Notification notifier(ExtendedUser extendedUser) {
        this.setNotifier(extendedUser);
        return this;
    }

    public ExtendedUser getNotifying() {
        return this.notifying;
    }

    public void setNotifying(ExtendedUser extendedUser) {
        this.notifying = extendedUser;
    }

    public Notification notifying(ExtendedUser extendedUser) {
        this.setNotifying(extendedUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", type='" + getType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
