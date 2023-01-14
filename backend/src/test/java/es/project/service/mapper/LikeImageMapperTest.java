package es.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeImageMapperTest {

    private LikeImageMapper likeImageMapper;

    @BeforeEach
    public void setUp() {
        likeImageMapper = new LikeImageMapperImpl();
    }
}
