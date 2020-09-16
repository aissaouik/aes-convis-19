package tn.aes.convid.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.aes.convid.service.BannerQueryService;
import tn.aes.convid.service.BannerService;
import tn.aes.convid.service.dto.BannerCriteria;
import tn.aes.convid.service.dto.BannerDTO;
import tn.aes.convid.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.aes.convid.domain.Banner}.
 */
@RestController
@RequestMapping("/api")
public class BannerResource {
    private final Logger log = LoggerFactory.getLogger(BannerResource.class);

    private static final String ENTITY_NAME = "banner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BannerService bannerService;

    private final BannerQueryService bannerQueryService;

    public BannerResource(BannerService bannerService, BannerQueryService bannerQueryService) {
        this.bannerService = bannerService;
        this.bannerQueryService = bannerQueryService;
    }

    /**
     * {@code POST  /banners} : Create a new banner.
     *
     * @param bannerDTO the bannerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bannerDTO, or with status {@code 400 (Bad Request)} if the banner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banners")
    public ResponseEntity<BannerDTO> createBanner(@Valid @RequestBody BannerDTO bannerDTO) throws URISyntaxException {
        log.debug("REST request to save Banner : {}", bannerDTO);
        if (bannerDTO.getId() != null) {
            throw new BadRequestAlertException("A new banner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BannerDTO result = bannerService.save(bannerDTO);
        return ResponseEntity
            .created(new URI("/api/banners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banners} : Updates an existing banner.
     *
     * @param bannerDTO the bannerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bannerDTO,
     * or with status {@code 400 (Bad Request)} if the bannerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bannerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banners")
    public ResponseEntity<BannerDTO> updateBanner(@Valid @RequestBody BannerDTO bannerDTO) throws URISyntaxException {
        log.debug("REST request to update Banner : {}", bannerDTO);
        if (bannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BannerDTO result = bannerService.save(bannerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bannerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /banners} : get all the banners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banners in body.
     */
    @GetMapping("/banners")
    public ResponseEntity<List<BannerDTO>> getAllBanners(BannerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Banners by criteria: {}", criteria);
        Page<BannerDTO> page = bannerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banners/count} : count all the banners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/banners/count")
    public ResponseEntity<Long> countBanners(BannerCriteria criteria) {
        log.debug("REST request to count Banners by criteria: {}", criteria);
        return ResponseEntity.ok().body(bannerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /banners/:id} : get the "id" banner.
     *
     * @param id the id of the bannerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bannerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banners/{id}")
    public ResponseEntity<BannerDTO> getBanner(@PathVariable Long id) {
        log.debug("REST request to get Banner : {}", id);
        Optional<BannerDTO> bannerDTO = bannerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bannerDTO);
    }

    /**
     * {@code DELETE  /banners/:id} : delete the "id" banner.
     *
     * @param id the id of the bannerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banners/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        log.debug("REST request to delete Banner : {}", id);
        bannerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
