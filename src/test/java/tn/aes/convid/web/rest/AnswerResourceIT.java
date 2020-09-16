package tn.aes.convid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.aes.convid.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import tn.aes.convid.domain.Question;
import tn.aes.convid.domain.User;
import tn.aes.convid.repository.AnswerRepository;
import tn.aes.convid.service.AnswerQueryService;
import tn.aes.convid.service.AnswerService;
import tn.aes.convid.service.dto.AnswerCriteria;
import tn.aes.convid.service.dto.AnswerDTO;
import tn.aes.convid.service.mapper.AnswerMapper;

/**
 * Integration tests for the {@link AnswerResource} REST controller.
 */
@SpringBootTest(classes = ConvidApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnswerResourceIT {
    private static final String DEFAULT_ANWSER = "AAAAAAAAAA";
    private static final String UPDATED_ANWSER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerQueryService answerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnswerMockMvc;

    private Answer answer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer().anwser(DEFAULT_ANWSER).comment(DEFAULT_COMMENT).date(DEFAULT_DATE);
        return answer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createUpdatedEntity(EntityManager em) {
        Answer answer = new Answer().anwser(UPDATED_ANWSER).comment(UPDATED_COMMENT).date(UPDATED_DATE);
        return answer;
    }

    @BeforeEach
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();
        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);
        restAnswerMockMvc
            .perform(post("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getAnwser()).isEqualTo(DEFAULT_ANWSER);
        assertThat(testAnswer.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testAnswer.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer with an existing ID
        answer.setId(1L);
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerMockMvc
            .perform(post("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAnwserIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setAnwser(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc
            .perform(post("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setComment(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc
            .perform(post("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setDate(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc
            .perform(post("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList
        restAnswerMockMvc
            .perform(get("/api/answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].anwser").value(hasItem(DEFAULT_ANWSER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc
            .perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.anwser").value(DEFAULT_ANWSER))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getAnswersByIdFiltering() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        Long id = answer.getId();

        defaultAnswerShouldBeFound("id.equals=" + id);
        defaultAnswerShouldNotBeFound("id.notEquals=" + id);

        defaultAnswerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnswerShouldNotBeFound("id.greaterThan=" + id);

        defaultAnswerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnswerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser equals to DEFAULT_ANWSER
        defaultAnswerShouldBeFound("anwser.equals=" + DEFAULT_ANWSER);

        // Get all the answerList where anwser equals to UPDATED_ANWSER
        defaultAnswerShouldNotBeFound("anwser.equals=" + UPDATED_ANWSER);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser not equals to DEFAULT_ANWSER
        defaultAnswerShouldNotBeFound("anwser.notEquals=" + DEFAULT_ANWSER);

        // Get all the answerList where anwser not equals to UPDATED_ANWSER
        defaultAnswerShouldBeFound("anwser.notEquals=" + UPDATED_ANWSER);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser in DEFAULT_ANWSER or UPDATED_ANWSER
        defaultAnswerShouldBeFound("anwser.in=" + DEFAULT_ANWSER + "," + UPDATED_ANWSER);

        // Get all the answerList where anwser equals to UPDATED_ANWSER
        defaultAnswerShouldNotBeFound("anwser.in=" + UPDATED_ANWSER);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser is not null
        defaultAnswerShouldBeFound("anwser.specified=true");

        // Get all the answerList where anwser is null
        defaultAnswerShouldNotBeFound("anwser.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser contains DEFAULT_ANWSER
        defaultAnswerShouldBeFound("anwser.contains=" + DEFAULT_ANWSER);

        // Get all the answerList where anwser contains UPDATED_ANWSER
        defaultAnswerShouldNotBeFound("anwser.contains=" + UPDATED_ANWSER);
    }

    @Test
    @Transactional
    public void getAllAnswersByAnwserNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where anwser does not contain DEFAULT_ANWSER
        defaultAnswerShouldNotBeFound("anwser.doesNotContain=" + DEFAULT_ANWSER);

        // Get all the answerList where anwser does not contain UPDATED_ANWSER
        defaultAnswerShouldBeFound("anwser.doesNotContain=" + UPDATED_ANWSER);
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment equals to DEFAULT_COMMENT
        defaultAnswerShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the answerList where comment equals to UPDATED_COMMENT
        defaultAnswerShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment not equals to DEFAULT_COMMENT
        defaultAnswerShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the answerList where comment not equals to UPDATED_COMMENT
        defaultAnswerShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultAnswerShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the answerList where comment equals to UPDATED_COMMENT
        defaultAnswerShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment is not null
        defaultAnswerShouldBeFound("comment.specified=true");

        // Get all the answerList where comment is null
        defaultAnswerShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment contains DEFAULT_COMMENT
        defaultAnswerShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the answerList where comment contains UPDATED_COMMENT
        defaultAnswerShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAnswersByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where comment does not contain DEFAULT_COMMENT
        defaultAnswerShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the answerList where comment does not contain UPDATED_COMMENT
        defaultAnswerShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date equals to DEFAULT_DATE
        defaultAnswerShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the answerList where date equals to UPDATED_DATE
        defaultAnswerShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date not equals to DEFAULT_DATE
        defaultAnswerShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the answerList where date not equals to UPDATED_DATE
        defaultAnswerShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date in DEFAULT_DATE or UPDATED_DATE
        defaultAnswerShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the answerList where date equals to UPDATED_DATE
        defaultAnswerShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is not null
        defaultAnswerShouldBeFound("date.specified=true");

        // Get all the answerList where date is null
        defaultAnswerShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is greater than or equal to DEFAULT_DATE
        defaultAnswerShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the answerList where date is greater than or equal to UPDATED_DATE
        defaultAnswerShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is less than or equal to DEFAULT_DATE
        defaultAnswerShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the answerList where date is less than or equal to SMALLER_DATE
        defaultAnswerShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is less than DEFAULT_DATE
        defaultAnswerShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the answerList where date is less than UPDATED_DATE
        defaultAnswerShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList where date is greater than DEFAULT_DATE
        defaultAnswerShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the answerList where date is greater than SMALLER_DATE
        defaultAnswerShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllAnswersByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        Question question = QuestionResourceIT.createEntity(em);
        em.persist(question);
        em.flush();
        answer.setQuestion(question);
        answerRepository.saveAndFlush(answer);
        Long questionId = question.getId();

        // Get all the answerList where question equals to questionId
        defaultAnswerShouldBeFound("questionId.equals=" + questionId);

        // Get all the answerList where question equals to questionId + 1
        defaultAnswerShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    public void getAllAnswersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        answer.setUser(user);
        answerRepository.saveAndFlush(answer);
        Long userId = user.getId();

        // Get all the answerList where user equals to userId
        defaultAnswerShouldBeFound("userId.equals=" + userId);

        // Get all the answerList where user equals to userId + 1
        defaultAnswerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnswerShouldBeFound(String filter) throws Exception {
        restAnswerMockMvc
            .perform(get("/api/answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].anwser").value(hasItem(DEFAULT_ANWSER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));

        // Check, that the count call also returns 1
        restAnswerMockMvc
            .perform(get("/api/answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnswerShouldNotBeFound(String filter) throws Exception {
        restAnswerMockMvc
            .perform(get("/api/answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnswerMockMvc
            .perform(get("/api/answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();
        // Disconnect from session so that the updates on updatedAnswer are not directly saved in db
        em.detach(updatedAnswer);
        updatedAnswer.anwser(UPDATED_ANWSER).comment(UPDATED_COMMENT).date(UPDATED_DATE);
        AnswerDTO answerDTO = answerMapper.toDto(updatedAnswer);

        restAnswerMockMvc
            .perform(put("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getAnwser()).isEqualTo(UPDATED_ANWSER);
        assertThat(testAnswer.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAnswer.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc
            .perform(put("/api/answers").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Delete the answer
        restAnswerMockMvc
            .perform(delete("/api/answers/{id}", answer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
