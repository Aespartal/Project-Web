package es.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeCommentaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeCommentaryDTO.class);
        LikeCommentaryDTO likeCommentaryDTO1 = new LikeCommentaryDTO();
        likeCommentaryDTO1.setId(1L);
        LikeCommentaryDTO likeCommentaryDTO2 = new LikeCommentaryDTO();
        assertThat(likeCommentaryDTO1).isNotEqualTo(likeCommentaryDTO2);
        likeCommentaryDTO2.setId(likeCommentaryDTO1.getId());
        assertThat(likeCommentaryDTO1).isEqualTo(likeCommentaryDTO2);
        likeCommentaryDTO2.setId(2L);
        assertThat(likeCommentaryDTO1).isNotEqualTo(likeCommentaryDTO2);
        likeCommentaryDTO1.setId(null);
        assertThat(likeCommentaryDTO1).isNotEqualTo(likeCommentaryDTO2);
    }
}
