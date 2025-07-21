package skc.immo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utilisateur getUtilisateurSample1() {
        return new Utilisateur()
            .id(1L)
            .iduser("iduser1")
            .login("login1")
            .nom("nom1")
            .prenom("prenom1")
            .motdepasse("motdepasse1")
            .email("email1")
            .photo("photo1")
            .pwd("pwd1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur()
            .id(2L)
            .iduser("iduser2")
            .login("login2")
            .nom("nom2")
            .prenom("prenom2")
            .motdepasse("motdepasse2")
            .email("email2")
            .photo("photo2")
            .pwd("pwd2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur()
            .id(longCount.incrementAndGet())
            .iduser(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .motdepasse(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .photo(UUID.randomUUID().toString())
            .pwd(UUID.randomUUID().toString());
    }
}
