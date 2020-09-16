package tn.aes.convid.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tn.aes.convid.domain.Answer} entity.
 */
@ApiModel(description = "Answer entity.\n@author msmida.")
public class AnswerDTO implements Serializable {
    private Long id;

    /**
     * La réponse : YE,NO OR CHOICE
     */
    @NotNull
    @ApiModelProperty(value = "La réponse : YE,NO OR CHOICE", required = true)
    private String anwser;

    /**
     * La réponse : YE,NO OR CHOICE
     */
    @NotNull
    @ApiModelProperty(value = "La réponse : YE,NO OR CHOICE", required = true)
    private String comment;

    /**
     * La date de la réponse
     */
    @NotNull
    @ApiModelProperty(value = "La date de la réponse", required = true)
    private ZonedDateTime date;

    private Long questionId;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerDTO)) {
            return false;
        }

        return id != null && id.equals(((AnswerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", anwser='" + getAnwser() + "'" +
            ", comment='" + getComment() + "'" +
            ", date='" + getDate() + "'" +
            ", questionId=" + getQuestionId() +
            ", userId=" + getUserId() +
            "}";
    }
}
