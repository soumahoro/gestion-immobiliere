package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MoiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Moi getMoiSample1() {
        return new Moi().id(1L).idmois(1).mois("mois1");
    }

    public static Moi getMoiSample2() {
        return new Moi().id(2L).idmois(2).mois("mois2");
    }

    public static Moi getMoiRandomSampleGenerator() {
        return new Moi().id(longCount.incrementAndGet()).idmois(intCount.incrementAndGet()).mois(UUID.randomUUID().toString());
    }
}
