package tn.aes.convid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.aes.convid.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
