package es.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentaryMapperTest {

    private CommentaryMapper commentaryMapper;

    @BeforeEach
    public void setUp() {
        commentaryMapper = new CommentaryMapperImpl();
    }
}
