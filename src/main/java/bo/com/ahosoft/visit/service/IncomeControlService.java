package bo.com.ahosoft.visit.service;

import bo.com.ahosoft.visit.domain.IncomeControl;
import bo.com.ahosoft.visit.domain.Visitor;
import bo.com.ahosoft.visit.repository.IncomeControlRepository;
import bo.com.ahosoft.visit.repository.VisitorRepository;
import bo.com.ahosoft.visit.service.errors.IncomeControlDepartureDateExist;
import bo.com.ahosoft.visit.service.errors.IncomeControlNotExist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link IncomeControl}.
 */
@Service
@Transactional
public class IncomeControlService {

    private final Logger log = LoggerFactory.getLogger(IncomeControlService.class);

    private final IncomeControlRepository incomeControlRepository;

    private final VisitorService visitorService;

    public IncomeControlService(IncomeControlRepository incomeControlRepository, VisitorService visitorService) {
        this.incomeControlRepository = incomeControlRepository;
        this.visitorService = visitorService;
    }

    /**
     * Save a incomeControl.
     *
     * @param incomeControl the entity to save.
     * @return the persisted entity.
     */
    public IncomeControl save(IncomeControl incomeControl) throws IOException, IncomeControlNotExist {
        log.debug("Request to save IncomeControl : {}", incomeControl);

        Visitor visitor = incomeControl.getVisitor();
        incomeControl.setVisitor(visitorService.save(visitor));
        if (incomeControl.getId() == null) {
            incomeControl.setAdmissionDate(ZonedDateTime.now());
            return incomeControlRepository.save(incomeControl);
        } else {
            Optional<IncomeControl> optionalIncomeControl = incomeControlRepository.findById(incomeControl.getId());
            if (!optionalIncomeControl.isPresent())
                throw new IncomeControlNotExist();
            IncomeControl updateIncomeControl = optionalIncomeControl.get();
            updateIncomeControl.setVisitor(visitor);
            updateIncomeControl.setReason(incomeControl.getReason());
            updateIncomeControl.setPlace(incomeControl.getPlace());
            updateIncomeControl.setAnswerable(incomeControl.getAnswerable());
            return incomeControlRepository.save(updateIncomeControl);

        }
    }

    /**
     * Get all the incomeControls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IncomeControl> findAll(Pageable pageable) {
        log.debug("Request to get all IncomeControls");
        return incomeControlRepository.findAll(pageable);
    }

    /**
     * Get one incomeControl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncomeControl> findOne(Long id) {
        log.debug("Request to get IncomeControl : {}", id);
        return incomeControlRepository.findById(id);
    }

    /**
     * Delete the incomeControl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IncomeControl : {}", id);
        incomeControlRepository.deleteById(id);
    }

    public void exit(Long id) throws IncomeControlNotExist, IncomeControlDepartureDateExist {
        log.debug("Request to exit IncomeControl : {}", id);
        Optional<IncomeControl> optionalIncomeControl = incomeControlRepository.findById(id);
        if (optionalIncomeControl.isPresent()) {
            IncomeControl incomeControl = optionalIncomeControl.get();
            if (incomeControl.getDepartureDate() != null)
                throw new IncomeControlDepartureDateExist();
            incomeControl.setDepartureDate(ZonedDateTime.now());
            incomeControlRepository.save(incomeControl);
        } else
            throw new IncomeControlNotExist();
    }
}
