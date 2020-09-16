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
import tn.aes.convid.domain.Choice;
import tn.aes.convid.domain.Question;
import tn.aes.convid.repository.ChoiceRepository;
import tn.aes.convid.service.ChoiceQueryService;
import tn.aes.convid.service.ChoiceService;
import tn.aes.convid.service.dto.ChoiceCriteria;
import tn.aes.convid.service.dto.ChoiceDTO;
import tn.aes.convid.service.mapper.ChoiceMapper;

/**
 * Integration tests for the {@link ChoiceResource} REST controller.
 */
@SpringBootTest(classes = ConvidApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ChoiceResourceIT {
    private static final String DEFAULT_CHOICE = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE = "BBBBBBBBBB";

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private ChoiceMapper choiceMapper;

    @Autowired
    private ChoiceService choiceService;

    @Autowired
    private ChoiceQueryService choiceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChoiceMockMvc;

    private Choice choice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Choice createEntity(EntityManager em) {
        Choice choice = new Choice().choice(DEFAULT_CHOICE);
        return choice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Choice createUpdatedEntity(EntityManager em) {
        Choice choice = new Choice().choice(UPDATED_CHOICE);
        return choice;
    }

    @BeforeEach
    public void initTest() {
        choice = createEntity(em);
    }

    @Test
    @Transactional
    public void createChoice() throws Exception {
        int databaseSizeBeforeCreate = choiceRepository.findAll().size();
        // Create the Choice
        ChoiceDTO choiceDTO = choiceMapper.toDto(choice);
        restChoiceMockMvc
            .perform(post("/api/choices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Choice in the database
        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeCreate + 1);
        Choice testChoice = choiceList.get(choiceList.size() - 1);
        assertThat(testChoice.getChoice()).isEqualTo(DEFAULT_CHOICE);
    }

    @Test
    @Transactional
    public void createChoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = choiceRepository.findAll().size();

        // Create the Choice with an existing ID
        choice.setId(1L);
        ChoiceDTO choiceDTO = choiceMapper.toDto(choice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChoiceMockMvc
            .perform(post("/api/choices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Choice in the database
        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = choiceRepository.findAll().size();
        // set the field null
        choice.setChoice(null);

        // Create the Choice, which fails.
        ChoiceDTO choiceDTO = choiceMapper.toDto(choice);

        restChoiceMockMvc
            .perform(post("/api/choices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choiceDTO)))
            .andExpect(status().isBadRequest());

        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChoices() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList
        restChoiceMockMvc
            .perform(get("/api/choices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(choice.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE)));
    }

    @Test
    @Transactional
    public void getChoice() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get the choice
        restChoiceMockMvc
            .perform(get("/api/choices/{id}", choice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(choice.getId().intValue()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE));
    }

    @Test
    @Transactional
    public void getChoicesByIdFiltering() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        Long id = choice.getId();

        defaultChoiceShouldBeFound("id.equals=" + id);
        defaultChoiceShouldNotBeFound("id.notEquals=" + id);

        defaultChoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultChoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice equals to DEFAULT_CHOICE
        defaultChoiceShouldBeFound("choice.equals=" + DEFAULT_CHOICE);

        // Get all the choiceList where choice equals to UPDATED_CHOICE
        defaultChoiceShouldNotBeFound("choice.equals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice not equals to DEFAULT_CHOICE
        defaultChoiceShouldNotBeFound("choice.notEquals=" + DEFAULT_CHOICE);

        // Get all the choiceList where choice not equals to UPDATED_CHOICE
        defaultChoiceShouldBeFound("choice.notEquals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice in DEFAULT_CHOICE or UPDATED_CHOICE
        defaultChoiceShouldBeFound("choice.in=" + DEFAULT_CHOICE + "," + UPDATED_CHOICE);

        // Get all the choiceList where choice equals to UPDATED_CHOICE
        defaultChoiceShouldNotBeFound("choice.in=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice is not null
        defaultChoiceShouldBeFound("choice.specified=true");

        // Get all the choiceList where choice is null
        defaultChoiceShouldNotBeFound("choice.specified=false");
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceContainsSomething() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice contains DEFAULT_CHOICE
        defaultChoiceShouldBeFound("choice.contains=" + DEFAULT_CHOICE);

        // Get all the choiceList where choice contains UPDATED_CHOICE
        defaultChoiceShouldNotBeFound("choice.contains=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllChoicesByChoiceNotContainsSomething() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        // Get all the choiceList where choice does not contain DEFAULT_CHOICE
        defaultChoiceShouldNotBeFound("choice.doesNotContain=" + DEFAULT_CHOICE);

        // Get all the choiceList where choice does not contain UPDATED_CHOICE
        defaultChoiceShouldBeFound("choice.doesNotContain=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllChoicesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);
        Question question = QuestionResourceIT.createEntity(em);
        em.persist(question);
        em.flush();
        choice.setQuestion(question);
        choiceRepository.saveAndFlush(choice);
        Long questionId = question.getId();

        // Get all the choiceList where question equals to questionId
        defaultChoiceShouldBeFound("questionId.equals=" + questionId);

        // Get all the choiceList where question equals to questionId + 1
        defaultChoiceShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChoiceShouldBeFound(String filter) throws Exception {
        restChoiceMockMvc
            .perform(get("/api/choices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(choice.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE)));

        // Check, that the count call also returns 1
        restChoiceMockMvc
            .perform(get("/api/choices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChoiceShouldNotBeFound(String filter) throws Exception {
        restChoiceMockMvc
            .perform(get("/api/choices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChoiceMockMvc
            .perform(get("/api/choices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingChoice() throws Exception {
        // Get the choice
        restChoiceMockMvc.perform(get("/api/choices/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChoice() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        int databaseSizeBeforeUpdate = choiceRepository.findAll().size();

        // Update the choice
        Choice updatedChoice = choiceRepository.findById(choice.getId()).get();
        // Disconnect from session so that the updates on updatedChoice are not directly saved in db
        em.detach(updatedChoice);
        updatedChoice.choice(UPDATED_CHOICE);
        ChoiceDTO choiceDTO = choiceMapper.toDto(updatedChoice);

        restChoiceMockMvc
            .perform(put("/api/choices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choiceDTO)))
            .andExpect(status().isOk());

        // Validate the Choice in the database
        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeUpdate);
        Choice testChoice = choiceList.get(choiceList.size() - 1);
        assertThat(testChoice.getChoice()).isEqualTo(UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void updateNonExistingChoice() throws Exception {
        int databaseSizeBeforeUpdate = choiceRepository.findAll().size();

        // Create the Choice
        ChoiceDTO choiceDTO = choiceMapper.toDto(choice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChoiceMockMvc
            .perform(put("/api/choices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Choice in the database
        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChoice() throws Exception {
        // Initialize the database
        choiceRepository.saveAndFlush(choice);

        int databaseSizeBeforeDelete = choiceRepository.findAll().size();

        // Delete the choice
        restChoiceMockMvc
            .perform(delete("/api/choices/{id}", choice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Choice> choiceList = choiceRepository.findAll();
        assertThat(choiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
