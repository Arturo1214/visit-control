package bo.com.ahosoft.visit.web.rest;

import bo.com.ahosoft.visit.domain.IncomeControl;
import bo.com.ahosoft.visit.service.IncomeControlService;
import bo.com.ahosoft.visit.service.dto.IncomeControlDTO;
import bo.com.ahosoft.visit.service.errors.IncomeControlDepartureDateExist;
import bo.com.ahosoft.visit.service.errors.IncomeControlNotExist;
import bo.com.ahosoft.visit.service.util.FileUtilService;
import bo.com.ahosoft.visit.web.rest.errors.BadRequestAlertException;
import bo.com.ahosoft.visit.service.dto.IncomeControlCriteria;
import bo.com.ahosoft.visit.service.IncomeControlQueryService;

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
 * REST controller for managing {@link bo.com.ahosoft.visit.domain.IncomeControl}.
 */
@RestController
@RequestMapping("/api")
public class IncomeControlResource {

    private final Logger log = LoggerFactory.getLogger(IncomeControlResource.class);

    private static final String ENTITY_NAME = "incomeControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomeControlService incomeControlService;

    private final IncomeControlQueryService incomeControlQueryService;

    private final FileUtilService fileUtilService;

    public IncomeControlResource(IncomeControlService incomeControlService, IncomeControlQueryService incomeControlQueryService, FileUtilService fileUtilService) {
        this.incomeControlService = incomeControlService;
        this.incomeControlQueryService = incomeControlQueryService;
        this.fileUtilService = fileUtilService;
    }

    /**
     * {@code POST  /income-controls} : Create a new incomeControl.
     *
     * @param incomeControl the incomeControl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomeControl, or with status {@code 400 (Bad Request)} if the incomeControl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/income-controls")
    public ResponseEntity<IncomeControl> createIncomeControl(@Valid @RequestBody IncomeControlDTO incomeControl) throws URISyntaxException {
        log.debug("REST request to save IncomeControl : {}", incomeControl);
        if (incomeControl.getId() != null) {
            throw new BadRequestAlertException("A new incomeControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IncomeControl result = null;
        try {
            result = incomeControlService.save(incomeControl.toIncomeControl());
        } catch (IOException e) {
            throw new BadRequestException("ERROR_IMAGE", e.getMessage());
        } catch (IncomeControlNotExist incomeControlNotExist) {
            throw new BadRequestException(incomeControlNotExist.getCode(), incomeControlNotExist.getMessage());
        }
        return ResponseEntity.created(new URI("/api/income-controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /income-controls} : Updates an existing incomeControl.
     *
     * @param incomeControl the incomeControl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeControl,
     * or with status {@code 400 (Bad Request)} if the incomeControl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomeControl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/income-controls")
    public ResponseEntity<IncomeControl> updateIncomeControl(@Valid @RequestBody IncomeControlDTO incomeControl) throws URISyntaxException {
        log.debug("REST request to update IncomeControl : {}", incomeControl);
        if (incomeControl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IncomeControl result = null;
        try {
            result = incomeControlService.save(incomeControl.toIncomeControl());
        } catch (IOException e) {
            throw new BadRequestException("ERROR_IMAGE", e.getMessage());
        } catch (IncomeControlNotExist incomeControlNotExist) {
            throw new BadRequestException(incomeControlNotExist.getCode(), incomeControlNotExist.getMessage());
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomeControl.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /income-controls} : get all the incomeControls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomeControls in body.
     */
    @GetMapping("/income-controls")
    public ResponseEntity<List<IncomeControl>> getAllIncomeControls(IncomeControlCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IncomeControls by criteria: {}", criteria);
        Page<IncomeControl> page = incomeControlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /income-controls/count} : count all the incomeControls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/income-controls/count")
    public ResponseEntity<Long> countIncomeControls(IncomeControlCriteria criteria) {
        log.debug("REST request to count IncomeControls by criteria: {}", criteria);
        return ResponseEntity.ok().body(incomeControlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /income-controls/:id} : get the "id" incomeControl.
     *
     * @param id the id of the incomeControl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomeControl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/income-controls/{id}")
    public ResponseEntity<IncomeControl> getIncomeControl(@PathVariable Long id) {
        log.debug("REST request to get IncomeControl : {}", id);
        Optional<IncomeControl> incomeControl = incomeControlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomeControl.map(incomeControl1 -> {
            if (incomeControl1.getVisitor() != null && incomeControl1.getVisitor().getImageName() != null
                && !incomeControl1.getVisitor().getImageName().isEmpty()) {
                try {
                    incomeControl1.getVisitor().setImageName(fileUtilService.readArchive(
                        incomeControl1.getVisitor().getImageName()
                    ));
                } catch (IOException e) {
                    throw new BadRequestException("ERROR_IMAGE", "error image");
                }
            }
            return incomeControl1;
        }));
    }

    /**
     * {@code DELETE  /income-controls/:id} : delete the "id" incomeControl.
     *
     * @param id the id of the incomeControl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/income-controls/{id}")
    public ResponseEntity<Void> deleteIncomeControl(@PathVariable Long id) {
        log.debug("REST request to delete IncomeControl : {}", id);
        incomeControlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/income-controls/exit/{id}")
    public ResponseEntity<Void> exitIncomeControl(@PathVariable Long id) {
        log.debug("REST request to delete IncomeControl : {}", id);
        try {
            incomeControlService.exit(id);
        } catch (IncomeControlNotExist incomeControlNotExist) {
            throw new BadRequestException(incomeControlNotExist.getCode(), incomeControlNotExist.getMessage());
        } catch (IncomeControlDepartureDateExist incomeControlDepartureDateExist) {
            throw new BadRequestException(incomeControlDepartureDateExist.getCode(), incomeControlDepartureDateExist.getMessage());
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
