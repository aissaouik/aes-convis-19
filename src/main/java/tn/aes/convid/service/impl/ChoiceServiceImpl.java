package tn.aes.convid.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.aes.convid.domain.Choice;
import tn.aes.convid.repository.ChoiceRepository;
import tn.aes.convid.service.ChoiceService;
import tn.aes.convid.service.dto.ChoiceDTO;
import tn.aes.convid.service.mapper.ChoiceMapper;

/**
 * Service Implementation for managing {@link Choice}.
 */
@Service
@Transactional
public class ChoiceServiceImpl implements ChoiceService {
    private final Logger log = LoggerFactory.getLogger(ChoiceServiceImpl.class);

    private final ChoiceRepository choiceRepository;

    private final ChoiceMapper choiceMapper;

    public ChoiceServiceImpl(ChoiceRepository choiceRepository, ChoiceMapper choiceMapper) {
        this.choiceRepository = choiceRepository;
        this.choiceMapper = choiceMapper;
    }

    @Override
    public ChoiceDTO save(ChoiceDTO choiceDTO) {
        log.debug("Request to save Choice : {}", choiceDTO);
        Choice choice = choiceMapper.toEntity(choiceDTO);
        choice = choiceRepository.save(choice);
        return choiceMapper.toDto(choice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Choices");
        return choiceRepository.findAll(pageable).map(choiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChoiceDTO> findOne(Long id) {
        log.debug("Request to get Choice : {}", id);
        return choiceRepository.findById(id).map(choiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Choice : {}", id);
        choiceRepository.deleteById(id);
    }
}
