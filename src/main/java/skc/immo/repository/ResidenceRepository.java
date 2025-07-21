package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Residence;

/**
 * Spring Data JPA repository for the Residence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Long> {}
