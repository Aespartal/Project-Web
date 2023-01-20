package es.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeImage.class);
        LikeImage likeImage1 = new LikeImage();
        likeImage1.setId(1L);
        LikeImage likeImage2 = new LikeImage();
        likeImage2.setId(likeImage1.getId());
        assertThat(likeImage1).isEqualTo(likeImage2);
        likeImage2.setId(2L);
        assertThat(likeImage1).isNotEqualTo(likeImage2);
        likeImage1.setId(null);
        assertThat(likeImage1).isNotEqualTo(likeImage2);
    }
}
