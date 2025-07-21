package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppartementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Appartement getAppartementSample1() {
        return new Appartement().id(1L).idapp("idapp1").libelle("libelle1").loyer(1).nbrepieces(1).taux(1);
    }

    public static Appartement getAppartementSample2() {
        return new Appartement().id(2L).idapp("idapp2").libelle("libelle2").loyer(2).nbrepieces(2).taux(2);
    }

    public static Appartement getAppartementRandomSampleGenerator() {
        return new Appartement()
            .id(longCount.incrementAndGet())
            .idapp(UUID.randomUUID().toString())
            .libelle(UUID.randomUUID().toString())
            .loyer(intCount.incrementAndGet())
            .nbrepieces(intCount.incrementAndGet())
            .taux(intCount.incrementAndGet());
    }
}
