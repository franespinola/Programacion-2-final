package fran.um.edu.ar.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fran.um.edu.ar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdenesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdenesDTO.class);
        OrdenesDTO ordenesDTO1 = new OrdenesDTO();
        ordenesDTO1.setId(1L);
        OrdenesDTO ordenesDTO2 = new OrdenesDTO();
        assertThat(ordenesDTO1).isNotEqualTo(ordenesDTO2);
        ordenesDTO2.setId(ordenesDTO1.getId());
        assertThat(ordenesDTO1).isEqualTo(ordenesDTO2);
        ordenesDTO2.setId(2L);
        assertThat(ordenesDTO1).isNotEqualTo(ordenesDTO2);
        ordenesDTO1.setId(null);
        assertThat(ordenesDTO1).isNotEqualTo(ordenesDTO2);
    }
}
