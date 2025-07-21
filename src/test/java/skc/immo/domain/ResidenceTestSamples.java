package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResidenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Residence getResidenceSample1() {
        return new Residence()
            .id(1L)
            .idres("idres1")
            .ilot("ilot1")
            .localisation("localisation1")
            .observation("observation1")
            .quartier("quartier1")
            .ville("ville1");
    }

    public static Residence getResidenceSample2() {
        return new Residence()
            .id(2L)
            .idres("idres2")
            .ilot("ilot2")
            .localisation("localisation2")
            .observation("observation2")
            .quartier("quartier2")
            .ville("ville2");
    }

    public static Residence getResidenceRandomSampleGenerator() {
        return new Residence()
            .id(longCount.incrementAndGet())
            .idres(UUID.randomUUID().toString())
            .ilot(UUID.randomUUID().toString())
            .localisation(UUID.randomUUID().toString())
            .observation(UUID.randomUUID().toString())
            .quartier(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString());
    }
}
