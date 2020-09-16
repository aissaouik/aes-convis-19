package tn.aes.convid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BannerMapperTest {
    private BannerMapper bannerMapper;

    @BeforeEach
    public void setUp() {
        bannerMapper = new BannerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bannerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bannerMapper.fromId(null)).isNull();
    }
}
