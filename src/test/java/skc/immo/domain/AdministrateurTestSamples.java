package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdministrateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Administrateur getAdministrateurSample1() {
        return new Administrateur().id(1L).code("code1");
    }

    public static Administrateur getAdministrateurSample2() {
        return new Administrateur().id(2L).code("code2");
    }

    public static Administrateur getAdministrateurRandomSampleGenerator() {
        return new Administrateur().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
