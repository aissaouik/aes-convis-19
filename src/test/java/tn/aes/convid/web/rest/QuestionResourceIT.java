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
import tn.aes.convid.domain.Answer;
import tn.aes.convid.domain.Choice;
import tn.aes.convid.domain.Question;
import tn.aes.convid.domain.enumeration.QuestionType;
import tn.aes.convid.repository.QuestionRepository;
import tn.aes.convid.service.QuestionQueryService;
import tn.aes.convid.service.QuestionService;
import tn.aes.convid.service.dto.QuestionCriteria;
import tn.aes.convid.service.dto.QuestionDTO;
import tn.aes.convid.service.mapper.QuestionMapper;

/**
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@SpringBootTest(classes = ConvidApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class QuestionResourceIT {
    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final QuestionType DEFAULT_TYPE = QuestionType.YES_NO;
    private static final QuestionType UPDATED_TYPE = QuestionType.WITH_CHOICE;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionQueryService questionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question().title(DEFAULT_TITLE).type(DEFAULT_TYPE);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question().title(UPDATED_TITLE).type(UPDATED_TYPE);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        restQuestionMockMvc
            .perform(post("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuestion.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setTitle(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setType(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionShouldBeFound("id.equals=" + id);
        defaultQuestionShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title equals to DEFAULT_TITLE
        defaultQuestionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the questionList where title equals to UPDATED_TITLE
        defaultQuestionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title not equals to DEFAULT_TITLE
        defaultQuestionShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the questionList where title not equals to UPDATED_TITLE
        defaultQuestionShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuestionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the questionList where title equals to UPDATED_TITLE
        defaultQuestionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title is not null
        defaultQuestionShouldBeFound("title.specified=true");

        // Get all the questionList where title is null
        defaultQuestionShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title contains DEFAULT_TITLE
        defaultQuestionShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the questionList where title contains UPDATED_TITLE
        defaultQuestionShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where title does not contain DEFAULT_TITLE
        defaultQuestionShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the questionList where title does not contain UPDATED_TITLE
        defaultQuestionShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where type equals to DEFAULT_TYPE
        defaultQuestionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the questionList where type equals to UPDATED_TYPE
        defaultQuestionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where type not equals to DEFAULT_TYPE
        defaultQuestionShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the questionList where type not equals to UPDATED_TYPE
        defaultQuestionShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultQuestionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the questionList where type equals to UPDATED_TYPE
        defaultQuestionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where type is not null
        defaultQuestionShouldBeFound("type.specified=true");

        // Get all the questionList where type is null
        defaultQuestionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByChoicesIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        Choice choices = ChoiceResourceIT.createEntity(em);
        em.persist(choices);
        em.flush();
        question.addChoices(choices);
        questionRepository.saveAndFlush(question);
        Long choicesId = choices.getId();

        // Get all the questionList where choices equals to choicesId
        defaultQuestionShouldBeFound("choicesId.equals=" + choicesId);

        // Get all the questionList where choices equals to choicesId + 1
        defaultQuestionShouldNotBeFound("choicesId.equals=" + (choicesId + 1));
    }

    @Test
    @Transactional
    public void getAllQuestionsByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        Answer answer = AnswerResourceIT.createEntity(em);
        em.persist(answer);
        em.flush();
        question.setAnswer(answer);
        answer.setQuestion(question);
        questionRepository.saveAndFlush(question);
        Long answerId = answer.getId();

        // Get all the questionList where answer equals to answerId
        defaultQuestionShouldBeFound("answerId.equals=" + answerId);

        // Get all the questionList where answer equals to answerId + 1
        defaultQuestionShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restQuestionMockMvc
            .perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc
            .perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion.title(UPDATED_TITLE).type(UPDATED_TYPE);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc
            .perform(put("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuestion.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put("/api/questions").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc
            .perform(delete("/api/questions/{id}", question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
