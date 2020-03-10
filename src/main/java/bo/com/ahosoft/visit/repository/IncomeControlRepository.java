package bo.com.ahosoft.visit.repository;

import bo.com.ahosoft.visit.domain.IncomeControl;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the IncomeControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomeControlRepository extends JpaRepository<IncomeControl, Long>, JpaSpecificationExecutor<IncomeControl> {

}
