package tn.aes.convid.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.aes.convid.service.dto.QuestionDTO;

/**
 * Service Interface for managing {@link tn.aes.convid.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionDTO> findAll(Pageable pageable);
    /**
     * Get all the QuestionDTO where Answer is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<QuestionDTO> findAllWhereAnswerIsNull();

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
