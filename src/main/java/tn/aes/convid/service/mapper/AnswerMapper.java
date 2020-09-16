package tn.aes.convid.service.mapper;

import org.mapstruct.*;
import tn.aes.convid.domain.*;
import tn.aes.convid.service.dto.AnswerDTO;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class, UserMapper.class })
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "user.id", target = "userId")
    AnswerDTO toDto(Answer answer);

    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "userId", target = "user")
    Answer toEntity(AnswerDTO answerDTO);

    default Answer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setId(id);
        return answer;
    }
}
