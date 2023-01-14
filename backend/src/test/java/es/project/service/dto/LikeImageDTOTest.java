package es.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeImageDTO.class);
        LikeImageDTO likeImageDTO1 = new LikeImageDTO();
        likeImageDTO1.setId(1L);
        LikeImageDTO likeImageDTO2 = new LikeImageDTO();
        assertThat(likeImageDTO1).isNotEqualTo(likeImageDTO2);
        likeImageDTO2.setId(likeImageDTO1.getId());
        assertThat(likeImageDTO1).isEqualTo(likeImageDTO2);
        likeImageDTO2.setId(2L);
        assertThat(likeImageDTO1).isNotEqualTo(likeImageDTO2);
        likeImageDTO1.setId(null);
        assertThat(likeImageDTO1).isNotEqualTo(likeImageDTO2);
    }
}
