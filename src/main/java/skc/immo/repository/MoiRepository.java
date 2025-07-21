package skc.immo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import skc.immo.domain.Moi;

/**
 * Spring Data JPA repository for the Moi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoiRepository extends JpaRepository<Moi, Long> {}
