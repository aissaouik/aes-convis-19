package tn.aes.convid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.aes.convid.domain.enumeration.QuestionType;

/**
 * Question entity.\n@author msmida.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Titre de la question
     */
    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    /**
     * Type de question
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestionType type;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Choice> choices = new HashSet<>();

    @OneToOne(mappedBy = "question")
    @JsonIgnore
    private Answer answer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Question title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionType getType() {
        return type;
    }

    public Question type(QuestionType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Set<Choice> getChoices() {
        return choices;
    }

    public Question choices(Set<Choice> choices) {
        this.choices = choices;
        return this;
    }

    public Question addChoices(Choice choice) {
        this.choices.add(choice);
        choice.setQuestion(this);
        return this;
    }

    public Question removeChoices(Choice choice) {
        this.choices.remove(choice);
        choice.setQuestion(null);
        return this;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Question answer(Answer answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
