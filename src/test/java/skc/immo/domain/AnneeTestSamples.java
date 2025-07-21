package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AnneeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Annee getAnneeSample1() {
        return new Annee().id(1L).an(1).libelle("libelle1");
    }

    public static Annee getAnneeSample2() {
        return new Annee().id(2L).an(2).libelle("libelle2");
    }

    public static Annee getAnneeRandomSampleGenerator() {
        return new Annee().id(longCount.incrementAndGet()).an(intCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
