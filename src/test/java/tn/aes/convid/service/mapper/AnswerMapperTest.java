package tn.aes.convid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnswerMapperTest {
    private AnswerMapper answerMapper;

    @BeforeEach
    public void setUp() {
        answerMapper = new AnswerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(answerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(answerMapper.fromId(null)).isNull();
    }
}
