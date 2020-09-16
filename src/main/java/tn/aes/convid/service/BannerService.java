package tn.aes.convid.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.aes.convid.service.dto.BannerDTO;

/**
 * Service Interface for managing {@link tn.aes.convid.domain.Banner}.
 */
public interface BannerService {
    /**
     * Save a banner.
     *
     * @param bannerDTO the entity to save.
     * @return the persisted entity.
     */
    BannerDTO save(BannerDTO bannerDTO);

    /**
     * Get all the banners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BannerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" banner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BannerDTO> findOne(Long id);

    /**
     * Delete the "id" banner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
