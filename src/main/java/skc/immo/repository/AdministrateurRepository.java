package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Administrateur;

/**
 * Spring Data JPA repository for the Administrateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {}
