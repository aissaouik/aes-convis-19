package tn.aes.convid.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link tn.aes.convid.domain.Choice} entity. This class is used
 * in {@link tn.aes.convid.web.rest.ChoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /choices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChoiceCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter choice;

    private LongFilter questionId;

    public ChoiceCriteria() {}

    public ChoiceCriteria(ChoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.choice = other.choice == null ? null : other.choice.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
    }

    @Override
    public ChoiceCriteria copy() {
        return new ChoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getChoice() {
        return choice;
    }

    public void setChoice(StringFilter choice) {
        this.choice = choice;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChoiceCriteria that = (ChoiceCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(choice, that.choice) && Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, choice, questionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (choice != null ? "choice=" + choice + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
            "}";
    }
}
