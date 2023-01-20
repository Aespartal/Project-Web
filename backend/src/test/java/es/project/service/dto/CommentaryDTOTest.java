package es.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentaryDTO.class);
        CommentaryDTO commentaryDTO1 = new CommentaryDTO();
        commentaryDTO1.setId(1L);
        CommentaryDTO commentaryDTO2 = new CommentaryDTO();
        assertThat(commentaryDTO1).isNotEqualTo(commentaryDTO2);
        commentaryDTO2.setId(commentaryDTO1.getId());
        assertThat(commentaryDTO1).isEqualTo(commentaryDTO2);
        commentaryDTO2.setId(2L);
        assertThat(commentaryDTO1).isNotEqualTo(commentaryDTO2);
        commentaryDTO1.setId(null);
        assertThat(commentaryDTO1).isNotEqualTo(commentaryDTO2);
    }
}
