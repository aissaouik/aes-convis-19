package tn.aes.convid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChoiceMapperTest {
    private ChoiceMapper choiceMapper;

    @BeforeEach
    public void setUp() {
        choiceMapper = new ChoiceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(choiceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(choiceMapper.fromId(null)).isNull();
    }
}
