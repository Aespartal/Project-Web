package es.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeCommentaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeCommentary.class);
        LikeCommentary likeCommentary1 = new LikeCommentary();
        likeCommentary1.setId(1L);
        LikeCommentary likeCommentary2 = new LikeCommentary();
        likeCommentary2.setId(likeCommentary1.getId());
        assertThat(likeCommentary1).isEqualTo(likeCommentary2);
        likeCommentary2.setId(2L);
        assertThat(likeCommentary1).isNotEqualTo(likeCommentary2);
        likeCommentary1.setId(null);
        assertThat(likeCommentary1).isNotEqualTo(likeCommentary2);
    }
}
