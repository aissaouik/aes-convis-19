package tn.aes.convid.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link tn.aes.convid.domain.Answer} entity. This class is used
 * in {@link tn.aes.convid.web.rest.AnswerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /answers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnswerCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter anwser;

    private StringFilter comment;

    private ZonedDateTimeFilter date;

    private LongFilter questionId;

    private LongFilter userId;

    public AnswerCriteria() {}

    public AnswerCriteria(AnswerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.anwser = other.anwser == null ? null : other.anwser.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public AnswerCriteria copy() {
        return new AnswerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAnwser() {
        return anwser;
    }

    public void setAnwser(StringFilter anwser) {
        this.anwser = anwser;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnswerCriteria that = (AnswerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anwser, that.anwser) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(date, that.date) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anwser, comment, date, questionId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (anwser != null ? "anwser=" + anwser + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
