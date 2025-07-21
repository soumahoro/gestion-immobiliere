package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.ProprietaireTestSamples.*;
import static skc.immo.domain.ResidenceTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class ResidenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Residence.class);
        Residence residence1 = getResidenceSample1();
        Residence residence2 = new Residence();
        assertThat(residence1).isNotEqualTo(residence2);

        residence2.setId(residence1.getId());
        assertThat(residence1).isEqualTo(residence2);

        residence2 = getResidenceSample2();
        assertThat(residence1).isNotEqualTo(residence2);
    }

    @Test
    void proprietaireTest() {
        Residence residence = getResidenceRandomSampleGenerator();
        Proprietaire proprietaireBack = getProprietaireRandomSampleGenerator();

        residence.setProprietaire(proprietaireBack);
        assertThat(residence.getProprietaire()).isEqualTo(proprietaireBack);

        residence.proprietaire(null);
        assertThat(residence.getProprietaire()).isNull();
    }
}
