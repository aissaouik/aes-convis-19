package tn.aes.convid.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.*;
import tn.aes.convid.domain.enumeration.QuestionType;

/**
 * A DTO for the {@link tn.aes.convid.domain.Question} entity.
 */
@ApiModel(description = "Question entity.\n@author msmida.")
public class QuestionDTO implements Serializable {
    private Long id;

    /**
     * Titre de la question
     */
    @NotNull
    @ApiModelProperty(value = "Titre de la question", required = true)
    private String title;

    /**
     * Type de question
     */
    @NotNull
    @ApiModelProperty(value = "Type de question", required = true)
    private QuestionType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        return id != null && id.equals(((QuestionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
