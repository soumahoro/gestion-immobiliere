package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.AnneeTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class AnneeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annee.class);
        Annee annee1 = getAnneeSample1();
        Annee annee2 = new Annee();
        assertThat(annee1).isNotEqualTo(annee2);

        annee2.setId(annee1.getId());
        assertThat(annee1).isEqualTo(annee2);

        annee2 = getAnneeSample2();
        assertThat(annee1).isNotEqualTo(annee2);
    }
}
