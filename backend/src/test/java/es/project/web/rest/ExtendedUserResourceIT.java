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

    private static final String DEFAULT_WEB = "AAAAAAAAAA";
    private static final String UPDATED_WEB = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

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
            .web(DEFAULT_WEB)
            .location(DEFAULT_LOCATION)
            .profession(DEFAULT_PROFESSION);
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
            .web(UPDATED_WEB)
            .location(UPDATED_LOCATION)
            .profession(UPDATED_PROFESSION);
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
    void createExtendedUser() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();
        // Create the ExtendedUser
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(extendedUser);
        restExtendedUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExtendedUser.getWeb()).isEqualTo(DEFAULT_WEB);
        assertThat(testExtendedUser.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testExtendedUser.getProfession()).isEqualTo(DEFAULT_PROFESSION);

        // Validate the id for MapsId, the ids must be same
        assertThat(testExtendedUser.getId()).isEqualTo(extendedUserDTO.getUser().getId());
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
    void updateExtendedUserMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();
        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).get();
        assertThat(updatedExtendedUser).isNotNull();
        // Disconnect from session so that the updates on updatedExtendedUser are not directly saved in db
        em.detach(updatedExtendedUser);

        // Update the User with new association value
        updatedExtendedUser.setUser(user);
        ExtendedUserDTO updatedExtendedUserDTO = extendedUserMapper.toDto(updatedExtendedUser);
        assertThat(updatedExtendedUserDTO).isNotNull();

        // Update the entity
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtendedUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExtendedUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testExtendedUser.getId()).isEqualTo(testExtendedUser.getUser().getId());
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
            .andExpect(jsonPath("$.[*].web").value(hasItem(DEFAULT_WEB)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)));
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
            .andExpect(jsonPath("$.web").value(DEFAULT_WEB))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION));
    }

    @Test
    @Transactional
    void getExtendedUsersByIdFiltering() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        Long id = extendedUser.getId();

        defaultExtendedUserShouldBeFound("id.equals=" + id);
        defaultExtendedUserShouldNotBeFound("id.notEquals=" + id);

        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.greaterThan=" + id);

        defaultExtendedUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.lessThan=" + id);
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
        defaultExtendedUserShouldBeFound("description.specified=true");

        // Get all the extendedUserList where description is null
        defaultExtendedUserShouldNotBeFound("description.specified=false");
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
        defaultExtendedUserShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the extendedUserList where description does not contain UPDATED_DESCRIPTION
        defaultExtendedUserShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWebIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where web equals to DEFAULT_WEB
        defaultExtendedUserShouldBeFound("web.equals=" + DEFAULT_WEB);

        // Get all the extendedUserList where web equals to UPDATED_WEB
        defaultExtendedUserShouldNotBeFound("web.equals=" + UPDATED_WEB);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWebIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where web in DEFAULT_WEB or UPDATED_WEB
        defaultExtendedUserShouldBeFound("web.in=" + DEFAULT_WEB + "," + UPDATED_WEB);

        // Get all the extendedUserList where web equals to UPDATED_WEB
        defaultExtendedUserShouldNotBeFound("web.in=" + UPDATED_WEB);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWebIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where web is not null
        defaultExtendedUserShouldBeFound("web.specified=true");

        // Get all the extendedUserList where web is null
        defaultExtendedUserShouldNotBeFound("web.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWebContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where web contains DEFAULT_WEB
        defaultExtendedUserShouldBeFound("web.contains=" + DEFAULT_WEB);

        // Get all the extendedUserList where web contains UPDATED_WEB
        defaultExtendedUserShouldNotBeFound("web.contains=" + UPDATED_WEB);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByWebNotContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where web does not contain DEFAULT_WEB
        defaultExtendedUserShouldNotBeFound("web.doesNotContain=" + DEFAULT_WEB);

        // Get all the extendedUserList where web does not contain UPDATED_WEB
        defaultExtendedUserShouldBeFound("web.doesNotContain=" + UPDATED_WEB);
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
        defaultExtendedUserShouldBeFound("location.specified=true");

        // Get all the extendedUserList where location is null
        defaultExtendedUserShouldNotBeFound("location.specified=false");
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
        defaultExtendedUserShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the extendedUserList where location does not contain UPDATED_LOCATION
        defaultExtendedUserShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where profession equals to DEFAULT_PROFESSION
        defaultExtendedUserShouldBeFound("profession.equals=" + DEFAULT_PROFESSION);

        // Get all the extendedUserList where profession equals to UPDATED_PROFESSION
        defaultExtendedUserShouldNotBeFound("profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where profession in DEFAULT_PROFESSION or UPDATED_PROFESSION
        defaultExtendedUserShouldBeFound("profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION);

        // Get all the extendedUserList where profession equals to UPDATED_PROFESSION
        defaultExtendedUserShouldNotBeFound("profession.in=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where profession is not null
        defaultExtendedUserShouldBeFound("profession.specified=true");

        // Get all the extendedUserList where profession is null
        defaultExtendedUserShouldNotBeFound("profession.specified=false");
    }

    @Test
    @Transactional
    void getAllExtendedUsersByProfessionContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where profession contains DEFAULT_PROFESSION
        defaultExtendedUserShouldBeFound("profession.contains=" + DEFAULT_PROFESSION);

        // Get all the extendedUserList where profession contains UPDATED_PROFESSION
        defaultExtendedUserShouldNotBeFound("profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllExtendedUsersByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where profession does not contain DEFAULT_PROFESSION
        defaultExtendedUserShouldNotBeFound("profession.doesNotContain=" + DEFAULT_PROFESSION);

        // Get all the extendedUserList where profession does not contain UPDATED_PROFESSION
        defaultExtendedUserShouldBeFound("profession.doesNotContain=" + UPDATED_PROFESSION);
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
            .andExpect(jsonPath("$.[*].web").value(hasItem(DEFAULT_WEB)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)));

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
    void putExistingExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).get();
        // Disconnect from session so that the updates on updatedExtendedUser are not directly saved in db
        em.detach(updatedExtendedUser);
        updatedExtendedUser.description(UPDATED_DESCRIPTION).web(UPDATED_WEB).location(UPDATED_LOCATION).profession(UPDATED_PROFESSION);
        ExtendedUserDTO extendedUserDTO = extendedUserMapper.toDto(updatedExtendedUser);

        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extendedUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extendedUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtendedUser.getWeb()).isEqualTo(UPDATED_WEB);
        assertThat(testExtendedUser.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExtendedUser.getProfession()).isEqualTo(UPDATED_PROFESSION);
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

        partialUpdatedExtendedUser.description(UPDATED_DESCRIPTION).web(UPDATED_WEB);

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
        assertThat(testExtendedUser.getWeb()).isEqualTo(UPDATED_WEB);
        assertThat(testExtendedUser.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testExtendedUser.getProfession()).isEqualTo(DEFAULT_PROFESSION);
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
            .web(UPDATED_WEB)
            .location(UPDATED_LOCATION)
            .profession(UPDATED_PROFESSION);

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
        assertThat(testExtendedUser.getWeb()).isEqualTo(UPDATED_WEB);
        assertThat(testExtendedUser.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExtendedUser.getProfession()).isEqualTo(UPDATED_PROFESSION);
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