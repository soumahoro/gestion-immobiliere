package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Proprietaire;

/**
 * Spring Data JPA repository for the Proprietaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {}
