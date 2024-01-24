package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.repository.CommentaryRepository;
import es.project.service.criteria.CommentaryCriteria;
import es.project.service.dto.CommentaryDTO;
import es.project.service.mapper.CommentaryMapper;
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
 * Integration tests for the {@link CommentaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentaryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commentaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private CommentaryMapper commentaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentaryMockMvc;

    private Commentary commentary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentary createEntity(EntityManager em) {
        Commentary commentary = new Commentary().description(DEFAULT_DESCRIPTION).creationDate(DEFAULT_CREATION_DATE);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        commentary.setExtendedUser(extendedUser);
        return commentary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentary createUpdatedEntity(EntityManager em) {
        Commentary commentary = new Commentary().description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createUpdatedEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        commentary.setExtendedUser(extendedUser);
        return commentary;
    }

    @BeforeEach
    public void initTest() {
        commentary = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentary() throws Exception {
        int databaseSizeBeforeCreate = commentaryRepository.findAll().size();
        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);
        restCommentaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeCreate + 1);
        Commentary testCommentary = commentaryList.get(commentaryList.size() - 1);
        assertThat(testCommentary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommentary.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createCommentaryWithExistingId() throws Exception {
        // Create the Commentary with an existing ID
        commentary.setId(1L);
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        int databaseSizeBeforeCreate = commentaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentaryRepository.findAll().size();
        // set the field null
        commentary.setDescription(null);

        // Create the Commentary, which fails.
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        restCommentaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
            .andExpect(status().isBadRequest());

        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentaryRepository.findAll().size();
        // set the field null
        commentary.setCreationDate(null);

        // Create the Commentary, which fails.
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        restCommentaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
            .andExpect(status().isBadRequest());

        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommentaries() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentary.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get the commentary
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL_ID, commentary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentary.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getCommentariesByIdFiltering() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        Long id = commentary.getId();

        defaultCommentaryShouldBeFound("id.equals=" + id);
        defaultCommentaryShouldNotBeFound("id.notEquals=" + id);

        defaultCommentaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentaryShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentariesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where description equals to DEFAULT_DESCRIPTION
        defaultCommentaryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the commentaryList where description equals to UPDATED_DESCRIPTION
        defaultCommentaryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentariesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCommentaryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the commentaryList where description equals to UPDATED_DESCRIPTION
        defaultCommentaryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentariesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where description is not null
        defaultCommentaryShouldBeFound("description.specified=true");

        // Get all the commentaryList where description is null
        defaultCommentaryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentariesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where description contains DEFAULT_DESCRIPTION
        defaultCommentaryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the commentaryList where description contains UPDATED_DESCRIPTION
        defaultCommentaryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentariesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where description does not contain DEFAULT_DESCRIPTION
        defaultCommentaryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the commentaryList where description does not contain UPDATED_DESCRIPTION
        defaultCommentaryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentariesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where creationDate equals to DEFAULT_CREATION_DATE
        defaultCommentaryShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the commentaryList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommentaryShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommentariesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultCommentaryShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the commentaryList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommentaryShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommentariesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        // Get all the commentaryList where creationDate is not null
        defaultCommentaryShouldBeFound("creationDate.specified=true");

        // Get all the commentaryList where creationDate is null
        defaultCommentaryShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentariesByExtendedUserIsEqualToSomething() throws Exception {
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            commentaryRepository.saveAndFlush(commentary);
            extendedUser = ExtendedUserResourceIT.createEntity(em);
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(extendedUser);
        em.flush();
        commentary.setExtendedUser(extendedUser);
        commentaryRepository.saveAndFlush(commentary);
        Long extendedUserId = extendedUser.getId();

        // Get all the commentaryList where extendedUser equals to extendedUserId
        defaultCommentaryShouldBeFound("extendedUserId.equals=" + extendedUserId);

        // Get all the commentaryList where extendedUser equals to (extendedUserId + 1)
        defaultCommentaryShouldNotBeFound("extendedUserId.equals=" + (extendedUserId + 1));
    }

    @Test
    @Transactional
    void getAllCommentariesByImageIsEqualToSomething() throws Exception {
        Image image;
        if (TestUtil.findAll(em, Image.class).isEmpty()) {
            commentaryRepository.saveAndFlush(commentary);
            image = ImageResourceIT.createEntity(em);
        } else {
            image = TestUtil.findAll(em, Image.class).get(0);
        }
        em.persist(image);
        em.flush();
        commentary.setImage(image);
        commentaryRepository.saveAndFlush(commentary);
        Long imageId = image.getId();

        // Get all the commentaryList where image equals to imageId
        defaultCommentaryShouldBeFound("imageId.equals=" + imageId);

        // Get all the commentaryList where image equals to (imageId + 1)
        defaultCommentaryShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentaryShouldBeFound(String filter) throws Exception {
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentary.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentaryShouldNotBeFound(String filter) throws Exception {
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommentary() throws Exception {
        // Get the commentary
        restCommentaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();

        // Update the commentary
        Commentary updatedCommentary = commentaryRepository.findById(commentary.getId()).get();
        // Disconnect from session so that the updates on updatedCommentary are not directly saved in db
        em.detach(updatedCommentary);
        updatedCommentary.description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(updatedCommentary);

        restCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
        Commentary testCommentary = commentaryList.get(commentaryList.size() - 1);
        assertThat(testCommentary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentaryWithPatch() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();

        // Update the commentary using partial update
        Commentary partialUpdatedCommentary = new Commentary();
        partialUpdatedCommentary.setId(commentary.getId());

        partialUpdatedCommentary.description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);

        restCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentary))
            )
            .andExpect(status().isOk());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
        Commentary testCommentary = commentaryList.get(commentaryList.size() - 1);
        assertThat(testCommentary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCommentaryWithPatch() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();

        // Update the commentary using partial update
        Commentary partialUpdatedCommentary = new Commentary();
        partialUpdatedCommentary.setId(commentary.getId());

        partialUpdatedCommentary.description(UPDATED_DESCRIPTION).creationDate(UPDATED_CREATION_DATE);

        restCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentary))
            )
            .andExpect(status().isOk());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
        Commentary testCommentary = commentaryList.get(commentaryList.size() - 1);
        assertThat(testCommentary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommentary.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentary() throws Exception {
        int databaseSizeBeforeUpdate = commentaryRepository.findAll().size();
        commentary.setId(count.incrementAndGet());

        // Create the Commentary
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commentaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentary in the database
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentary() throws Exception {
        // Initialize the database
        commentaryRepository.saveAndFlush(commentary);

        int databaseSizeBeforeDelete = commentaryRepository.findAll().size();

        // Delete the commentary
        restCommentaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commentary> commentaryList = commentaryRepository.findAll();
        assertThat(commentaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
