package tn.aes.convid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.aes.convid.web.rest.TestUtil;

public class ChoiceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChoiceDTO.class);
        ChoiceDTO choiceDTO1 = new ChoiceDTO();
        choiceDTO1.setId(1L);
        ChoiceDTO choiceDTO2 = new ChoiceDTO();
        assertThat(choiceDTO1).isNotEqualTo(choiceDTO2);
        choiceDTO2.setId(choiceDTO1.getId());
        assertThat(choiceDTO1).isEqualTo(choiceDTO2);
        choiceDTO2.setId(2L);
        assertThat(choiceDTO1).isNotEqualTo(choiceDTO2);
        choiceDTO1.setId(null);
        assertThat(choiceDTO1).isNotEqualTo(choiceDTO2);
    }
}
