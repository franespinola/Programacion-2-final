package fran.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fran.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdenesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordenes.class);
        Ordenes ordenes1 = new Ordenes();
        ordenes1.setId(1L);
        Ordenes ordenes2 = new Ordenes();
        ordenes2.setId(ordenes1.getId());
        assertThat(ordenes1).isEqualTo(ordenes2);
        ordenes2.setId(2L);
        assertThat(ordenes1).isNotEqualTo(ordenes2);
        ordenes1.setId(null);
        assertThat(ordenes1).isNotEqualTo(ordenes2);
    }
}
