package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.LikeImage;
import es.project.repository.LikeImageRepository;
import es.project.service.criteria.LikeImageCriteria;
import es.project.service.dto.LikeImageDTO;
import es.project.service.mapper.LikeImageMapper;
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
 * Integration tests for the {@link LikeImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeImageResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/like-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeImageRepository likeImageRepository;

    @Autowired
    private LikeImageMapper likeImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeImageMockMvc;

    private LikeImage likeImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeImage createEntity(EntityManager em) {
        LikeImage likeImage = new LikeImage().creationDate(DEFAULT_CREATION_DATE);
        // Add required entity
        Image image;
        if (TestUtil.findAll(em, Image.class).isEmpty()) {
            image = ImageResourceIT.createEntity(em);
            em.persist(image);
            em.flush();
        } else {
            image = TestUtil.findAll(em, Image.class).get(0);
        }
        likeImage.setImage(image);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        likeImage.setExtendedUser(extendedUser);
        return likeImage;
    }

    @BeforeEach
    public void initTest() {
        likeImage = createEntity(em);
    }

    @Test
    @Transactional
    void createLikeImage() throws Exception {
        int databaseSizeBeforeCreate = likeImageRepository.findAll().size();
        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);
        restLikeImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeImageDTO)))
            .andExpect(status().isCreated());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeCreate + 1);
        LikeImage testLikeImage = likeImageList.get(likeImageList.size() - 1);
        assertThat(testLikeImage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createLikeImageWithExistingId() throws Exception {
        // Create the LikeImage with an existing ID
        likeImage.setId(1L);
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        int databaseSizeBeforeCreate = likeImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeImageRepository.findAll().size();
        // set the field null
        likeImage.setCreationDate(null);

        // Create the LikeImage, which fails.
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        restLikeImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeImageDTO)))
            .andExpect(status().isBadRequest());

        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikeImages() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        // Get all the likeImageList
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getLikeImage() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        // Get the likeImage
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL_ID, likeImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeImage.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getLikeImagesByIdFiltering() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        Long id = likeImage.getId();

        defaultLikeImageShouldBeFound("id.equals=" + id);
        defaultLikeImageShouldNotBeFound("id.notEquals=" + id);

        defaultLikeImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLikeImageShouldNotBeFound("id.greaterThan=" + id);

        defaultLikeImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLikeImageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLikeImagesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        // Get all the likeImageList where creationDate equals to DEFAULT_CREATION_DATE
        defaultLikeImageShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the likeImageList where creationDate equals to UPDATED_CREATION_DATE
        defaultLikeImageShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllLikeImagesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        // Get all the likeImageList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultLikeImageShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the likeImageList where creationDate equals to UPDATED_CREATION_DATE
        defaultLikeImageShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllLikeImagesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        // Get all the likeImageList where creationDate is not null
        defaultLikeImageShouldBeFound("creationDate.specified=true");

        // Get all the likeImageList where creationDate is null
        defaultLikeImageShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLikeImagesByImageIsEqualToSomething() throws Exception {
        Image image;
        if (TestUtil.findAll(em, Image.class).isEmpty()) {
            likeImageRepository.saveAndFlush(likeImage);
            image = ImageResourceIT.createEntity(em);
        } else {
            image = TestUtil.findAll(em, Image.class).get(0);
        }
        em.persist(image);
        em.flush();
        likeImage.setImage(image);
        likeImageRepository.saveAndFlush(likeImage);
        Long imageId = image.getId();

        // Get all the likeImageList where image equals to imageId
        defaultLikeImageShouldBeFound("imageId.equals=" + imageId);

        // Get all the likeImageList where image equals to (imageId + 1)
        defaultLikeImageShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllLikeImagesByExtendedUserIsEqualToSomething() throws Exception {
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            likeImageRepository.saveAndFlush(likeImage);
            extendedUser = ExtendedUserResourceIT.createEntity(em);
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(extendedUser);
        em.flush();
        likeImage.setExtendedUser(extendedUser);
        likeImageRepository.saveAndFlush(likeImage);
        Long extendedUserId = extendedUser.getId();

        // Get all the likeImageList where extendedUser equals to extendedUserId
        defaultLikeImageShouldBeFound("extendedUserId.equals=" + extendedUserId);

        // Get all the likeImageList where extendedUser equals to (extendedUserId + 1)
        defaultLikeImageShouldNotBeFound("extendedUserId.equals=" + (extendedUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLikeImageShouldBeFound(String filter) throws Exception {
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLikeImageShouldNotBeFound(String filter) throws Exception {
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLikeImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLikeImage() throws Exception {
        // Get the likeImage
        restLikeImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikeImage() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();

        // Update the likeImage
        LikeImage updatedLikeImage = likeImageRepository.findById(likeImage.getId()).get();
        // Disconnect from session so that the updates on updatedLikeImage are not directly saved in db
        em.detach(updatedLikeImage);
        updatedLikeImage.creationDate(UPDATED_CREATION_DATE);
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(updatedLikeImage);

        restLikeImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
        LikeImage testLikeImage = likeImageList.get(likeImageList.size() - 1);
        assertThat(testLikeImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeImageWithPatch() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();

        // Update the likeImage using partial update
        LikeImage partialUpdatedLikeImage = new LikeImage();
        partialUpdatedLikeImage.setId(likeImage.getId());

        partialUpdatedLikeImage.creationDate(UPDATED_CREATION_DATE);

        restLikeImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeImage))
            )
            .andExpect(status().isOk());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
        LikeImage testLikeImage = likeImageList.get(likeImageList.size() - 1);
        assertThat(testLikeImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLikeImageWithPatch() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();

        // Update the likeImage using partial update
        LikeImage partialUpdatedLikeImage = new LikeImage();
        partialUpdatedLikeImage.setId(likeImage.getId());

        partialUpdatedLikeImage.creationDate(UPDATED_CREATION_DATE);

        restLikeImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeImage))
            )
            .andExpect(status().isOk());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
        LikeImage testLikeImage = likeImageList.get(likeImageList.size() - 1);
        assertThat(testLikeImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeImage() throws Exception {
        int databaseSizeBeforeUpdate = likeImageRepository.findAll().size();
        likeImage.setId(count.incrementAndGet());

        // Create the LikeImage
        LikeImageDTO likeImageDTO = likeImageMapper.toDto(likeImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(likeImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeImage in the database
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeImage() throws Exception {
        // Initialize the database
        likeImageRepository.saveAndFlush(likeImage);

        int databaseSizeBeforeDelete = likeImageRepository.findAll().size();

        // Delete the likeImage
        restLikeImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikeImage> likeImageList = likeImageRepository.findAll();
        assertThat(likeImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
