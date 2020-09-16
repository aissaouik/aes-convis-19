package tn.aes.convid.service.mapper;

import org.mapstruct.*;
import tn.aes.convid.domain.*;
import tn.aes.convid.service.dto.QuestionDTO;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "choices", ignore = true)
    @Mapping(target = "removeChoices", ignore = true)
    @Mapping(target = "answer", ignore = true)
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
