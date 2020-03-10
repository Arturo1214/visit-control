package bo.com.ahosoft.visit.service;

import bo.com.ahosoft.visit.domain.Visitor;
import bo.com.ahosoft.visit.repository.VisitorRepository;
import bo.com.ahosoft.visit.service.util.FileUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Visitor}.
 */
@Service
@Transactional
public class VisitorService {

    private final Logger log = LoggerFactory.getLogger(VisitorService.class);

    private final VisitorRepository visitorRepository;

    private final FileUtilService fileUtilService;

    public VisitorService(VisitorRepository visitorRepository, FileUtilService fileUtilService) {
        this.visitorRepository = visitorRepository;
        this.fileUtilService = fileUtilService;
    }

    /**
     * Save a visitor.
     *
     * @param visitor the entity to save.
     * @return the persisted entity.
     */
    public Visitor save(Visitor visitor) throws IOException {
        log.debug("Request to save Visitor : {}", visitor);
        String imageName = visitor.getDocumentNumber()
            .concat("-")
            .concat(visitor.getDocumentType().getId().toString());
        fileUtilService.createOrUpdateFile(imageName, visitor.getImageName().getBytes(StandardCharsets.UTF_8));
        visitor.setImageName(imageName);
        return visitorRepository.save(visitor);
    }

    /**
     * Get all the visitors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Visitor> findAll(Pageable pageable) {
        log.debug("Request to get all Visitors");
        return visitorRepository.findAll(pageable);
    }

    /**
     * Get one visitor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Visitor> findOne(Long id) {
        log.debug("Request to get Visitor : {}", id);
        return visitorRepository.findById(id);
    }

    /**
     * Delete the visitor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Visitor : {}", id);
        visitorRepository.deleteById(id);
    }
}
