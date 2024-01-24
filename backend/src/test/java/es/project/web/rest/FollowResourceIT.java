package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.ExtendedUser;
import es.project.domain.Follow;
import es.project.domain.enumeration.FollowState;
import es.project.repository.FollowRepository;
import es.project.service.criteria.FollowCriteria;
import es.project.service.dto.FollowDTO;
import es.project.service.mapper.FollowMapper;
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
 * Integration tests for the {@link FollowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FollowResourceIT {

    private static final FollowState DEFAULT_STATE = FollowState.PENDING;
    private static final FollowState UPDATED_STATE = FollowState.ACCEPTED;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACCEPTANCE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTANCE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/follows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFollowMockMvc;

    private Follow follow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createEntity(EntityManager em) {
        Follow follow = new Follow().state(DEFAULT_STATE).creationDate(DEFAULT_CREATION_DATE).acceptanceDate(DEFAULT_ACCEPTANCE_DATE);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        follow.setFollower(extendedUser);
        // Add required entity
        follow.setFollowing(extendedUser);
        return follow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createUpdatedEntity(EntityManager em) {
        Follow follow = new Follow().state(UPDATED_STATE).creationDate(UPDATED_CREATION_DATE).acceptanceDate(UPDATED_ACCEPTANCE_DATE);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createUpdatedEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        follow.setFollower(extendedUser);
        // Add required entity
        follow.setFollowing(extendedUser);
        return follow;
    }

    @BeforeEach
    public void initTest() {
        follow = createEntity(em);
    }

    @Test
    @Transactional
    void createFollow() throws Exception {
        int databaseSizeBeforeCreate = followRepository.findAll().size();
        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);
        restFollowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(followDTO)))
            .andExpect(status().isCreated());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate + 1);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFollow.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFollow.getAcceptanceDate()).isEqualTo(DEFAULT_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void createFollowWithExistingId() throws Exception {
        // Create the Follow with an existing ID
        follow.setId(1L);
        FollowDTO followDTO = followMapper.toDto(follow);

        int databaseSizeBeforeCreate = followRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(followDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = followRepository.findAll().size();
        // set the field null
        follow.setState(null);

        // Create the Follow, which fails.
        FollowDTO followDTO = followMapper.toDto(follow);

        restFollowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(followDTO)))
            .andExpect(status().isBadRequest());

        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = followRepository.findAll().size();
        // set the field null
        follow.setCreationDate(null);

        // Create the Follow, which fails.
        FollowDTO followDTO = followMapper.toDto(follow);

        restFollowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(followDTO)))
            .andExpect(status().isBadRequest());

        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFollows() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList
        restFollowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(follow.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].acceptanceDate").value(hasItem(DEFAULT_ACCEPTANCE_DATE.toString())));
    }

    @Test
    @Transactional
    void getFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get the follow
        restFollowMockMvc
            .perform(get(ENTITY_API_URL_ID, follow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(follow.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.acceptanceDate").value(DEFAULT_ACCEPTANCE_DATE.toString()));
    }

    @Test
    @Transactional
    void getFollowsByIdFiltering() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        Long id = follow.getId();

        defaultFollowShouldBeFound("id.equals=" + id);
        defaultFollowShouldNotBeFound("id.notEquals=" + id);

        defaultFollowShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFollowShouldNotBeFound("id.greaterThan=" + id);

        defaultFollowShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFollowShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFollowsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where state equals to DEFAULT_STATE
        defaultFollowShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the followList where state equals to UPDATED_STATE
        defaultFollowShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFollowsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where state in DEFAULT_STATE or UPDATED_STATE
        defaultFollowShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the followList where state equals to UPDATED_STATE
        defaultFollowShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFollowsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where state is not null
        defaultFollowShouldBeFound("state.specified=true");

        // Get all the followList where state is null
        defaultFollowShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllFollowsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where creationDate equals to DEFAULT_CREATION_DATE
        defaultFollowShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the followList where creationDate equals to UPDATED_CREATION_DATE
        defaultFollowShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFollowsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultFollowShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the followList where creationDate equals to UPDATED_CREATION_DATE
        defaultFollowShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFollowsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where creationDate is not null
        defaultFollowShouldBeFound("creationDate.specified=true");

        // Get all the followList where creationDate is null
        defaultFollowShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFollowsByAcceptanceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where acceptanceDate equals to DEFAULT_ACCEPTANCE_DATE
        defaultFollowShouldBeFound("acceptanceDate.equals=" + DEFAULT_ACCEPTANCE_DATE);

        // Get all the followList where acceptanceDate equals to UPDATED_ACCEPTANCE_DATE
        defaultFollowShouldNotBeFound("acceptanceDate.equals=" + UPDATED_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void getAllFollowsByAcceptanceDateIsInShouldWork() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where acceptanceDate in DEFAULT_ACCEPTANCE_DATE or UPDATED_ACCEPTANCE_DATE
        defaultFollowShouldBeFound("acceptanceDate.in=" + DEFAULT_ACCEPTANCE_DATE + "," + UPDATED_ACCEPTANCE_DATE);

        // Get all the followList where acceptanceDate equals to UPDATED_ACCEPTANCE_DATE
        defaultFollowShouldNotBeFound("acceptanceDate.in=" + UPDATED_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void getAllFollowsByAcceptanceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where acceptanceDate is not null
        defaultFollowShouldBeFound("acceptanceDate.specified=true");

        // Get all the followList where acceptanceDate is null
        defaultFollowShouldNotBeFound("acceptanceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFollowsByFollowerIsEqualToSomething() throws Exception {
        ExtendedUser follower;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            followRepository.saveAndFlush(follow);
            follower = ExtendedUserResourceIT.createEntity(em);
        } else {
            follower = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(follower);
        em.flush();
        follow.setFollower(follower);
        followRepository.saveAndFlush(follow);
        Long followerId = follower.getId();

        // Get all the followList where follower equals to followerId
        defaultFollowShouldBeFound("followerId.equals=" + followerId);

        // Get all the followList where follower equals to (followerId + 1)
        defaultFollowShouldNotBeFound("followerId.equals=" + (followerId + 1));
    }

    @Test
    @Transactional
    void getAllFollowsByFollowingIsEqualToSomething() throws Exception {
        ExtendedUser following;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            followRepository.saveAndFlush(follow);
            following = ExtendedUserResourceIT.createEntity(em);
        } else {
            following = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(following);
        em.flush();
        follow.setFollowing(following);
        followRepository.saveAndFlush(follow);
        Long followingId = following.getId();

        // Get all the followList where following equals to followingId
        defaultFollowShouldBeFound("followingId.equals=" + followingId);

        // Get all the followList where following equals to (followingId + 1)
        defaultFollowShouldNotBeFound("followingId.equals=" + (followingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFollowShouldBeFound(String filter) throws Exception {
        restFollowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(follow.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].acceptanceDate").value(hasItem(DEFAULT_ACCEPTANCE_DATE.toString())));

        // Check, that the count call also returns 1
        restFollowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFollowShouldNotBeFound(String filter) throws Exception {
        restFollowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFollowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFollow() throws Exception {
        // Get the follow
        restFollowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // Update the follow
        Follow updatedFollow = followRepository.findById(follow.getId()).get();
        // Disconnect from session so that the updates on updatedFollow are not directly saved in db
        em.detach(updatedFollow);
        updatedFollow.state(UPDATED_STATE).creationDate(UPDATED_CREATION_DATE).acceptanceDate(UPDATED_ACCEPTANCE_DATE);
        FollowDTO followDTO = followMapper.toDto(updatedFollow);

        restFollowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, followDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isOk());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFollow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFollow.getAcceptanceDate()).isEqualTo(UPDATED_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, followDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(followDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFollowWithPatch() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // Update the follow using partial update
        Follow partialUpdatedFollow = new Follow();
        partialUpdatedFollow.setId(follow.getId());

        partialUpdatedFollow.creationDate(UPDATED_CREATION_DATE);

        restFollowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFollow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFollow))
            )
            .andExpect(status().isOk());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFollow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFollow.getAcceptanceDate()).isEqualTo(DEFAULT_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFollowWithPatch() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // Update the follow using partial update
        Follow partialUpdatedFollow = new Follow();
        partialUpdatedFollow.setId(follow.getId());

        partialUpdatedFollow.state(UPDATED_STATE).creationDate(UPDATED_CREATION_DATE).acceptanceDate(UPDATED_ACCEPTANCE_DATE);

        restFollowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFollow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFollow))
            )
            .andExpect(status().isOk());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFollow.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFollow.getAcceptanceDate()).isEqualTo(UPDATED_ACCEPTANCE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, followDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();
        follow.setId(count.incrementAndGet());

        // Create the Follow
        FollowDTO followDTO = followMapper.toDto(follow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFollowMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(followDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        int databaseSizeBeforeDelete = followRepository.findAll().size();

        // Delete the follow
        restFollowMockMvc
            .perform(delete(ENTITY_API_URL_ID, follow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
