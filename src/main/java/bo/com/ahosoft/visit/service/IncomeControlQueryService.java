package bo.com.ahosoft.visit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import bo.com.ahosoft.visit.domain.IncomeControl;
import bo.com.ahosoft.visit.domain.*; // for static metamodels
import bo.com.ahosoft.visit.repository.IncomeControlRepository;
import bo.com.ahosoft.visit.service.dto.IncomeControlCriteria;

/**
 * Service for executing complex queries for {@link IncomeControl} entities in the database.
 * The main input is a {@link IncomeControlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IncomeControl} or a {@link Page} of {@link IncomeControl} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IncomeControlQueryService extends QueryService<IncomeControl> {

    private final Logger log = LoggerFactory.getLogger(IncomeControlQueryService.class);

    private final IncomeControlRepository incomeControlRepository;

    public IncomeControlQueryService(IncomeControlRepository incomeControlRepository) {
        this.incomeControlRepository = incomeControlRepository;
    }

    /**
     * Return a {@link List} of {@link IncomeControl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IncomeControl> findByCriteria(IncomeControlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IncomeControl> specification = createSpecification(criteria);
        return incomeControlRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link IncomeControl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IncomeControl> findByCriteria(IncomeControlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IncomeControl> specification = createSpecification(criteria);
        return incomeControlRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IncomeControlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IncomeControl> specification = createSpecification(criteria);
        return incomeControlRepository.count(specification);
    }

    /**
     * Function to convert {@link IncomeControlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IncomeControl> createSpecification(IncomeControlCriteria criteria) {
        Specification<IncomeControl> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IncomeControl_.id));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), IncomeControl_.reason));
            }
            if (criteria.getPlace() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlace(), IncomeControl_.place));
            }
            if (criteria.getAnswerable() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswerable(), IncomeControl_.answerable));
            }
            if (criteria.getAdmissionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAdmissionDate(), IncomeControl_.admissionDate));
            }
            if (criteria.getDepartureDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepartureDate(), IncomeControl_.departureDate));
            }
            if (criteria.getVisitorId() != null) {
                specification = specification.and(buildSpecification(criteria.getVisitorId(),
                    root -> root.join(IncomeControl_.visitor, JoinType.LEFT).get(Visitor_.id)));
            }
        }
        return specification;
    }
}
