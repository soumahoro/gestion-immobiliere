package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Locataire;

/**
 * Spring Data JPA repository for the Locataire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocataireRepository extends JpaRepository<Locataire, Long> {}
