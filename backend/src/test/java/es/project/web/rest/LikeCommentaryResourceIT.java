package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.LikeCommentary;
import es.project.repository.LikeCommentaryRepository;
import es.project.service.criteria.LikeCommentaryCriteria;
import es.project.service.dto.LikeCommentaryDTO;
import es.project.service.mapper.LikeCommentaryMapper;
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
 * Integration tests for the {@link LikeCommentaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeCommentaryResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/like-commentaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeCommentaryRepository likeCommentaryRepository;

    @Autowired
    private LikeCommentaryMapper likeCommentaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeCommentaryMockMvc;

    private LikeCommentary likeCommentary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeCommentary createEntity(EntityManager em) {
        LikeCommentary likeCommentary = new LikeCommentary().creationDate(DEFAULT_CREATION_DATE);
        // Add required entity
        Commentary commentary;
        if (TestUtil.findAll(em, Commentary.class).isEmpty()) {
            commentary = CommentaryResourceIT.createEntity(em);
            em.persist(commentary);
            em.flush();
        } else {
            commentary = TestUtil.findAll(em, Commentary.class).get(0);
        }
        likeCommentary.getCommentaries().add(commentary);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        likeCommentary.getExtendedUsers().add(extendedUser);
        return likeCommentary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeCommentary createUpdatedEntity(EntityManager em) {
        LikeCommentary likeCommentary = new LikeCommentary().creationDate(UPDATED_CREATION_DATE);
        // Add required entity
        Commentary commentary;
        if (TestUtil.findAll(em, Commentary.class).isEmpty()) {
            commentary = CommentaryResourceIT.createUpdatedEntity(em);
            em.persist(commentary);
            em.flush();
        } else {
            commentary = TestUtil.findAll(em, Commentary.class).get(0);
        }
        likeCommentary.getCommentaries().add(commentary);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createUpdatedEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        likeCommentary.getExtendedUsers().add(extendedUser);
        return likeCommentary;
    }

    @BeforeEach
    public void initTest() {
        likeCommentary = createEntity(em);
    }

    @Test
    @Transactional
    void createLikeCommentary() throws Exception {
        int databaseSizeBeforeCreate = likeCommentaryRepository.findAll().size();
        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);
        restLikeCommentaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeCreate + 1);
        LikeCommentary testLikeCommentary = likeCommentaryList.get(likeCommentaryList.size() - 1);
        assertThat(testLikeCommentary.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createLikeCommentaryWithExistingId() throws Exception {
        // Create the LikeCommentary with an existing ID
        likeCommentary.setId(1L);
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        int databaseSizeBeforeCreate = likeCommentaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeCommentaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeCommentaryRepository.findAll().size();
        // set the field null
        likeCommentary.setCreationDate(null);

        // Create the LikeCommentary, which fails.
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        restLikeCommentaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikeCommentaries() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        // Get all the likeCommentaryList
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeCommentary.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getLikeCommentary() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        // Get the likeCommentary
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL_ID, likeCommentary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeCommentary.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getLikeCommentariesByIdFiltering() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        Long id = likeCommentary.getId();

        defaultLikeCommentaryShouldBeFound("id.equals=" + id);
        defaultLikeCommentaryShouldNotBeFound("id.notEquals=" + id);

        defaultLikeCommentaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLikeCommentaryShouldNotBeFound("id.greaterThan=" + id);

        defaultLikeCommentaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLikeCommentaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLikeCommentariesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        // Get all the likeCommentaryList where creationDate equals to DEFAULT_CREATION_DATE
        defaultLikeCommentaryShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the likeCommentaryList where creationDate equals to UPDATED_CREATION_DATE
        defaultLikeCommentaryShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllLikeCommentariesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        // Get all the likeCommentaryList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultLikeCommentaryShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the likeCommentaryList where creationDate equals to UPDATED_CREATION_DATE
        defaultLikeCommentaryShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllLikeCommentariesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        // Get all the likeCommentaryList where creationDate is not null
        defaultLikeCommentaryShouldBeFound("creationDate.specified=true");

        // Get all the likeCommentaryList where creationDate is null
        defaultLikeCommentaryShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLikeCommentariesByCommentaryIsEqualToSomething() throws Exception {
        Commentary commentary;
        if (TestUtil.findAll(em, Commentary.class).isEmpty()) {
            likeCommentaryRepository.saveAndFlush(likeCommentary);
            commentary = CommentaryResourceIT.createEntity(em);
        } else {
            commentary = TestUtil.findAll(em, Commentary.class).get(0);
        }
        em.persist(commentary);
        em.flush();
        likeCommentary.addCommentary(commentary);
        likeCommentaryRepository.saveAndFlush(likeCommentary);
        Long commentaryId = commentary.getId();

        // Get all the likeCommentaryList where commentary equals to commentaryId
        defaultLikeCommentaryShouldBeFound("commentaryId.equals=" + commentaryId);

        // Get all the likeCommentaryList where commentary equals to (commentaryId + 1)
        defaultLikeCommentaryShouldNotBeFound("commentaryId.equals=" + (commentaryId + 1));
    }

    @Test
    @Transactional
    void getAllLikeCommentariesByExtendedUserIsEqualToSomething() throws Exception {
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            likeCommentaryRepository.saveAndFlush(likeCommentary);
            extendedUser = ExtendedUserResourceIT.createEntity(em);
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(extendedUser);
        em.flush();
        likeCommentary.addExtendedUser(extendedUser);
        likeCommentaryRepository.saveAndFlush(likeCommentary);
        Long extendedUserId = extendedUser.getId();

        // Get all the likeCommentaryList where extendedUser equals to extendedUserId
        defaultLikeCommentaryShouldBeFound("extendedUserId.equals=" + extendedUserId);

        // Get all the likeCommentaryList where extendedUser equals to (extendedUserId + 1)
        defaultLikeCommentaryShouldNotBeFound("extendedUserId.equals=" + (extendedUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLikeCommentaryShouldBeFound(String filter) throws Exception {
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeCommentary.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLikeCommentaryShouldNotBeFound(String filter) throws Exception {
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLikeCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLikeCommentary() throws Exception {
        // Get the likeCommentary
        restLikeCommentaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikeCommentary() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();

        // Update the likeCommentary
        LikeCommentary updatedLikeCommentary = likeCommentaryRepository.findById(likeCommentary.getId()).get();
        // Disconnect from session so that the updates on updatedLikeCommentary are not directly saved in db
        em.detach(updatedLikeCommentary);
        updatedLikeCommentary.creationDate(UPDATED_CREATION_DATE);
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(updatedLikeCommentary);

        restLikeCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeCommentaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
        LikeCommentary testLikeCommentary = likeCommentaryList.get(likeCommentaryList.size() - 1);
        assertThat(testLikeCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeCommentaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeCommentaryWithPatch() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();

        // Update the likeCommentary using partial update
        LikeCommentary partialUpdatedLikeCommentary = new LikeCommentary();
        partialUpdatedLikeCommentary.setId(likeCommentary.getId());

        partialUpdatedLikeCommentary.creationDate(UPDATED_CREATION_DATE);

        restLikeCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeCommentary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeCommentary))
            )
            .andExpect(status().isOk());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
        LikeCommentary testLikeCommentary = likeCommentaryList.get(likeCommentaryList.size() - 1);
        assertThat(testLikeCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLikeCommentaryWithPatch() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();

        // Update the likeCommentary using partial update
        LikeCommentary partialUpdatedLikeCommentary = new LikeCommentary();
        partialUpdatedLikeCommentary.setId(likeCommentary.getId());

        partialUpdatedLikeCommentary.creationDate(UPDATED_CREATION_DATE);

        restLikeCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeCommentary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeCommentary))
            )
            .andExpect(status().isOk());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
        LikeCommentary testLikeCommentary = likeCommentaryList.get(likeCommentaryList.size() - 1);
        assertThat(testLikeCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeCommentaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeCommentary() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentaryRepository.findAll().size();
        likeCommentary.setId(count.incrementAndGet());

        // Create the LikeCommentary
        LikeCommentaryDTO likeCommentaryDTO = likeCommentaryMapper.toDto(likeCommentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeCommentary in the database
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeCommentary() throws Exception {
        // Initialize the database
        likeCommentaryRepository.saveAndFlush(likeCommentary);

        int databaseSizeBeforeDelete = likeCommentaryRepository.findAll().size();

        // Delete the likeCommentary
        restLikeCommentaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeCommentary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikeCommentary> likeCommentaryList = likeCommentaryRepository.findAll();
        assertThat(likeCommentaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
