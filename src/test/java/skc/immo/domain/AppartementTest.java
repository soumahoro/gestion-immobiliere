package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.AppartementTestSamples.*;
import static skc.immo.domain.ResidenceTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class AppartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appartement.class);
        Appartement appartement1 = getAppartementSample1();
        Appartement appartement2 = new Appartement();
        assertThat(appartement1).isNotEqualTo(appartement2);

        appartement2.setId(appartement1.getId());
        assertThat(appartement1).isEqualTo(appartement2);

        appartement2 = getAppartementSample2();
        assertThat(appartement1).isNotEqualTo(appartement2);
    }

    @Test
    void residenceTest() {
        Appartement appartement = getAppartementRandomSampleGenerator();
        Residence residenceBack = getResidenceRandomSampleGenerator();

        appartement.setResidence(residenceBack);
        assertThat(appartement.getResidence()).isEqualTo(residenceBack);

        appartement.residence(null);
        assertThat(appartement.getResidence()).isNull();
    }
}
