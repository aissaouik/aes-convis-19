package tn.aes.convid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionMapperTest {
    private QuestionMapper questionMapper;

    @BeforeEach
    public void setUp() {
        questionMapper = new QuestionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(questionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questionMapper.fromId(null)).isNull();
    }
}
