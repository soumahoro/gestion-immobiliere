package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Utilisateur;

/**
 * Spring Data JPA repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {}
