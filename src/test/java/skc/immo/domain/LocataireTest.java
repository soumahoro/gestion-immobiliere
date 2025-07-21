package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.AppartementTestSamples.*;
import static skc.immo.domain.LocataireTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class LocataireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locataire.class);
        Locataire locataire1 = getLocataireSample1();
        Locataire locataire2 = new Locataire();
        assertThat(locataire1).isNotEqualTo(locataire2);

        locataire2.setId(locataire1.getId());
        assertThat(locataire1).isEqualTo(locataire2);

        locataire2 = getLocataireSample2();
        assertThat(locataire1).isNotEqualTo(locataire2);
    }

    @Test
    void appartementTest() {
        Locataire locataire = getLocataireRandomSampleGenerator();
        Appartement appartementBack = getAppartementRandomSampleGenerator();

        locataire.setAppartement(appartementBack);
        assertThat(locataire.getAppartement()).isEqualTo(appartementBack);

        locataire.appartement(null);
        assertThat(locataire.getAppartement()).isNull();
    }
}
