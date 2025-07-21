package skc.immo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static skc.immo.domain.MoiTestSamples.*;

import org.junit.jupiter.api.Test;
import skc.immo.web.rest.TestUtil;

class MoiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Moi.class);
        Moi moi1 = getMoiSample1();
        Moi moi2 = new Moi();
        assertThat(moi1).isNotEqualTo(moi2);

        moi2.setId(moi1.getId());
        assertThat(moi1).isEqualTo(moi2);

        moi2 = getMoiSample2();
        assertThat(moi1).isNotEqualTo(moi2);
    }
}
