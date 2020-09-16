package tn.aes.convid.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.aes.convid.service.dto.ChoiceDTO;

/**
 * Service Interface for managing {@link tn.aes.convid.domain.Choice}.
 */
public interface ChoiceService {
    /**
     * Save a choice.
     *
     * @param choiceDTO the entity to save.
     * @return the persisted entity.
     */
    ChoiceDTO save(ChoiceDTO choiceDTO);

    /**
     * Get all the choices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChoiceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" choice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChoiceDTO> findOne(Long id);

    /**
     * Delete the "id" choice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
