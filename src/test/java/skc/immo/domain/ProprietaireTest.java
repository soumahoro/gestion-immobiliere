package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.ProprietaireTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class ProprietaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proprietaire.class);
        Proprietaire proprietaire1 = getProprietaireSample1();
        Proprietaire proprietaire2 = new Proprietaire();
        assertThat(proprietaire1).isNotEqualTo(proprietaire2);

        proprietaire2.setId(proprietaire1.getId());
        assertThat(proprietaire1).isEqualTo(proprietaire2);

        proprietaire2 = getProprietaireSample2();
        assertThat(proprietaire1).isNotEqualTo(proprietaire2);
    }
}
