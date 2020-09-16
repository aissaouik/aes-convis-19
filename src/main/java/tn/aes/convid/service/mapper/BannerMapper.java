package tn.aes.convid.service.mapper;

import org.mapstruct.*;
import tn.aes.convid.domain.*;
import tn.aes.convid.service.dto.BannerDTO;

/**
 * Mapper for the entity {@link Banner} and its DTO {@link BannerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BannerMapper extends EntityMapper<BannerDTO, Banner> {
    default Banner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Banner banner = new Banner();
        banner.setId(id);
        return banner;
    }
}
