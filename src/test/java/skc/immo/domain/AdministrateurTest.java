package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.AdministrateurTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class AdministrateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Administrateur.class);
        Administrateur administrateur1 = getAdministrateurSample1();
        Administrateur administrateur2 = new Administrateur();
        assertThat(administrateur1).isNotEqualTo(administrateur2);

        administrateur2.setId(administrateur1.getId());
        assertThat(administrateur1).isEqualTo(administrateur2);

        administrateur2 = getAdministrateurSample2();
        assertThat(administrateur1).isNotEqualTo(administrateur2);
    }
}
