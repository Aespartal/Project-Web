package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.ExtendedUser;
import es.project.domain.User;
import es.project.repository.ExtendedUserRepository;
import es.project.service.criteria.ExtendedUserCriteria;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.mapper.ExtendedUserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExtendedUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtendedUserResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Double DEFAULT_HEIGHT = 0D;
    private static final Double UPDATED_HEIGHT = 1D;
    private static final Double SMALLER_HEIGHT = 0D - 1D;

    private static final Double DEFAULT_WEIGHT = 0D;
    private static final Double UPDATED_WEIGHT = 1D;
    private static final Double SMALLER_WEIGHT = 0D - 1D;

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TOTAL_FOLLOWERS = 0;
    private static final Integer UPDATED_TOTAL_FOLLOWERS = 1;
    private static final Integer SMALLER_TOTAL_FOLLOWERS = 0 - 1;

    private static final Integer DEFAULT_TOTAL_FOLLOWING = 0;
    private static final Integer UPDATED_TOTAL_FOLLOWING = 1;
    private static final Integer SMALLER_TOTAL_FOLLOWING = 0 - 1;

    private static final Integer DEFAULT_TOTAL_IMAGES = 0;
    private static final Integer UPDATED_TOTAL_IMAGES = 1;
    private static final Integer SMALLER_TOTAL_IMAGES = 0 - 1;

    private static final Integer DEFAULT_TOTAL_NOTIFICATIONS = 0;
    private static final Integer UPDATED_TOTAL_NOTIFICATIONS = 1;
    private static final Integer SMALLER_TOTAL_NOTIFICATIONS = 0 - 1;

    private static final String ENTITY_API_URL = "/api/extended-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    @Autowired
    private ExtendedUserMapper extendedUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtendedUserMockMvc;

    private ExtendedUser extendedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser()
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .height(DEFAULT_HEIGHT)
            .weight(DEFAULT_WEIGHT)
            .birthDate(DEFAULT_BIRTH_DATE)
            .totalFollowers(DEFAULT_TOTAL_FOLLOWERS)
            .totalFollowing(DEFAULT_TOTAL_FOLLOWING)
            .totalImages(DEFAULT_TOTAL_IMAGES)
            .totalNotifications(DEFAULT_TOTAL_NOTIFICATIONS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        extendedUser.setUser(user);
        return extendedUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createUpdatedEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser()
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .birthDate(UPDATED_BIRTH_DATE)
            .totalFollowers(UPDATED_TOTAL_FOLLOWERS)
            .totalFollowing(UPDATED_TOTAL_FOLLOWING)
            .totalImages(UPDATED_TOTAL_IMAGES)
            .totalNotifications(UPDATED_TOTAL_NOTIFICATIONS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        extendedUser.setUser(user);
        return extendedUser;
    }

    @BeforeEach
    public void initTest() {
        extendedUser = createEntity(em);
    }

    @Test
    @Transactional
    void createExtendedUserWithExistingId() throws Exception {
        // Create the ExtendedUser with an existing ID
        extendedUser.setId(1L);
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = extendedUserRepository.findAll().size();
        // set the field null
        extendedUser.setHeight(null);

        // Create the ExtendedUser, which fails.
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = extendedUserRepository.findAll().size();
        // set the field null
        extendedUser.setWeight(null);

        // Create the ExtendedUser, which fails.
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = extendedUserRepository.findAll().size();
        // set the field null
        extendedUser.setBirthDate(null);

        // Create the ExtendedUser, which fails.
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalImagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = extendedUserRepository.findAll().size();
        // set the field null
        extendedUser.setTotalImages(null);

        // Create the ExtendedUser, which fails.
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = extendedUserRepository.findAll().size();
        // set the field null
        extendedUser.setTotalNotifications(null);

        // Create the ExtendedUser, which fails.
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExtendedUsers() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalFollowers").value(hasItem(DEFAULT_TOTAL_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].totalFollowing").value(hasItem(DEFAULT_TOTAL_FOLLOWING)))
            .andExpect(jsonPath("$.[*].totalImages").value(hasItem(DEFAULT_TOTAL_IMAGES)))
            .andExpect(jsonPath("$.[*].totalNotifications").value(hasItem(DEFAULT_TOTAL_NOTIFICATIONS)));
    }

    @Test
    @Transactional
    void getExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get the extendedUser
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL_ID, extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extendedUser.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.totalFollowers").value(DEFAULT_TOTAL_FOLLOWERS))
            .andExpect(jsonPath("$.totalFollowing").value(DEFAULT_TOTAL_FOLLOWING))
            .andExpect(jsonPath("$.totalImages").value(DEFAULT_TOTAL_IMAGES))
            .andExpect(jsonPath("$.totalNotifications").value(DEFAULT_TOTAL_NOTIFICATIONS));
    }

    @Test
    @Transactional
    void getExtendedUsersByIdFiltering() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        Long id = extendedUser.getId();

        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&id.equals=" + id);
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&id.notEquals=" + id);

        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&id.greaterThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&id.greaterThan=" + id);

        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&id.lessThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where description equals to DEFAULT_DESCRIPTION
        defaultExtendedUserShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the extendedUserList where description equals to UPDATED_DESCRIPTION
        defaultExtendedUserShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExtendedUserShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the extendedUserList where description equals to UPDATED_DESCRIPTION
        defaultExtendedUserShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where description is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&description.specified=true");

        // Get all the extendedUserList where description is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&description.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where description contains DEFAULT_DESCRIPTION
        defaultExtendedUserShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the extendedUserList where description contains UPDATED_DESCRIPTION
        defaultExtendedUserShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where description does not contain DEFAULT_DESCRIPTION
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the extendedUserList where description does not contain UPDATED_DESCRIPTION
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where location equals to DEFAULT_LOCATION
        defaultExtendedUserShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the extendedUserList where location equals to UPDATED_LOCATION
        defaultExtendedUserShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultExtendedUserShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the extendedUserList where location equals to UPDATED_LOCATION
        defaultExtendedUserShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where location is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&location.specified=true");

        // Get all the extendedUserList where location is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&location.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByLocationContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where location contains DEFAULT_LOCATION
        defaultExtendedUserShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the extendedUserList where location contains UPDATED_LOCATION
        defaultExtendedUserShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where location does not contain DEFAULT_LOCATION
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the extendedUserList where location does not contain UPDATED_LOCATION
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height equals to DEFAULT_HEIGHT
        defaultExtendedUserShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the extendedUserList where height equals to UPDATED_HEIGHT
        defaultExtendedUserShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultExtendedUserShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the extendedUserList where height equals to UPDATED_HEIGHT
        defaultExtendedUserShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&height.specified=true");

        // Get all the extendedUserList where height is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&height.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height is greater than or equal to DEFAULT_HEIGHT
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the extendedUserList where height is greater than or equal to UPDATED_HEIGHT
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height is less than or equal to DEFAULT_HEIGHT
        defaultExtendedUserShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the extendedUserList where height is less than or equal to SMALLER_HEIGHT
        defaultExtendedUserShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height is less than DEFAULT_HEIGHT
        defaultExtendedUserShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the extendedUserList where height is less than UPDATED_HEIGHT
        defaultExtendedUserShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where height is greater than DEFAULT_HEIGHT
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the extendedUserList where height is greater than SMALLER_HEIGHT
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight equals to DEFAULT_WEIGHT
        defaultExtendedUserShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the extendedUserList where weight equals to UPDATED_WEIGHT
        defaultExtendedUserShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultExtendedUserShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the extendedUserList where weight equals to UPDATED_WEIGHT
        defaultExtendedUserShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&weight.specified=true");

        // Get all the extendedUserList where weight is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&weight.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the extendedUserList where weight is greater than or equal to UPDATED_WEIGHT
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight is less than or equal to DEFAULT_WEIGHT
        defaultExtendedUserShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the extendedUserList where weight is less than or equal to SMALLER_WEIGHT
        defaultExtendedUserShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight is less than DEFAULT_WEIGHT
        defaultExtendedUserShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the extendedUserList where weight is less than UPDATED_WEIGHT
        defaultExtendedUserShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where weight is greater than DEFAULT_WEIGHT
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the extendedUserList where weight is greater than SMALLER_WEIGHT
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&weight.greaterThan=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultExtendedUserShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the extendedUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultExtendedUserShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultExtendedUserShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the extendedUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultExtendedUserShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where birthDate is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&birthDate.specified=true");

        // Get all the extendedUserList where birthDate is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers equals to DEFAULT_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.equals=" + DEFAULT_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers equals to UPDATED_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.equals=" + UPDATED_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers in DEFAULT_TOTAL_FOLLOWERS or UPDATED_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.in=" + DEFAULT_TOTAL_FOLLOWERS + "," + UPDATED_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers equals to UPDATED_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.in=" + UPDATED_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.specified=true");

        // Get all the extendedUserList where totalFollowers is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers is greater than or equal to DEFAULT_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.greaterThanOrEqual=" + DEFAULT_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers is greater than or equal to UPDATED_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.greaterThanOrEqual=" + UPDATED_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers is less than or equal to DEFAULT_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.lessThanOrEqual=" + DEFAULT_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers is less than or equal to SMALLER_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.lessThanOrEqual=" + SMALLER_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers is less than DEFAULT_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.lessThan=" + DEFAULT_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers is less than UPDATED_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.lessThan=" + UPDATED_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowers is greater than DEFAULT_TOTAL_FOLLOWERS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowers.greaterThan=" + DEFAULT_TOTAL_FOLLOWERS);

        // Get all the extendedUserList where totalFollowers is greater than SMALLER_TOTAL_FOLLOWERS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowers.greaterThan=" + SMALLER_TOTAL_FOLLOWERS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing equals to DEFAULT_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.equals=" + DEFAULT_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing equals to UPDATED_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.equals=" + UPDATED_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing in DEFAULT_TOTAL_FOLLOWING or UPDATED_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.in=" + DEFAULT_TOTAL_FOLLOWING + "," + UPDATED_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing equals to UPDATED_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.in=" + UPDATED_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.specified=true");

        // Get all the extendedUserList where totalFollowing is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing is greater than or equal to DEFAULT_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.greaterThanOrEqual=" + DEFAULT_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing is greater than or equal to UPDATED_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.greaterThanOrEqual=" + UPDATED_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing is less than or equal to DEFAULT_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.lessThanOrEqual=" + DEFAULT_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing is less than or equal to SMALLER_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.lessThanOrEqual=" + SMALLER_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing is less than DEFAULT_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.lessThan=" + DEFAULT_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing is less than UPDATED_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.lessThan=" + UPDATED_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalFollowingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalFollowing is greater than DEFAULT_TOTAL_FOLLOWING
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalFollowing.greaterThan=" + DEFAULT_TOTAL_FOLLOWING);

        // Get all the extendedUserList where totalFollowing is greater than SMALLER_TOTAL_FOLLOWING
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalFollowing.greaterThan=" + SMALLER_TOTAL_FOLLOWING);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages equals to DEFAULT_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.equals=" + DEFAULT_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages equals to UPDATED_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.equals=" + UPDATED_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages in DEFAULT_TOTAL_IMAGES or UPDATED_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.in=" + DEFAULT_TOTAL_IMAGES + "," + UPDATED_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages equals to UPDATED_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.in=" + UPDATED_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.specified=true");

        // Get all the extendedUserList where totalImages is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages is greater than or equal to DEFAULT_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.greaterThanOrEqual=" + DEFAULT_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages is greater than or equal to UPDATED_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.greaterThanOrEqual=" + UPDATED_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages is less than or equal to DEFAULT_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.lessThanOrEqual=" + DEFAULT_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages is less than or equal to SMALLER_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("totalImages.lessThanOrEqual=" + SMALLER_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages is less than DEFAULT_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.lessThan=" + DEFAULT_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages is less than UPDATED_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.lessThan=" + UPDATED_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalImagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalImages is greater than DEFAULT_TOTAL_IMAGES
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalImages.greaterThan=" + DEFAULT_TOTAL_IMAGES);

        // Get all the extendedUserList where totalImages is greater than SMALLER_TOTAL_IMAGES
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalImages.greaterThan=" + SMALLER_TOTAL_IMAGES);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications equals to DEFAULT_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.equals=" + DEFAULT_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications equals to UPDATED_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.equals=" + UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications in DEFAULT_TOTAL_NOTIFICATIONS or UPDATED_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.in=" + DEFAULT_TOTAL_NOTIFICATIONS + "," + UPDATED_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications equals to UPDATED_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.in=" + UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications is not null
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.specified=true");

        // Get all the extendedUserList where totalNotifications is null
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications is greater than or equal to DEFAULT_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.greaterThanOrEqual=" + DEFAULT_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications is greater than or equal to UPDATED_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.greaterThanOrEqual=" + UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications is less than or equal to DEFAULT_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.lessThanOrEqual=" + DEFAULT_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications is less than or equal to SMALLER_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.lessThanOrEqual=" + SMALLER_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsLessThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications is less than DEFAULT_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.lessThan=" + DEFAULT_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications is less than UPDATED_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.lessThan=" + UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByTotalNotificationsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where totalNotifications is greater than DEFAULT_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldNotBeFound("id.greaterThanOrEqual=1000&totalNotifications.greaterThan=" + DEFAULT_TOTAL_NOTIFICATIONS);

        // Get all the extendedUserList where totalNotifications is greater than SMALLER_TOTAL_NOTIFICATIONS
        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=1000&totalNotifications.greaterThan=" + SMALLER_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = extendedUser.getUser();
        extendedUserRepository.saveAndFlush(extendedUser);
        Long userId = user.getId();

        // Get all the extendedUserList where user equals to userId
        defaultExtendedUserShouldBeFound("userId.equals=" + userId);

        // Get all the extendedUserList where user equals to (userId + 1)
        defaultExtendedUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExtendedUserShouldBeFound(String filter) throws Exception {
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalFollowers").value(hasItem(DEFAULT_TOTAL_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].totalFollowing").value(hasItem(DEFAULT_TOTAL_FOLLOWING)))
            .andExpect(jsonPath("$.[*].totalImages").value(hasItem(DEFAULT_TOTAL_IMAGES)))
            .andExpect(jsonPath("$.[*].totalNotifications").value(hasItem(DEFAULT_TOTAL_NOTIFICATIONS)));

        // Check, that the count call also returns 1
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExtendedUserShouldNotBeFound(String filter) throws Exception {
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExtendedUser() throws Exception {
        // Get the extendedUser
        restExtendedUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNonExistingExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extendedUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtendedUserWithPatch() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser using partial update
        ExtendedUser partialUpdatedExtendedUser = new ExtendedUser();
        partialUpdatedExtendedUser.setId(extendedUser.getId());

        partialUpdatedExtendedUser
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .totalFollowers(UPDATED_TOTAL_FOLLOWERS)
            .totalFollowing(UPDATED_TOTAL_FOLLOWING)
            .totalImages(UPDATED_TOTAL_IMAGES)
            .totalNotifications(UPDATED_TOTAL_NOTIFICATIONS);

        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtendedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtendedUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtendedUser.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExtendedUser.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testExtendedUser.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testExtendedUser.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testExtendedUser.getTotalFollowers()).isEqualTo(UPDATED_TOTAL_FOLLOWERS);
        assertThat(testExtendedUser.getTotalFollowing()).isEqualTo(UPDATED_TOTAL_FOLLOWING);
        assertThat(testExtendedUser.getTotalImages()).isEqualTo(UPDATED_TOTAL_IMAGES);
        assertThat(testExtendedUser.getTotalNotifications()).isEqualTo(UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void fullUpdateExtendedUserWithPatch() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser using partial update
        ExtendedUser partialUpdatedExtendedUser = new ExtendedUser();
        partialUpdatedExtendedUser.setId(extendedUser.getId());

        partialUpdatedExtendedUser
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .birthDate(UPDATED_BIRTH_DATE)
            .totalFollowers(UPDATED_TOTAL_FOLLOWERS)
            .totalFollowing(UPDATED_TOTAL_FOLLOWING)
            .totalImages(UPDATED_TOTAL_IMAGES)
            .totalNotifications(UPDATED_TOTAL_NOTIFICATIONS);

        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtendedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtendedUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtendedUser.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExtendedUser.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testExtendedUser.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testExtendedUser.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testExtendedUser.getTotalFollowers()).isEqualTo(UPDATED_TOTAL_FOLLOWERS);
        assertThat(testExtendedUser.getTotalFollowing()).isEqualTo(UPDATED_TOTAL_FOLLOWING);
        assertThat(testExtendedUser.getTotalImages()).isEqualTo(UPDATED_TOTAL_IMAGES);
        assertThat(testExtendedUser.getTotalNotifications()).isEqualTo(UPDATED_TOTAL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void patchNonExistingExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extendedUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();
        extendedUser.setId(count.incrementAndGet());

        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        int databaseSizeBeforeDelete = extendedUserRepository.findAll().size();

        // Delete the extendedUser
        restExtendedUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, extendedUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
