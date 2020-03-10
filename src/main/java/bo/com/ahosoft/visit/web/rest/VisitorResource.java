package bo.com.ahosoft.visit.web.rest;

import bo.com.ahosoft.visit.domain.Visitor;
import bo.com.ahosoft.visit.repository.VisitorRepository;
import bo.com.ahosoft.visit.service.VisitorService;
import bo.com.ahosoft.visit.service.util.FileUtilService;
import bo.com.ahosoft.visit.web.rest.errors.BadRequestAlertException;
import bo.com.ahosoft.visit.service.dto.VisitorCriteria;
import bo.com.ahosoft.visit.service.VisitorQueryService;

import bo.com.ahosoft.visit.web.rest.errors.my.BadRequestException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bo.com.ahosoft.visit.domain.Visitor}.
 */
@RestController
@RequestMapping("/api")
public class VisitorResource {

    private final Logger log = LoggerFactory.getLogger(VisitorResource.class);

    private static final String ENTITY_NAME = "visitor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitorService visitorService;

    private final VisitorQueryService visitorQueryService;

    private final FileUtilService fileUtilService;

    private final VisitorRepository visitorRepository;

    public VisitorResource(VisitorService visitorService, VisitorQueryService visitorQueryService, FileUtilService fileUtilService, VisitorRepository visitorRepository) {
        this.visitorService = visitorService;
        this.visitorQueryService = visitorQueryService;
        this.fileUtilService = fileUtilService;
        this.visitorRepository = visitorRepository;
    }

    /**
     * {@code POST  /visitors} : Create a new visitor.
     *
     * @param visitor the visitor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visitor, or with status {@code 400 (Bad Request)} if the visitor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visitors")
    public ResponseEntity<Visitor> createVisitor(@Valid @RequestBody Visitor visitor) throws URISyntaxException {
        log.debug("REST request to save Visitor : {}", visitor);
        if (visitor.getId() != null) {
            throw new BadRequestAlertException("A new visitor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Visitor result = null;
        try {
            result = visitorService.save(visitor);
        } catch (IOException e) {
            throw new BadRequestException("ERROR_IMAGE", e.getMessage());
        }
        return ResponseEntity.created(new URI("/api/visitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visitors} : Updates an existing visitor.
     *
     * @param visitor the visitor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitor,
     * or with status {@code 400 (Bad Request)} if the visitor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visitors")
    public ResponseEntity<Visitor> updateVisitor(@Valid @RequestBody Visitor visitor) throws URISyntaxException {
        log.debug("REST request to update Visitor : {}", visitor);
        if (visitor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Visitor result = null;
        try {
            result = visitorService.save(visitor);
        } catch (IOException e) {
            throw new BadRequestException("ERROR_IMAGE", e.getMessage());
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitor.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /visitors} : get all the visitors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visitors in body.
     */
    @GetMapping("/visitors")
    public ResponseEntity<List<Visitor>> getAllVisitors(VisitorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Visitors by criteria: {}", criteria);
        Page<Visitor> page = visitorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visitors/count} : count all the visitors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/visitors/count")
    public ResponseEntity<Long> countVisitors(VisitorCriteria criteria) {
        log.debug("REST request to count Visitors by criteria: {}", criteria);
        return ResponseEntity.ok().body(visitorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /visitors/:id} : get the "id" visitor.
     *
     * @param id the id of the visitor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visitor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visitors/{id}")
    public ResponseEntity<Visitor> getVisitor(@PathVariable Long id) {
        log.debug("REST request to get Visitor : {}", id);
        Optional<Visitor> visitor = visitorService.findOne(id)
            .map(visitor1 -> {
                try {
                    if (visitor1.getImageName() != null && !visitor1.getImageName().isEmpty())
                        visitor1.setImageName(fileUtilService.readArchive(visitor1.getImageName()));
                } catch (IOException e) {
                    throw new BadRequestException("ERROR_IMAGE", "error image");
                }
                return visitor1;
            });
        return ResponseUtil.wrapOrNotFound(visitor);
    }

    @GetMapping("/visitors/documentNumber/{documentNumber}")
    public ResponseEntity<Visitor> getVisitorDocumentNumber(@PathVariable String documentNumber) {
        log.debug("REST request to get Visitor : {}", documentNumber);
        Optional<Visitor> visitor = visitorRepository.findFirstByDocumentNumber(documentNumber)
            .map(visitor1 -> {
                try {
                    if (visitor1.getImageName() != null && !visitor1.getImageName().isEmpty())
                        visitor1.setImageName(fileUtilService.readArchive(visitor1.getImageName()));
                } catch (IOException e) {
                    throw new BadRequestException("ERROR_IMAGE", "error image");
                }
                return visitor1;
            });
        return ResponseUtil.wrapOrNotFound(visitor);
    }

    @GetMapping("/visitors/documentNumber-type/{documentNumber}/{documentTypeId}")
    public ResponseEntity<Visitor> getVisitorDocumentNumberAndDocumentType(@PathVariable String documentNumber, @PathVariable Long documentTypeId) {
        log.debug("REST request to get Visitor : {}", documentNumber);
        Optional<Visitor> visitor = visitorRepository.findFirstByDocumentNumberAndDocumentTypeId(documentNumber, documentTypeId)
            .map(visitor1 -> {
                try {
                    if (visitor1.getImageName() != null && !visitor1.getImageName().isEmpty())
                        visitor1.setImageName(fileUtilService.readArchive(visitor1.getImageName()));
                } catch (IOException e) {
                    throw new BadRequestException("ERROR_IMAGE", "error image");
                }
                return visitor1;
            });

        return visitor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    /**
     * {@code DELETE  /visitors/:id} : delete the "id" visitor.
     *
     * @param id the id of the visitor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visitors/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        log.debug("REST request to delete Visitor : {}", id);
        visitorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
