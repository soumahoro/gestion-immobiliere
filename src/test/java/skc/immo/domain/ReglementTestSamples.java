package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReglementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Reglement getReglementSample1() {
        return new Reglement()
            .id(1L)
            .idreg(1)
            .annee(1)
            .montant(1)
            .motif("motif1")
            .observ1("observ11")
            .observ2("observ21")
            .observ3("observ31")
            .reste(1);
    }

    public static Reglement getReglementSample2() {
        return new Reglement()
            .id(2L)
            .idreg(2)
            .annee(2)
            .montant(2)
            .motif("motif2")
            .observ1("observ12")
            .observ2("observ22")
            .observ3("observ32")
            .reste(2);
    }

    public static Reglement getReglementRandomSampleGenerator() {
        return new Reglement()
            .id(longCount.incrementAndGet())
            .idreg(intCount.incrementAndGet())
            .annee(intCount.incrementAndGet())
            .montant(intCount.incrementAndGet())
            .motif(UUID.randomUUID().toString())
            .observ1(UUID.randomUUID().toString())
            .observ2(UUID.randomUUID().toString())
            .observ3(UUID.randomUUID().toString())
            .reste(intCount.incrementAndGet());
    }
}
