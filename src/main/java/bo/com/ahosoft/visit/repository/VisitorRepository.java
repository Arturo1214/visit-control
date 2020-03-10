package bo.com.ahosoft.visit.repository;

import bo.com.ahosoft.visit.domain.Visitor;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Visitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long>, JpaSpecificationExecutor<Visitor> {

    Optional<Visitor> findFirstByDocumentNumber(String documentNumber);

    Optional<Visitor> findFirstByDocumentNumberAndDocumentTypeId(String documentNumber, Long documentTypeId);
}
