package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Appartement;

/**
 * Spring Data JPA repository for the Appartement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long> {}
