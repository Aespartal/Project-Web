package es.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import es.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtendedUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtendedUserDTO.class);
        ExtendedUserDTO extendedUserDTO1 = new ExtendedUserDTO();
        extendedUserDTO1.setId(1L);
        ExtendedUserDTO extendedUserDTO2 = new ExtendedUserDTO();
        assertThat(extendedUserDTO1).isNotEqualTo(extendedUserDTO2);
        extendedUserDTO2.setId(extendedUserDTO1.getId());
        assertThat(extendedUserDTO1).isEqualTo(extendedUserDTO2);
        extendedUserDTO2.setId(2L);
        assertThat(extendedUserDTO1).isNotEqualTo(extendedUserDTO2);
        extendedUserDTO1.setId(null);
        assertThat(extendedUserDTO1).isNotEqualTo(extendedUserDTO2);
    }
}
