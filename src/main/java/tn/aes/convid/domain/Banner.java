package tn.aes.convid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Banner entity.\n@author msmida.
 */
@Entity
@Table(name = "banner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Banner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Titre da la bannière
     */
    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    /**
     * Contenu de la bannière
     */
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * Fréquence d'affichage de la bannière en seconde(s)
     */
    @NotNull
    @Column(name = "frequency", nullable = false)
    private Integer frequency;

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

    public Banner title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Banner content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public Banner frequency(Integer frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banner)) {
            return false;
        }
        return id != null && id.equals(((Banner) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banner{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", frequency=" + getFrequency() +
            "}";
    }
}
