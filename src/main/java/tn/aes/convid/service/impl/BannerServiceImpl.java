package tn.aes.convid.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.aes.convid.domain.Banner;
import tn.aes.convid.repository.BannerRepository;
import tn.aes.convid.service.BannerService;
import tn.aes.convid.service.dto.BannerDTO;
import tn.aes.convid.service.mapper.BannerMapper;

/**
 * Service Implementation for managing {@link Banner}.
 */
@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    private final Logger log = LoggerFactory.getLogger(BannerServiceImpl.class);

    private final BannerRepository bannerRepository;

    private final BannerMapper bannerMapper;

    public BannerServiceImpl(BannerRepository bannerRepository, BannerMapper bannerMapper) {
        this.bannerRepository = bannerRepository;
        this.bannerMapper = bannerMapper;
    }

    @Override
    public BannerDTO save(BannerDTO bannerDTO) {
        log.debug("Request to save Banner : {}", bannerDTO);
        Banner banner = bannerMapper.toEntity(bannerDTO);
        banner = bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BannerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banners");
        return bannerRepository.findAll(pageable).map(bannerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BannerDTO> findOne(Long id) {
        log.debug("Request to get Banner : {}", id);
        return bannerRepository.findById(id).map(bannerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Banner : {}", id);
        bannerRepository.deleteById(id);
    }
}
