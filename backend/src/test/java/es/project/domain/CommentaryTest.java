package es.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commentary.class);
        Commentary commentary1 = new Commentary();
        commentary1.setId(1L);
        Commentary commentary2 = new Commentary();
        commentary2.setId(commentary1.getId());
        assertThat(commentary1).isEqualTo(commentary2);
        commentary2.setId(2L);
        assertThat(commentary1).isNotEqualTo(commentary2);
        commentary1.setId(null);
        assertThat(commentary1).isNotEqualTo(commentary2);
    }
}
