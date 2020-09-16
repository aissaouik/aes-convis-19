package tn.aes.convid.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tn.aes.convid.domain.Banner} entity.
 */
@ApiModel(description = "Banner entity.\n@author msmida.")
public class BannerDTO implements Serializable {
    private Long id;

    /**
     * Titre da la bannière
     */
    @NotNull
    @ApiModelProperty(value = "Titre da la bannière", required = true)
    private String title;

    /**
     * Contenu de la bannière
     */
    @NotNull
    @ApiModelProperty(value = "Contenu de la bannière", required = true)
    private String content;

    /**
     * Fréquence d'affichage de la bannière en seconde(s)
     */
    @NotNull
    @ApiModelProperty(value = "Fréquence d'affichage de la bannière en seconde(s)", required = true)
    private Integer frequency;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BannerDTO)) {
            return false;
        }

        return id != null && id.equals(((BannerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BannerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", frequency=" + getFrequency() +
            "}";
    }
}
