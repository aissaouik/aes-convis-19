package tn.aes.convid.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.aes.convid.domain.Answer;

/**
 * Spring Data  repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {
    @Query("select answer from Answer answer where answer.user.login = ?#{principal.username}")
    List<Answer> findByUserIsCurrentUser();
}
