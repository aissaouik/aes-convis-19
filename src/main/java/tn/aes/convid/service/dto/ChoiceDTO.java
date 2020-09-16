package tn.aes.convid.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tn.aes.convid.domain.Choice} entity.
 */
@ApiModel(description = "Choice entity.\n@author msmida.")
public class ChoiceDTO implements Serializable {
    private Long id;

    /**
     * Date/heure du début du cycle
     */
    @NotNull
    @ApiModelProperty(value = "Date/heure du début du cycle", required = true)
    private String choice;

    private Long questionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChoiceDTO)) {
            return false;
        }

        return id != null && id.equals(((ChoiceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChoiceDTO{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", questionId=" + getQuestionId() +
            "}";
    }
}
