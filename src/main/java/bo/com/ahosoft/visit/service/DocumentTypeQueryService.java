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

import bo.com.ahosoft.visit.domain.DocumentType;
import bo.com.ahosoft.visit.domain.*; // for static metamodels
import bo.com.ahosoft.visit.repository.DocumentTypeRepository;
import bo.com.ahosoft.visit.service.dto.DocumentTypeCriteria;

/**
 * Service for executing complex queries for {@link DocumentType} entities in the database.
 * The main input is a {@link DocumentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentType} or a {@link Page} of {@link DocumentType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentTypeQueryService extends QueryService<DocumentType> {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeQueryService.class);

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeQueryService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * Return a {@link List} of {@link DocumentType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentType> findByCriteria(DocumentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocumentType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentType> findByCriteria(DocumentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentType> createSpecification(DocumentTypeCriteria criteria) {
        Specification<DocumentType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocumentType_.name));
            }
        }
        return specification;
    }
}
