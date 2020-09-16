package tn.aes.convid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Choice entity.\n@author msmida.
 */
@Entity
@Table(name = "choice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Choice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Date/heure du début du cycle
     */
    @NotNull
    @Column(name = "choice", nullable = false)
    private String choice;

    @ManyToOne
    @JsonIgnoreProperties(value = "choices", allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public Choice choice(String choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Question getQuestion() {
        return question;
    }

    public Choice question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Choice)) {
            return false;
        }
        return id != null && id.equals(((Choice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Choice{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            "}";
    }
}
