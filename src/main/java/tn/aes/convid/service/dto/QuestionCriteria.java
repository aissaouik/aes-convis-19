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
import tn.aes.convid.domain.enumeration.QuestionType;

/**
 * Criteria class for the {@link tn.aes.convid.domain.Question} entity. This class is used
 * in {@link tn.aes.convid.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestionType
     */
    public static class QuestionTypeFilter extends Filter<QuestionType> {

        public QuestionTypeFilter() {}

        public QuestionTypeFilter(QuestionTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestionTypeFilter copy() {
            return new QuestionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private QuestionTypeFilter type;

    private LongFilter choicesId;

    private LongFilter answerId;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.choicesId = other.choicesId == null ? null : other.choicesId.copy();
        this.answerId = other.answerId == null ? null : other.answerId.copy();
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public QuestionTypeFilter getType() {
        return type;
    }

    public void setType(QuestionTypeFilter type) {
        this.type = type;
    }

    public LongFilter getChoicesId() {
        return choicesId;
    }

    public void setChoicesId(LongFilter choicesId) {
        this.choicesId = choicesId;
    }

    public LongFilter getAnswerId() {
        return answerId;
    }

    public void setAnswerId(LongFilter answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(type, that.type) &&
            Objects.equals(choicesId, that.choicesId) &&
            Objects.equals(answerId, that.answerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, choicesId, answerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (choicesId != null ? "choicesId=" + choicesId + ", " : "") +
                (answerId != null ? "answerId=" + answerId + ", " : "") +
            "}";
    }
}
