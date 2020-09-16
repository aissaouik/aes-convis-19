package tn.aes.convid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Answer entity.\n@author msmida.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * La réponse : YE,NO OR CHOICE
     */
    @NotNull
    @Column(name = "anwser", nullable = false)
    private String anwser;

    /**
     * La réponse : YE,NO OR CHOICE
     */
    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    /**
     * La date de la réponse
     */
    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @OneToOne
    @JoinColumn(unique = true)
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties(value = "answers", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnwser() {
        return anwser;
    }

    public Answer anwser(String anwser) {
        this.anwser = anwser;
        return this;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public String getComment() {
        return comment;
    }

    public Answer comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Answer date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public Answer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", anwser='" + getAnwser() + "'" +
            ", comment='" + getComment() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
