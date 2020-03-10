package bo.com.ahosoft.visit.web.rest;

import bo.com.ahosoft.visit.VisitApp;
import bo.com.ahosoft.visit.domain.IncomeControl;
import bo.com.ahosoft.visit.domain.Visitor;
import bo.com.ahosoft.visit.repository.IncomeControlRepository;
import bo.com.ahosoft.visit.service.IncomeControlService;
import bo.com.ahosoft.visit.web.rest.errors.ExceptionTranslator;
import bo.com.ahosoft.visit.service.IncomeControlQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static bo.com.ahosoft.visit.web.rest.TestUtil.sameInstant;
import static bo.com.ahosoft.visit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IncomeControlResource} REST controller.
 */
@SpringBootTest(classes = VisitApp.class)
public class IncomeControlResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWERABLE = "AAAAAAAAAA";
    private static final String UPDATED_ANSWERABLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ADMISSION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ADMISSION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ADMISSION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DEPARTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DEPARTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private IncomeControlRepository incomeControlRepository;

    @Autowired
    private IncomeControlService incomeControlService;

    @Autowired
    private IncomeControlQueryService incomeControlQueryService;

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

    private MockMvc restIncomeControlMockMvc;

    private IncomeControl incomeControl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IncomeControlResource incomeControlResource = new IncomeControlResource(incomeControlService, incomeControlQueryService, fileUtilService);
        this.restIncomeControlMockMvc = MockMvcBuilders.standaloneSetup(incomeControlResource)
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
    public static IncomeControl createEntity(EntityManager em) {
        IncomeControl incomeControl = new IncomeControl()
            .reason(DEFAULT_REASON)
            .place(DEFAULT_PLACE)
            .answerable(DEFAULT_ANSWERABLE)
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .departureDate(DEFAULT_DEPARTURE_DATE);
        // Add required entity
        Visitor visitor;
        if (TestUtil.findAll(em, Visitor.class).isEmpty()) {
            visitor = VisitorResourceIT.createEntity(em);
            em.persist(visitor);
            em.flush();
        } else {
            visitor = TestUtil.findAll(em, Visitor.class).get(0);
        }
        incomeControl.setVisitor(visitor);
        return incomeControl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeControl createUpdatedEntity(EntityManager em) {
        IncomeControl incomeControl = new IncomeControl()
            .reason(UPDATED_REASON)
            .place(UPDATED_PLACE)
            .answerable(UPDATED_ANSWERABLE)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .departureDate(UPDATED_DEPARTURE_DATE);
        // Add required entity
        Visitor visitor;
        if (TestUtil.findAll(em, Visitor.class).isEmpty()) {
            visitor = VisitorResourceIT.createUpdatedEntity(em);
            em.persist(visitor);
            em.flush();
        } else {
            visitor = TestUtil.findAll(em, Visitor.class).get(0);
        }
        incomeControl.setVisitor(visitor);
        return incomeControl;
    }

    @BeforeEach
    public void initTest() {
        incomeControl = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncomeControl() throws Exception {
        int databaseSizeBeforeCreate = incomeControlRepository.findAll().size();

        // Create the IncomeControl
        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isCreated());

        // Validate the IncomeControl in the database
        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeCreate + 1);
        IncomeControl testIncomeControl = incomeControlList.get(incomeControlList.size() - 1);
        assertThat(testIncomeControl.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testIncomeControl.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testIncomeControl.getAnswerable()).isEqualTo(DEFAULT_ANSWERABLE);
        assertThat(testIncomeControl.getAdmissionDate()).isEqualTo(DEFAULT_ADMISSION_DATE);
        assertThat(testIncomeControl.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void createIncomeControlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incomeControlRepository.findAll().size();

        // Create the IncomeControl with an existing ID
        incomeControl.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        // Validate the IncomeControl in the database
        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeControlRepository.findAll().size();
        // set the field null
        incomeControl.setReason(null);

        // Create the IncomeControl, which fails.

        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeControlRepository.findAll().size();
        // set the field null
        incomeControl.setPlace(null);

        // Create the IncomeControl, which fails.

        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnswerableIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeControlRepository.findAll().size();
        // set the field null
        incomeControl.setAnswerable(null);

        // Create the IncomeControl, which fails.

        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeControlRepository.findAll().size();
        // set the field null
        incomeControl.setAdmissionDate(null);

        // Create the IncomeControl, which fails.

        restIncomeControlMockMvc.perform(post("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIncomeControls() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList
        restIncomeControlMockMvc.perform(get("/api/income-controls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].answerable").value(hasItem(DEFAULT_ANSWERABLE)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(sameInstant(DEFAULT_ADMISSION_DATE))))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(sameInstant(DEFAULT_DEPARTURE_DATE))));
    }

    @Test
    @Transactional
    public void getIncomeControl() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get the incomeControl
        restIncomeControlMockMvc.perform(get("/api/income-controls/{id}", incomeControl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomeControl.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.answerable").value(DEFAULT_ANSWERABLE))
            .andExpect(jsonPath("$.admissionDate").value(sameInstant(DEFAULT_ADMISSION_DATE)))
            .andExpect(jsonPath("$.departureDate").value(sameInstant(DEFAULT_DEPARTURE_DATE)));
    }


    @Test
    @Transactional
    public void getIncomeControlsByIdFiltering() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        Long id = incomeControl.getId();

        defaultIncomeControlShouldBeFound("id.equals=" + id);
        defaultIncomeControlShouldNotBeFound("id.notEquals=" + id);

        defaultIncomeControlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIncomeControlShouldNotBeFound("id.greaterThan=" + id);

        defaultIncomeControlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIncomeControlShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason equals to DEFAULT_REASON
        defaultIncomeControlShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the incomeControlList where reason equals to UPDATED_REASON
        defaultIncomeControlShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByReasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason not equals to DEFAULT_REASON
        defaultIncomeControlShouldNotBeFound("reason.notEquals=" + DEFAULT_REASON);

        // Get all the incomeControlList where reason not equals to UPDATED_REASON
        defaultIncomeControlShouldBeFound("reason.notEquals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultIncomeControlShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the incomeControlList where reason equals to UPDATED_REASON
        defaultIncomeControlShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason is not null
        defaultIncomeControlShouldBeFound("reason.specified=true");

        // Get all the incomeControlList where reason is null
        defaultIncomeControlShouldNotBeFound("reason.specified=false");
    }
                @Test
    @Transactional
    public void getAllIncomeControlsByReasonContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason contains DEFAULT_REASON
        defaultIncomeControlShouldBeFound("reason.contains=" + DEFAULT_REASON);

        // Get all the incomeControlList where reason contains UPDATED_REASON
        defaultIncomeControlShouldNotBeFound("reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where reason does not contain DEFAULT_REASON
        defaultIncomeControlShouldNotBeFound("reason.doesNotContain=" + DEFAULT_REASON);

        // Get all the incomeControlList where reason does not contain UPDATED_REASON
        defaultIncomeControlShouldBeFound("reason.doesNotContain=" + UPDATED_REASON);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place equals to DEFAULT_PLACE
        defaultIncomeControlShouldBeFound("place.equals=" + DEFAULT_PLACE);

        // Get all the incomeControlList where place equals to UPDATED_PLACE
        defaultIncomeControlShouldNotBeFound("place.equals=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByPlaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place not equals to DEFAULT_PLACE
        defaultIncomeControlShouldNotBeFound("place.notEquals=" + DEFAULT_PLACE);

        // Get all the incomeControlList where place not equals to UPDATED_PLACE
        defaultIncomeControlShouldBeFound("place.notEquals=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place in DEFAULT_PLACE or UPDATED_PLACE
        defaultIncomeControlShouldBeFound("place.in=" + DEFAULT_PLACE + "," + UPDATED_PLACE);

        // Get all the incomeControlList where place equals to UPDATED_PLACE
        defaultIncomeControlShouldNotBeFound("place.in=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place is not null
        defaultIncomeControlShouldBeFound("place.specified=true");

        // Get all the incomeControlList where place is null
        defaultIncomeControlShouldNotBeFound("place.specified=false");
    }
                @Test
    @Transactional
    public void getAllIncomeControlsByPlaceContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place contains DEFAULT_PLACE
        defaultIncomeControlShouldBeFound("place.contains=" + DEFAULT_PLACE);

        // Get all the incomeControlList where place contains UPDATED_PLACE
        defaultIncomeControlShouldNotBeFound("place.contains=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where place does not contain DEFAULT_PLACE
        defaultIncomeControlShouldNotBeFound("place.doesNotContain=" + DEFAULT_PLACE);

        // Get all the incomeControlList where place does not contain UPDATED_PLACE
        defaultIncomeControlShouldBeFound("place.doesNotContain=" + UPDATED_PLACE);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableIsEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable equals to DEFAULT_ANSWERABLE
        defaultIncomeControlShouldBeFound("answerable.equals=" + DEFAULT_ANSWERABLE);

        // Get all the incomeControlList where answerable equals to UPDATED_ANSWERABLE
        defaultIncomeControlShouldNotBeFound("answerable.equals=" + UPDATED_ANSWERABLE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable not equals to DEFAULT_ANSWERABLE
        defaultIncomeControlShouldNotBeFound("answerable.notEquals=" + DEFAULT_ANSWERABLE);

        // Get all the incomeControlList where answerable not equals to UPDATED_ANSWERABLE
        defaultIncomeControlShouldBeFound("answerable.notEquals=" + UPDATED_ANSWERABLE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableIsInShouldWork() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable in DEFAULT_ANSWERABLE or UPDATED_ANSWERABLE
        defaultIncomeControlShouldBeFound("answerable.in=" + DEFAULT_ANSWERABLE + "," + UPDATED_ANSWERABLE);

        // Get all the incomeControlList where answerable equals to UPDATED_ANSWERABLE
        defaultIncomeControlShouldNotBeFound("answerable.in=" + UPDATED_ANSWERABLE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable is not null
        defaultIncomeControlShouldBeFound("answerable.specified=true");

        // Get all the incomeControlList where answerable is null
        defaultIncomeControlShouldNotBeFound("answerable.specified=false");
    }
                @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable contains DEFAULT_ANSWERABLE
        defaultIncomeControlShouldBeFound("answerable.contains=" + DEFAULT_ANSWERABLE);

        // Get all the incomeControlList where answerable contains UPDATED_ANSWERABLE
        defaultIncomeControlShouldNotBeFound("answerable.contains=" + UPDATED_ANSWERABLE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAnswerableNotContainsSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where answerable does not contain DEFAULT_ANSWERABLE
        defaultIncomeControlShouldNotBeFound("answerable.doesNotContain=" + DEFAULT_ANSWERABLE);

        // Get all the incomeControlList where answerable does not contain UPDATED_ANSWERABLE
        defaultIncomeControlShouldBeFound("answerable.doesNotContain=" + UPDATED_ANSWERABLE);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate equals to DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.equals=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate equals to UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.equals=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate not equals to DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.notEquals=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate not equals to UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.notEquals=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsInShouldWork() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate in DEFAULT_ADMISSION_DATE or UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.in=" + DEFAULT_ADMISSION_DATE + "," + UPDATED_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate equals to UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.in=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate is not null
        defaultIncomeControlShouldBeFound("admissionDate.specified=true");

        // Get all the incomeControlList where admissionDate is null
        defaultIncomeControlShouldNotBeFound("admissionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate is greater than or equal to DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.greaterThanOrEqual=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate is greater than or equal to UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.greaterThanOrEqual=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate is less than or equal to DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.lessThanOrEqual=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate is less than or equal to SMALLER_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.lessThanOrEqual=" + SMALLER_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate is less than DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.lessThan=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate is less than UPDATED_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.lessThan=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByAdmissionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where admissionDate is greater than DEFAULT_ADMISSION_DATE
        defaultIncomeControlShouldNotBeFound("admissionDate.greaterThan=" + DEFAULT_ADMISSION_DATE);

        // Get all the incomeControlList where admissionDate is greater than SMALLER_ADMISSION_DATE
        defaultIncomeControlShouldBeFound("admissionDate.greaterThan=" + SMALLER_ADMISSION_DATE);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate equals to DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.equals=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate equals to UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.equals=" + UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate not equals to DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.notEquals=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate not equals to UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.notEquals=" + UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsInShouldWork() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate in DEFAULT_DEPARTURE_DATE or UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.in=" + DEFAULT_DEPARTURE_DATE + "," + UPDATED_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate equals to UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.in=" + UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate is not null
        defaultIncomeControlShouldBeFound("departureDate.specified=true");

        // Get all the incomeControlList where departureDate is null
        defaultIncomeControlShouldNotBeFound("departureDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate is greater than or equal to DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.greaterThanOrEqual=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate is greater than or equal to UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.greaterThanOrEqual=" + UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate is less than or equal to DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.lessThanOrEqual=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate is less than or equal to SMALLER_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.lessThanOrEqual=" + SMALLER_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate is less than DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.lessThan=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate is less than UPDATED_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.lessThan=" + UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllIncomeControlsByDepartureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        incomeControlRepository.saveAndFlush(incomeControl);

        // Get all the incomeControlList where departureDate is greater than DEFAULT_DEPARTURE_DATE
        defaultIncomeControlShouldNotBeFound("departureDate.greaterThan=" + DEFAULT_DEPARTURE_DATE);

        // Get all the incomeControlList where departureDate is greater than SMALLER_DEPARTURE_DATE
        defaultIncomeControlShouldBeFound("departureDate.greaterThan=" + SMALLER_DEPARTURE_DATE);
    }


    @Test
    @Transactional
    public void getAllIncomeControlsByVisitorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Visitor visitor = incomeControl.getVisitor();
        incomeControlRepository.saveAndFlush(incomeControl);
        Long visitorId = visitor.getId();

        // Get all the incomeControlList where visitor equals to visitorId
        defaultIncomeControlShouldBeFound("visitorId.equals=" + visitorId);

        // Get all the incomeControlList where visitor equals to visitorId + 1
        defaultIncomeControlShouldNotBeFound("visitorId.equals=" + (visitorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIncomeControlShouldBeFound(String filter) throws Exception {
        restIncomeControlMockMvc.perform(get("/api/income-controls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].answerable").value(hasItem(DEFAULT_ANSWERABLE)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(sameInstant(DEFAULT_ADMISSION_DATE))))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(sameInstant(DEFAULT_DEPARTURE_DATE))));

        // Check, that the count call also returns 1
        restIncomeControlMockMvc.perform(get("/api/income-controls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIncomeControlShouldNotBeFound(String filter) throws Exception {
        restIncomeControlMockMvc.perform(get("/api/income-controls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIncomeControlMockMvc.perform(get("/api/income-controls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIncomeControl() throws Exception {
        // Get the incomeControl
        restIncomeControlMockMvc.perform(get("/api/income-controls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncomeControl() throws Exception {
        // Initialize the database
        incomeControlService.save(incomeControl);

        int databaseSizeBeforeUpdate = incomeControlRepository.findAll().size();

        // Update the incomeControl
        IncomeControl updatedIncomeControl = incomeControlRepository.findById(incomeControl.getId()).get();
        // Disconnect from session so that the updates on updatedIncomeControl are not directly saved in db
        em.detach(updatedIncomeControl);
        updatedIncomeControl
            .reason(UPDATED_REASON)
            .place(UPDATED_PLACE)
            .answerable(UPDATED_ANSWERABLE)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .departureDate(UPDATED_DEPARTURE_DATE);

        restIncomeControlMockMvc.perform(put("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncomeControl)))
            .andExpect(status().isOk());

        // Validate the IncomeControl in the database
        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeUpdate);
        IncomeControl testIncomeControl = incomeControlList.get(incomeControlList.size() - 1);
        assertThat(testIncomeControl.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testIncomeControl.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testIncomeControl.getAnswerable()).isEqualTo(UPDATED_ANSWERABLE);
        assertThat(testIncomeControl.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testIncomeControl.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingIncomeControl() throws Exception {
        int databaseSizeBeforeUpdate = incomeControlRepository.findAll().size();

        // Create the IncomeControl

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeControlMockMvc.perform(put("/api/income-controls")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incomeControl)))
            .andExpect(status().isBadRequest());

        // Validate the IncomeControl in the database
        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIncomeControl() throws Exception {
        // Initialize the database
        incomeControlService.save(incomeControl);

        int databaseSizeBeforeDelete = incomeControlRepository.findAll().size();

        // Delete the incomeControl
        restIncomeControlMockMvc.perform(delete("/api/income-controls/{id}", incomeControl.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncomeControl> incomeControlList = incomeControlRepository.findAll();
        assertThat(incomeControlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
