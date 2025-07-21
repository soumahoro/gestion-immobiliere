package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProprietaireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Proprietaire getProprietaireSample1() {
        return new Proprietaire().id(1L).idpro("idpro1").nom("nom1").residence("residence1").tel("tel1");
    }

    public static Proprietaire getProprietaireSample2() {
        return new Proprietaire().id(2L).idpro("idpro2").nom("nom2").residence("residence2").tel("tel2");
    }

    public static Proprietaire getProprietaireRandomSampleGenerator() {
        return new Proprietaire()
            .id(longCount.incrementAndGet())
            .idpro(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .residence(UUID.randomUUID().toString())
            .tel(UUID.randomUUID().toString());
    }
}
