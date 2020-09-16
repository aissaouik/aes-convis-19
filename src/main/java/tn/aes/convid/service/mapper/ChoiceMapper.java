package tn.aes.convid.service.mapper;

import org.mapstruct.*;
import tn.aes.convid.domain.*;
import tn.aes.convid.service.dto.ChoiceDTO;

/**
 * Mapper for the entity {@link Choice} and its DTO {@link ChoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface ChoiceMapper extends EntityMapper<ChoiceDTO, Choice> {
    @Mapping(source = "question.id", target = "questionId")
    ChoiceDTO toDto(Choice choice);

    @Mapping(source = "questionId", target = "question")
    Choice toEntity(ChoiceDTO choiceDTO);

    default Choice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Choice choice = new Choice();
        choice.setId(id);
        return choice;
    }
}
