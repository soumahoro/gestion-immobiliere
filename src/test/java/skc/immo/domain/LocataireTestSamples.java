package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LocataireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Locataire getLocataireSample1() {
        return new Locataire()
            .id(1L)
            .idloc("idloc1")
            .arriere(1)
            .fonction("fonction1")
            .loyer(1)
            .motifdepart("motifdepart1")
            .nationalite("nationalite1")
            .nom("nom1")
            .numpiece("numpiece1")
            .observation("observation1")
            .statut("statut1")
            .telephone("telephone1")
            .typepiece("typepiece1");
    }

    public static Locataire getLocataireSample2() {
        return new Locataire()
            .id(2L)
            .idloc("idloc2")
            .arriere(2)
            .fonction("fonction2")
            .loyer(2)
            .motifdepart("motifdepart2")
            .nationalite("nationalite2")
            .nom("nom2")
            .numpiece("numpiece2")
            .observation("observation2")
            .statut("statut2")
            .telephone("telephone2")
            .typepiece("typepiece2");
    }

    public static Locataire getLocataireRandomSampleGenerator() {
        return new Locataire()
            .id(longCount.incrementAndGet())
            .idloc(UUID.randomUUID().toString())
            .arriere(intCount.incrementAndGet())
            .fonction(UUID.randomUUID().toString())
            .loyer(intCount.incrementAndGet())
            .motifdepart(UUID.randomUUID().toString())
            .nationalite(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .numpiece(UUID.randomUUID().toString())
            .observation(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .typepiece(UUID.randomUUID().toString());
    }
}
