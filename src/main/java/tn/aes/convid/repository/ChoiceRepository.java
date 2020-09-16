package tn.aes.convid.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.aes.convid.domain.Choice;

/**
 * Spring Data  repository for the Choice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long>, JpaSpecificationExecutor<Choice> {}
