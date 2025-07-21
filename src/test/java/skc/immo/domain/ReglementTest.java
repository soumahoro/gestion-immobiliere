package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.LocataireTestSamples.*;
import static skc.immo.domain.MoiTestSamples.*;
import static skc.immo.domain.ReglementTestSamples.*;
import static skc.immo.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class ReglementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reglement.class);
        Reglement reglement1 = getReglementSample1();
        Reglement reglement2 = new Reglement();
        assertThat(reglement1).isNotEqualTo(reglement2);

        reglement2.setId(reglement1.getId());
        assertThat(reglement1).isEqualTo(reglement2);

        reglement2 = getReglementSample2();
        assertThat(reglement1).isNotEqualTo(reglement2);
    }

    @Test
    void locataireTest() {
        Reglement reglement = getReglementRandomSampleGenerator();
        Locataire locataireBack = getLocataireRandomSampleGenerator();

        reglement.setLocataire(locataireBack);
        assertThat(reglement.getLocataire()).isEqualTo(locataireBack);

        reglement.locataire(null);
        assertThat(reglement.getLocataire()).isNull();
    }

    @Test
    void moiTest() {
        Reglement reglement = getReglementRandomSampleGenerator();
        Moi moiBack = getMoiRandomSampleGenerator();

        reglement.setMoi(moiBack);
        assertThat(reglement.getMoi()).isEqualTo(moiBack);

        reglement.moi(null);
        assertThat(reglement.getMoi()).isNull();
    }

    @Test
    void utilisateurTest() {
        Reglement reglement = getReglementRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        reglement.setUtilisateur(utilisateurBack);
        assertThat(reglement.getUtilisateur()).isEqualTo(utilisateurBack);

        reglement.utilisateur(null);
        assertThat(reglement.getUtilisateur()).isNull();
    }
}
