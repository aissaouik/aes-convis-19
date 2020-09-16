package tn.aes.convid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.aes.convid.ConvidApp;
import tn.aes.convid.domain.Banner;
import tn.aes.convid.repository.BannerRepository;
import tn.aes.convid.service.BannerQueryService;
import tn.aes.convid.service.BannerService;
import tn.aes.convid.service.dto.BannerCriteria;
import tn.aes.convid.service.dto.BannerDTO;
import tn.aes.convid.service.mapper.BannerMapper;

/**
 * Integration tests for the {@link BannerResource} REST controller.
 */
@SpringBootTest(classes = ConvidApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BannerResourceIT {
    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_FREQUENCY = 1;
    private static final Integer UPDATED_FREQUENCY = 2;
    private static final Integer SMALLER_FREQUENCY = 1 - 1;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerQueryService bannerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBannerMockMvc;

    private Banner banner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banner createEntity(EntityManager em) {
        Banner banner = new Banner().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).frequency(DEFAULT_FREQUENCY);
        return banner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banner createUpdatedEntity(EntityManager em) {
        Banner banner = new Banner().title(UPDATED_TITLE).content(UPDATED_CONTENT).frequency(UPDATED_FREQUENCY);
        return banner;
    }

    @BeforeEach
    public void initTest() {
        banner = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanner() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();
        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);
        restBannerMockMvc
            .perform(post("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isCreated());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate + 1);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBanner.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBanner.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    @Transactional
    public void createBannerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();

        // Create the Banner with an existing ID
        banner.setId(1L);
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerMockMvc
            .perform(post("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerRepository.findAll().size();
        // set the field null
        banner.setTitle(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerRepository.findAll().size();
        // set the field null
        banner.setContent(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerRepository.findAll().size();
        // set the field null
        banner.setFrequency(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanners() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList
        restBannerMockMvc
            .perform(get("/api/banners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)));
    }

    @Test
    @Transactional
    public void getBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get the banner
        restBannerMockMvc
            .perform(get("/api/banners/{id}", banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banner.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY));
    }

    @Test
    @Transactional
    public void getBannersByIdFiltering() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        Long id = banner.getId();

        defaultBannerShouldBeFound("id.equals=" + id);
        defaultBannerShouldNotBeFound("id.notEquals=" + id);

        defaultBannerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBannerShouldNotBeFound("id.greaterThan=" + id);

        defaultBannerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBannerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllBannersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title equals to DEFAULT_TITLE
        defaultBannerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the bannerList where title equals to UPDATED_TITLE
        defaultBannerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBannersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title not equals to DEFAULT_TITLE
        defaultBannerShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the bannerList where title not equals to UPDATED_TITLE
        defaultBannerShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBannersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBannerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the bannerList where title equals to UPDATED_TITLE
        defaultBannerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBannersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title is not null
        defaultBannerShouldBeFound("title.specified=true");

        // Get all the bannerList where title is null
        defaultBannerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllBannersByTitleContainsSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title contains DEFAULT_TITLE
        defaultBannerShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the bannerList where title contains UPDATED_TITLE
        defaultBannerShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBannersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where title does not contain DEFAULT_TITLE
        defaultBannerShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the bannerList where title does not contain UPDATED_TITLE
        defaultBannerShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBannersByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content equals to DEFAULT_CONTENT
        defaultBannerShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the bannerList where content equals to UPDATED_CONTENT
        defaultBannerShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBannersByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content not equals to DEFAULT_CONTENT
        defaultBannerShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the bannerList where content not equals to UPDATED_CONTENT
        defaultBannerShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBannersByContentIsInShouldWork() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultBannerShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the bannerList where content equals to UPDATED_CONTENT
        defaultBannerShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBannersByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content is not null
        defaultBannerShouldBeFound("content.specified=true");

        // Get all the bannerList where content is null
        defaultBannerShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    public void getAllBannersByContentContainsSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content contains DEFAULT_CONTENT
        defaultBannerShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the bannerList where content contains UPDATED_CONTENT
        defaultBannerShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBannersByContentNotContainsSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where content does not contain DEFAULT_CONTENT
        defaultBannerShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the bannerList where content does not contain UPDATED_CONTENT
        defaultBannerShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency equals to DEFAULT_FREQUENCY
        defaultBannerShouldBeFound("frequency.equals=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency equals to UPDATED_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.equals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency not equals to DEFAULT_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.notEquals=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency not equals to UPDATED_FREQUENCY
        defaultBannerShouldBeFound("frequency.notEquals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency in DEFAULT_FREQUENCY or UPDATED_FREQUENCY
        defaultBannerShouldBeFound("frequency.in=" + DEFAULT_FREQUENCY + "," + UPDATED_FREQUENCY);

        // Get all the bannerList where frequency equals to UPDATED_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.in=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency is not null
        defaultBannerShouldBeFound("frequency.specified=true");

        // Get all the bannerList where frequency is null
        defaultBannerShouldNotBeFound("frequency.specified=false");
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency is greater than or equal to DEFAULT_FREQUENCY
        defaultBannerShouldBeFound("frequency.greaterThanOrEqual=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency is greater than or equal to UPDATED_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.greaterThanOrEqual=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency is less than or equal to DEFAULT_FREQUENCY
        defaultBannerShouldBeFound("frequency.lessThanOrEqual=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency is less than or equal to SMALLER_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.lessThanOrEqual=" + SMALLER_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsLessThanSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency is less than DEFAULT_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.lessThan=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency is less than UPDATED_FREQUENCY
        defaultBannerShouldBeFound("frequency.lessThan=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void getAllBannersByFrequencyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList where frequency is greater than DEFAULT_FREQUENCY
        defaultBannerShouldNotBeFound("frequency.greaterThan=" + DEFAULT_FREQUENCY);

        // Get all the bannerList where frequency is greater than SMALLER_FREQUENCY
        defaultBannerShouldBeFound("frequency.greaterThan=" + SMALLER_FREQUENCY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBannerShouldBeFound(String filter) throws Exception {
        restBannerMockMvc
            .perform(get("/api/banners?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)));

        // Check, that the count call also returns 1
        restBannerMockMvc
            .perform(get("/api/banners/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBannerShouldNotBeFound(String filter) throws Exception {
        restBannerMockMvc
            .perform(get("/api/banners?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBannerMockMvc
            .perform(get("/api/banners/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBanner() throws Exception {
        // Get the banner
        restBannerMockMvc.perform(get("/api/banners/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Update the banner
        Banner updatedBanner = bannerRepository.findById(banner.getId()).get();
        // Disconnect from session so that the updates on updatedBanner are not directly saved in db
        em.detach(updatedBanner);
        updatedBanner.title(UPDATED_TITLE).content(UPDATED_CONTENT).frequency(UPDATED_FREQUENCY);
        BannerDTO bannerDTO = bannerMapper.toDto(updatedBanner);

        restBannerMockMvc
            .perform(put("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isOk());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBanner.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBanner.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void updateNonExistingBanner() throws Exception {
        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(put("/api/banners").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        int databaseSizeBeforeDelete = bannerRepository.findAll().size();

        // Delete the banner
        restBannerMockMvc
            .perform(delete("/api/banners/{id}", banner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
