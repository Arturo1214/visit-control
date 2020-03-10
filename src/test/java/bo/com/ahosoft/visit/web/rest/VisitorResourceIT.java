package bo.com.ahosoft.visit.web.rest;

import bo.com.ahosoft.visit.VisitApp;
import bo.com.ahosoft.visit.domain.Visitor;
import bo.com.ahosoft.visit.domain.DocumentType;
import bo.com.ahosoft.visit.repository.VisitorRepository;
import bo.com.ahosoft.visit.web.rest.errors.ExceptionTranslator;
import bo.com.ahosoft.visit.service.VisitorQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static bo.com.ahosoft.visit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VisitorResource} REST controller.
 */
@SpringBootTest(classes = VisitApp.class)
public class VisitorResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_NAME = "BBBBBBBBBB";

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitorQueryService visitorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVisitorMockMvc;

    private Visitor visitor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VisitorResource visitorResource = new VisitorResource(visitorService, visitorQueryService, fileUtilService, visitorRepository);
        this.restVisitorMockMvc = MockMvcBuilders.standaloneSetup(visitorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitor createEntity(EntityManager em) {
        Visitor visitor = new Visitor()
            .fullName(DEFAULT_FULL_NAME)
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .business(DEFAULT_BUSINESS)
            .position(DEFAULT_POSITION)
            .imageName(DEFAULT_IMAGE_NAME);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        visitor.setDocumentType(documentType);
        return visitor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitor createUpdatedEntity(EntityManager em) {
        Visitor visitor = new Visitor()
            .fullName(UPDATED_FULL_NAME)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .business(UPDATED_BUSINESS)
            .position(UPDATED_POSITION)
            .imageName(UPDATED_IMAGE_NAME);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createUpdatedEntity(em);
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        visitor.setDocumentType(documentType);
        return visitor;
    }

    @BeforeEach
    public void initTest() {
        visitor = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisitor() throws Exception {
        int databaseSizeBeforeCreate = visitorRepository.findAll().size();

        // Create the Visitor
        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isCreated());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate + 1);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testVisitor.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testVisitor.getBusiness()).isEqualTo(DEFAULT_BUSINESS);
        assertThat(testVisitor.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testVisitor.getImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void createVisitorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitorRepository.findAll().size();

        // Create the Visitor with an existing ID
        visitor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setFullName(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setDocumentNumber(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisitors() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList
        restVisitorMockMvc.perform(get("/api/visitors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].business").value(hasItem(DEFAULT_BUSINESS)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME)));
    }

    @Test
    @Transactional
    public void getVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get the visitor
        restVisitorMockMvc.perform(get("/api/visitors/{id}", visitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visitor.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.business").value(DEFAULT_BUSINESS))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.imageName").value(DEFAULT_IMAGE_NAME));
    }


    @Test
    @Transactional
    public void getVisitorsByIdFiltering() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        Long id = visitor.getId();

        defaultVisitorShouldBeFound("id.equals=" + id);
        defaultVisitorShouldNotBeFound("id.notEquals=" + id);

        defaultVisitorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVisitorShouldNotBeFound("id.greaterThan=" + id);

        defaultVisitorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVisitorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVisitorsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName equals to DEFAULT_FULL_NAME
        defaultVisitorShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the visitorList where fullName equals to UPDATED_FULL_NAME
        defaultVisitorShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName not equals to DEFAULT_FULL_NAME
        defaultVisitorShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the visitorList where fullName not equals to UPDATED_FULL_NAME
        defaultVisitorShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultVisitorShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the visitorList where fullName equals to UPDATED_FULL_NAME
        defaultVisitorShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName is not null
        defaultVisitorShouldBeFound("fullName.specified=true");

        // Get all the visitorList where fullName is null
        defaultVisitorShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitorsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName contains DEFAULT_FULL_NAME
        defaultVisitorShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the visitorList where fullName contains UPDATED_FULL_NAME
        defaultVisitorShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where fullName does not contain DEFAULT_FULL_NAME
        defaultVisitorShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the visitorList where fullName does not contain UPDATED_FULL_NAME
        defaultVisitorShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber equals to DEFAULT_DOCUMENT_NUMBER
        defaultVisitorShouldBeFound("documentNumber.equals=" + DEFAULT_DOCUMENT_NUMBER);

        // Get all the visitorList where documentNumber equals to UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldNotBeFound("documentNumber.equals=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber not equals to DEFAULT_DOCUMENT_NUMBER
        defaultVisitorShouldNotBeFound("documentNumber.notEquals=" + DEFAULT_DOCUMENT_NUMBER);

        // Get all the visitorList where documentNumber not equals to UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldBeFound("documentNumber.notEquals=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber in DEFAULT_DOCUMENT_NUMBER or UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldBeFound("documentNumber.in=" + DEFAULT_DOCUMENT_NUMBER + "," + UPDATED_DOCUMENT_NUMBER);

        // Get all the visitorList where documentNumber equals to UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldNotBeFound("documentNumber.in=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber is not null
        defaultVisitorShouldBeFound("documentNumber.specified=true");

        // Get all the visitorList where documentNumber is null
        defaultVisitorShouldNotBeFound("documentNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber contains DEFAULT_DOCUMENT_NUMBER
        defaultVisitorShouldBeFound("documentNumber.contains=" + DEFAULT_DOCUMENT_NUMBER);

        // Get all the visitorList where documentNumber contains UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldNotBeFound("documentNumber.contains=" + UPDATED_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVisitorsByDocumentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where documentNumber does not contain DEFAULT_DOCUMENT_NUMBER
        defaultVisitorShouldNotBeFound("documentNumber.doesNotContain=" + DEFAULT_DOCUMENT_NUMBER);

        // Get all the visitorList where documentNumber does not contain UPDATED_DOCUMENT_NUMBER
        defaultVisitorShouldBeFound("documentNumber.doesNotContain=" + UPDATED_DOCUMENT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllVisitorsByBusinessIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business equals to DEFAULT_BUSINESS
        defaultVisitorShouldBeFound("business.equals=" + DEFAULT_BUSINESS);

        // Get all the visitorList where business equals to UPDATED_BUSINESS
        defaultVisitorShouldNotBeFound("business.equals=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllVisitorsByBusinessIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business not equals to DEFAULT_BUSINESS
        defaultVisitorShouldNotBeFound("business.notEquals=" + DEFAULT_BUSINESS);

        // Get all the visitorList where business not equals to UPDATED_BUSINESS
        defaultVisitorShouldBeFound("business.notEquals=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllVisitorsByBusinessIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business in DEFAULT_BUSINESS or UPDATED_BUSINESS
        defaultVisitorShouldBeFound("business.in=" + DEFAULT_BUSINESS + "," + UPDATED_BUSINESS);

        // Get all the visitorList where business equals to UPDATED_BUSINESS
        defaultVisitorShouldNotBeFound("business.in=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllVisitorsByBusinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business is not null
        defaultVisitorShouldBeFound("business.specified=true");

        // Get all the visitorList where business is null
        defaultVisitorShouldNotBeFound("business.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitorsByBusinessContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business contains DEFAULT_BUSINESS
        defaultVisitorShouldBeFound("business.contains=" + DEFAULT_BUSINESS);

        // Get all the visitorList where business contains UPDATED_BUSINESS
        defaultVisitorShouldNotBeFound("business.contains=" + UPDATED_BUSINESS);
    }

    @Test
    @Transactional
    public void getAllVisitorsByBusinessNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where business does not contain DEFAULT_BUSINESS
        defaultVisitorShouldNotBeFound("business.doesNotContain=" + DEFAULT_BUSINESS);

        // Get all the visitorList where business does not contain UPDATED_BUSINESS
        defaultVisitorShouldBeFound("business.doesNotContain=" + UPDATED_BUSINESS);
    }


    @Test
    @Transactional
    public void getAllVisitorsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position equals to DEFAULT_POSITION
        defaultVisitorShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the visitorList where position equals to UPDATED_POSITION
        defaultVisitorShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllVisitorsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position not equals to DEFAULT_POSITION
        defaultVisitorShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the visitorList where position not equals to UPDATED_POSITION
        defaultVisitorShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllVisitorsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultVisitorShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the visitorList where position equals to UPDATED_POSITION
        defaultVisitorShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllVisitorsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position is not null
        defaultVisitorShouldBeFound("position.specified=true");

        // Get all the visitorList where position is null
        defaultVisitorShouldNotBeFound("position.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitorsByPositionContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position contains DEFAULT_POSITION
        defaultVisitorShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the visitorList where position contains UPDATED_POSITION
        defaultVisitorShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllVisitorsByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where position does not contain DEFAULT_POSITION
        defaultVisitorShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the visitorList where position does not contain UPDATED_POSITION
        defaultVisitorShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }


    @Test
    @Transactional
    public void getAllVisitorsByImageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName equals to DEFAULT_IMAGE_NAME
        defaultVisitorShouldBeFound("imageName.equals=" + DEFAULT_IMAGE_NAME);

        // Get all the visitorList where imageName equals to UPDATED_IMAGE_NAME
        defaultVisitorShouldNotBeFound("imageName.equals=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByImageNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName not equals to DEFAULT_IMAGE_NAME
        defaultVisitorShouldNotBeFound("imageName.notEquals=" + DEFAULT_IMAGE_NAME);

        // Get all the visitorList where imageName not equals to UPDATED_IMAGE_NAME
        defaultVisitorShouldBeFound("imageName.notEquals=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByImageNameIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName in DEFAULT_IMAGE_NAME or UPDATED_IMAGE_NAME
        defaultVisitorShouldBeFound("imageName.in=" + DEFAULT_IMAGE_NAME + "," + UPDATED_IMAGE_NAME);

        // Get all the visitorList where imageName equals to UPDATED_IMAGE_NAME
        defaultVisitorShouldNotBeFound("imageName.in=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByImageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName is not null
        defaultVisitorShouldBeFound("imageName.specified=true");

        // Get all the visitorList where imageName is null
        defaultVisitorShouldNotBeFound("imageName.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitorsByImageNameContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName contains DEFAULT_IMAGE_NAME
        defaultVisitorShouldBeFound("imageName.contains=" + DEFAULT_IMAGE_NAME);

        // Get all the visitorList where imageName contains UPDATED_IMAGE_NAME
        defaultVisitorShouldNotBeFound("imageName.contains=" + UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void getAllVisitorsByImageNameNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where imageName does not contain DEFAULT_IMAGE_NAME
        defaultVisitorShouldNotBeFound("imageName.doesNotContain=" + DEFAULT_IMAGE_NAME);

        // Get all the visitorList where imageName does not contain UPDATED_IMAGE_NAME
        defaultVisitorShouldBeFound("imageName.doesNotContain=" + UPDATED_IMAGE_NAME);
    }


    @Test
    @Transactional
    public void getAllVisitorsByDocumentTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        DocumentType documentType = visitor.getDocumentType();
        visitorRepository.saveAndFlush(visitor);
        Long documentTypeId = documentType.getId();

        // Get all the visitorList where documentType equals to documentTypeId
        defaultVisitorShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the visitorList where documentType equals to documentTypeId + 1
        defaultVisitorShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVisitorShouldBeFound(String filter) throws Exception {
        restVisitorMockMvc.perform(get("/api/visitors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].business").value(hasItem(DEFAULT_BUSINESS)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME)));

        // Check, that the count call also returns 1
        restVisitorMockMvc.perform(get("/api/visitors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVisitorShouldNotBeFound(String filter) throws Exception {
        restVisitorMockMvc.perform(get("/api/visitors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVisitorMockMvc.perform(get("/api/visitors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVisitor() throws Exception {
        // Get the visitor
        restVisitorMockMvc.perform(get("/api/visitors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisitor() throws Exception {
        // Initialize the database
        visitorService.save(visitor);

        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Update the visitor
        Visitor updatedVisitor = visitorRepository.findById(visitor.getId()).get();
        // Disconnect from session so that the updates on updatedVisitor are not directly saved in db
        em.detach(updatedVisitor);
        updatedVisitor
            .fullName(UPDATED_FULL_NAME)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .business(UPDATED_BUSINESS)
            .position(UPDATED_POSITION)
            .imageName(UPDATED_IMAGE_NAME);

        restVisitorMockMvc.perform(put("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVisitor)))
            .andExpect(status().isOk());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testVisitor.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testVisitor.getBusiness()).isEqualTo(UPDATED_BUSINESS);
        assertThat(testVisitor.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testVisitor.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Create the Visitor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorMockMvc.perform(put("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVisitor() throws Exception {
        // Initialize the database
        visitorService.save(visitor);

        int databaseSizeBeforeDelete = visitorRepository.findAll().size();

        // Delete the visitor
        restVisitorMockMvc.perform(delete("/api/visitors/{id}", visitor.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
