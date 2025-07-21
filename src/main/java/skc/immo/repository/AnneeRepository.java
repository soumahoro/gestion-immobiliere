package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Annee;

/**
 * Spring Data JPA repository for the Annee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeRepository extends JpaRepository<Annee, Long> {}
