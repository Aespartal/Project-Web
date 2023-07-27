package es.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.Commentary;
import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.repository.ImageRepository;
import es.project.service.criteria.ImageCriteria;
import es.project.service.dto.ImageDTO;
import es.project.service.mapper.ImageMapper;
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
 * Integration tests for the {@link ImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    private static final Integer DEFAULT_TOTAL_LIKES = 0;
    private static final Integer UPDATED_TOTAL_LIKES = 1;
    private static final Integer SMALLER_TOTAL_LIKES = 0 - 1;

    private static final Integer DEFAULT_TOTAL_COMMENTARIES = 0;
    private static final Integer UPDATED_TOTAL_COMMENTARIES = 1;
    private static final Integer SMALLER_TOTAL_COMMENTARIES = 0 - 1;

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity(EntityManager em) {
        Image image = new Image()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .fileName(DEFAULT_FILE_NAME)
            .path(DEFAULT_PATH)
            .creationDate(DEFAULT_CREATION_DATE)
            .modificationDate(DEFAULT_MODIFICATION_DATE)
            .isPrivate(DEFAULT_IS_PRIVATE)
            .totalLikes(DEFAULT_TOTAL_LIKES)
            .totalCommentaries(DEFAULT_TOTAL_COMMENTARIES);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        image.setExtendedUser(extendedUser);
        return image;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createUpdatedEntity(EntityManager em) {
        Image image = new Image()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .path(UPDATED_PATH)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .totalLikes(UPDATED_TOTAL_LIKES)
            .totalCommentaries(UPDATED_TOTAL_COMMENTARIES);
        // Add required entity
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            extendedUser = ExtendedUserResourceIT.createUpdatedEntity(em);
            em.persist(extendedUser);
            em.flush();
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        image.setExtendedUser(extendedUser);
        return image;
    }

    @BeforeEach
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();
        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);
        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testImage.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testImage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testImage.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testImage.getIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
        assertThat(testImage.getTotalLikes()).isEqualTo(DEFAULT_TOTAL_LIKES);
        assertThat(testImage.getTotalCommentaries()).isEqualTo(DEFAULT_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void createImageWithExistingId() throws Exception {
        // Create the Image with an existing ID
        image.setId(1L);
        ImageDTO imageDTO = imageMapper.toDto(image);

        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setTitle(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setDescription(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setFileName(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setPath(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setCreationDate(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPrivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setIsPrivate(null);

        // Create the Image, which fails.
        ImageDTO imageDTO = imageMapper.toDto(image);

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].totalLikes").value(hasItem(DEFAULT_TOTAL_LIKES)))
            .andExpect(jsonPath("$.[*].totalCommentaries").value(hasItem(DEFAULT_TOTAL_COMMENTARIES)));
    }

    @Test
    @Transactional
    void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc
            .perform(get(ENTITY_API_URL_ID, image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
            .andExpect(jsonPath("$.totalLikes").value(DEFAULT_TOTAL_LIKES))
            .andExpect(jsonPath("$.totalCommentaries").value(DEFAULT_TOTAL_COMMENTARIES));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        Long id = image.getId();

        defaultImageShouldBeFound("id.equals=" + id);
        defaultImageShouldNotBeFound("id.notEquals=" + id);

        defaultImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImageShouldNotBeFound("id.greaterThan=" + id);

        defaultImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where title equals to DEFAULT_TITLE
        defaultImageShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the imageList where title equals to UPDATED_TITLE
        defaultImageShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllImagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultImageShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the imageList where title equals to UPDATED_TITLE
        defaultImageShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllImagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where title is not null
        defaultImageShouldBeFound("title.specified=true");

        // Get all the imageList where title is null
        defaultImageShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where title contains DEFAULT_TITLE
        defaultImageShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the imageList where title contains UPDATED_TITLE
        defaultImageShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllImagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where title does not contain DEFAULT_TITLE
        defaultImageShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the imageList where title does not contain UPDATED_TITLE
        defaultImageShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllImagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description equals to DEFAULT_DESCRIPTION
        defaultImageShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the imageList where description equals to UPDATED_DESCRIPTION
        defaultImageShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultImageShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the imageList where description equals to UPDATED_DESCRIPTION
        defaultImageShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description is not null
        defaultImageShouldBeFound("description.specified=true");

        // Get all the imageList where description is null
        defaultImageShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description contains DEFAULT_DESCRIPTION
        defaultImageShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the imageList where description contains UPDATED_DESCRIPTION
        defaultImageShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImagesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description does not contain DEFAULT_DESCRIPTION
        defaultImageShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the imageList where description does not contain UPDATED_DESCRIPTION
        defaultImageShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImagesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where fileName equals to DEFAULT_FILE_NAME
        defaultImageShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the imageList where fileName equals to UPDATED_FILE_NAME
        defaultImageShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultImageShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the imageList where fileName equals to UPDATED_FILE_NAME
        defaultImageShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where fileName is not null
        defaultImageShouldBeFound("fileName.specified=true");

        // Get all the imageList where fileName is null
        defaultImageShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where fileName contains DEFAULT_FILE_NAME
        defaultImageShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the imageList where fileName contains UPDATED_FILE_NAME
        defaultImageShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where fileName does not contain DEFAULT_FILE_NAME
        defaultImageShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the imageList where fileName does not contain UPDATED_FILE_NAME
        defaultImageShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where path equals to DEFAULT_PATH
        defaultImageShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the imageList where path equals to UPDATED_PATH
        defaultImageShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllImagesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where path in DEFAULT_PATH or UPDATED_PATH
        defaultImageShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the imageList where path equals to UPDATED_PATH
        defaultImageShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllImagesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where path is not null
        defaultImageShouldBeFound("path.specified=true");

        // Get all the imageList where path is null
        defaultImageShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByPathContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where path contains DEFAULT_PATH
        defaultImageShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the imageList where path contains UPDATED_PATH
        defaultImageShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllImagesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where path does not contain DEFAULT_PATH
        defaultImageShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the imageList where path does not contain UPDATED_PATH
        defaultImageShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllImagesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where creationDate equals to DEFAULT_CREATION_DATE
        defaultImageShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the imageList where creationDate equals to UPDATED_CREATION_DATE
        defaultImageShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllImagesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultImageShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the imageList where creationDate equals to UPDATED_CREATION_DATE
        defaultImageShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllImagesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where creationDate is not null
        defaultImageShouldBeFound("creationDate.specified=true");

        // Get all the imageList where creationDate is null
        defaultImageShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByModificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where modificationDate equals to DEFAULT_MODIFICATION_DATE
        defaultImageShouldBeFound("modificationDate.equals=" + DEFAULT_MODIFICATION_DATE);

        // Get all the imageList where modificationDate equals to UPDATED_MODIFICATION_DATE
        defaultImageShouldNotBeFound("modificationDate.equals=" + UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void getAllImagesByModificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where modificationDate in DEFAULT_MODIFICATION_DATE or UPDATED_MODIFICATION_DATE
        defaultImageShouldBeFound("modificationDate.in=" + DEFAULT_MODIFICATION_DATE + "," + UPDATED_MODIFICATION_DATE);

        // Get all the imageList where modificationDate equals to UPDATED_MODIFICATION_DATE
        defaultImageShouldNotBeFound("modificationDate.in=" + UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void getAllImagesByModificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where modificationDate is not null
        defaultImageShouldBeFound("modificationDate.specified=true");

        // Get all the imageList where modificationDate is null
        defaultImageShouldNotBeFound("modificationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByIsPrivateIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where isPrivate equals to DEFAULT_IS_PRIVATE
        defaultImageShouldBeFound("isPrivate.equals=" + DEFAULT_IS_PRIVATE);

        // Get all the imageList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultImageShouldNotBeFound("isPrivate.equals=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void getAllImagesByIsPrivateIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where isPrivate in DEFAULT_IS_PRIVATE or UPDATED_IS_PRIVATE
        defaultImageShouldBeFound("isPrivate.in=" + DEFAULT_IS_PRIVATE + "," + UPDATED_IS_PRIVATE);

        // Get all the imageList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultImageShouldNotBeFound("isPrivate.in=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void getAllImagesByIsPrivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where isPrivate is not null
        defaultImageShouldBeFound("isPrivate.specified=true");

        // Get all the imageList where isPrivate is null
        defaultImageShouldNotBeFound("isPrivate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes equals to DEFAULT_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.equals=" + DEFAULT_TOTAL_LIKES);

        // Get all the imageList where totalLikes equals to UPDATED_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.equals=" + UPDATED_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes in DEFAULT_TOTAL_LIKES or UPDATED_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.in=" + DEFAULT_TOTAL_LIKES + "," + UPDATED_TOTAL_LIKES);

        // Get all the imageList where totalLikes equals to UPDATED_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.in=" + UPDATED_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes is not null
        defaultImageShouldBeFound("totalLikes.specified=true");

        // Get all the imageList where totalLikes is null
        defaultImageShouldNotBeFound("totalLikes.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes is greater than or equal to DEFAULT_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.greaterThanOrEqual=" + DEFAULT_TOTAL_LIKES);

        // Get all the imageList where totalLikes is greater than or equal to UPDATED_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.greaterThanOrEqual=" + UPDATED_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes is less than or equal to DEFAULT_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.lessThanOrEqual=" + DEFAULT_TOTAL_LIKES);

        // Get all the imageList where totalLikes is less than or equal to SMALLER_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.lessThanOrEqual=" + SMALLER_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsLessThanSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes is less than DEFAULT_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.lessThan=" + DEFAULT_TOTAL_LIKES);

        // Get all the imageList where totalLikes is less than UPDATED_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.lessThan=" + UPDATED_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalLikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalLikes is greater than DEFAULT_TOTAL_LIKES
        defaultImageShouldNotBeFound("totalLikes.greaterThan=" + DEFAULT_TOTAL_LIKES);

        // Get all the imageList where totalLikes is greater than SMALLER_TOTAL_LIKES
        defaultImageShouldBeFound("totalLikes.greaterThan=" + SMALLER_TOTAL_LIKES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries equals to DEFAULT_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.equals=" + DEFAULT_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries equals to UPDATED_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.equals=" + UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries in DEFAULT_TOTAL_COMMENTARIES or UPDATED_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.in=" + DEFAULT_TOTAL_COMMENTARIES + "," + UPDATED_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries equals to UPDATED_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.in=" + UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries is not null
        defaultImageShouldBeFound("totalCommentaries.specified=true");

        // Get all the imageList where totalCommentaries is null
        defaultImageShouldNotBeFound("totalCommentaries.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries is greater than or equal to DEFAULT_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.greaterThanOrEqual=" + DEFAULT_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries is greater than or equal to UPDATED_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.greaterThanOrEqual=" + UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries is less than or equal to DEFAULT_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.lessThanOrEqual=" + DEFAULT_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries is less than or equal to SMALLER_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.lessThanOrEqual=" + SMALLER_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsLessThanSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries is less than DEFAULT_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.lessThan=" + DEFAULT_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries is less than UPDATED_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.lessThan=" + UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByTotalCommentariesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where totalCommentaries is greater than DEFAULT_TOTAL_COMMENTARIES
        defaultImageShouldNotBeFound("totalCommentaries.greaterThan=" + DEFAULT_TOTAL_COMMENTARIES);

        // Get all the imageList where totalCommentaries is greater than SMALLER_TOTAL_COMMENTARIES
        defaultImageShouldBeFound("totalCommentaries.greaterThan=" + SMALLER_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void getAllImagesByCommentariesIsEqualToSomething() throws Exception {
        Commentary commentaries;
        if (TestUtil.findAll(em, Commentary.class).isEmpty()) {
            imageRepository.saveAndFlush(image);
            commentaries = CommentaryResourceIT.createEntity(em);
        } else {
            commentaries = TestUtil.findAll(em, Commentary.class).get(0);
        }
        em.persist(commentaries);
        em.flush();
        image.addCommentaries(commentaries);
        imageRepository.saveAndFlush(image);
        Long commentariesId = commentaries.getId();

        // Get all the imageList where commentaries equals to commentariesId
        defaultImageShouldBeFound("commentariesId.equals=" + commentariesId);

        // Get all the imageList where commentaries equals to (commentariesId + 1)
        defaultImageShouldNotBeFound("commentariesId.equals=" + (commentariesId + 1));
    }

    @Test
    @Transactional
    void getAllImagesByExtendedUserIsEqualToSomething() throws Exception {
        ExtendedUser extendedUser;
        if (TestUtil.findAll(em, ExtendedUser.class).isEmpty()) {
            imageRepository.saveAndFlush(image);
            extendedUser = ExtendedUserResourceIT.createEntity(em);
        } else {
            extendedUser = TestUtil.findAll(em, ExtendedUser.class).get(0);
        }
        em.persist(extendedUser);
        em.flush();
        image.setExtendedUser(extendedUser);
        imageRepository.saveAndFlush(image);
        Long extendedUserId = extendedUser.getId();

        // Get all the imageList where extendedUser equals to extendedUserId
        defaultImageShouldBeFound("extendedUserId.equals=" + extendedUserId);

        // Get all the imageList where extendedUser equals to (extendedUserId + 1)
        defaultImageShouldNotBeFound("extendedUserId.equals=" + (extendedUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].totalLikes").value(hasItem(DEFAULT_TOTAL_LIKES)))
            .andExpect(jsonPath("$.[*].totalCommentaries").value(hasItem(DEFAULT_TOTAL_COMMENTARIES)));

        // Check, that the count call also returns 1
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).get();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .path(UPDATED_PATH)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .totalLikes(UPDATED_TOTAL_LIKES)
            .totalCommentaries(UPDATED_TOTAL_COMMENTARIES);
        ImageDTO imageDTO = imageMapper.toDto(updatedImage);

        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testImage.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testImage.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testImage.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testImage.getTotalLikes()).isEqualTo(UPDATED_TOTAL_LIKES);
        assertThat(testImage.getTotalCommentaries()).isEqualTo(UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void putNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageWithPatch() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .totalLikes(UPDATED_TOTAL_LIKES);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testImage.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testImage.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testImage.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testImage.getTotalLikes()).isEqualTo(UPDATED_TOTAL_LIKES);
        assertThat(testImage.getTotalCommentaries()).isEqualTo(DEFAULT_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void fullUpdateImageWithPatch() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .path(UPDATED_PATH)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .totalLikes(UPDATED_TOTAL_LIKES)
            .totalCommentaries(UPDATED_TOTAL_COMMENTARIES);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testImage.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testImage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testImage.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testImage.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testImage.getTotalLikes()).isEqualTo(UPDATED_TOTAL_LIKES);
        assertThat(testImage.getTotalCommentaries()).isEqualTo(UPDATED_TOTAL_COMMENTARIES);
    }

    @Test
    @Transactional
    void patchNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();
        image.setId(count.incrementAndGet());

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Delete the image
        restImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, image.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
