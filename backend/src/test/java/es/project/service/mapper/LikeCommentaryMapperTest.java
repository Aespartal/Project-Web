package es.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeCommentaryMapperTest {

    private LikeCommentaryMapper likeCommentaryMapper;

    @BeforeEach
    public void setUp() {
        likeCommentaryMapper = new LikeCommentaryMapperImpl();
    }
}
