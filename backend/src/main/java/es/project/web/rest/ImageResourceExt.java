package es.project.web.rest;

import es.project.config.ApplicationProperties;
import es.project.exception.BadRequestAlertException;
import es.project.repository.ImageRepository;
import es.project.service.ImageService;
import es.project.service.dto.ImageDTO;
import es.project.service.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link es.project.domain.Image}.
 */
@RestController
@RequestMapping("/api")
public class ImageResourceExt {

    private final Logger log = LoggerFactory.getLogger(ImageResourceExt.class);

    private static final String ENTITY_NAME = "image";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageService imageService;

    private final ImageRepository imageRepository;
    private final ApplicationProperties applicationProperties;

    public ImageResourceExt(ImageService imageService, ImageRepository imageRepository, ApplicationProperties applicationProperties) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /images} : Create a new image.
     *
     * @param imageDTO the imageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageDTO, or with status {@code 400 (Bad Request)} if the image has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @RequestMapping(value = "/images", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> createImage(
        @Valid @RequestPart("image") ImageDTO imageDTO,
        @RequestPart(value = "file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to save Image : {}", imageDTO);
        if (imageDTO.getId() != null) {
            throw new BadRequestAlertException("A new image cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageDTO result = imageService.createImage(imageDTO, file);
        return ResponseEntity
            .created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /images/:id} : Updates an existing image.
     *
     * @param id       the id of the imageDTO to save.
     * @param imageDTO the imageDTO to update.
     * @param file
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageDTO,
     * or with status {@code 400 (Bad Request)} if the imageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @RequestMapping(value = "/images/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> updateImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestPart("image") ImageDTO imageDTO,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to update Image : {}, {}", id, imageDTO);
        if (imageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageDTO result = imageService.updateImage(imageDTO, file);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /images/ext} : get all the images.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
   @GetMapping("/images/recent")
    public ResponseEntity<List<ImageDTO>> getRecentImages(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ImageDTO> page = imageService.findRecentImages(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /images/popular} : get popular images.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
    @GetMapping("/images/popular")
    public ResponseEntity<List<ImageDTO>> getPopularImages(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ImageDTO> page = imageService.findPopularImages(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/images/base64/{id}/{fileName}")
    public ResponseEntity<byte[]> getImageBase64(
        @PathVariable("id") Long id,
        @PathVariable("fileName") String fileName)
        throws IOException {
        byte[] imageBytes = Files.readAllBytes(FileUtil.getFilePath(id, fileName));
        return ResponseEntity.ok()
            .body(imageBytes);

    }

    @GetMapping("/images/{id}/like")
    public ResponseEntity<Void> likeImage(
        @PathVariable("id") Long id
    ) {
        imageService.findOne(id).ifPresent(
            imageService::likeImage
        );
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code DELETE  /images/:id} : delete the "id" image.
     *
     * @param id the id of the imageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        log.debug("REST request to delete Image : {}", id);
        imageService.deleteImage(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping(value = "/file-system/images/{id}/download-file")
    public ResponseEntity<ResourceRegion> downloadFile(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        log.debug("REST request to download file Product : {}", id);

        List<HttpRange> rangeList = headers.getRange();
        Optional<ResourceRegion> result;
        HttpStatus status;
        long maxChunkSize = applicationProperties.getMedia().getMaxChunkSize();
        if(rangeList.isEmpty()){
            result = imageService.findData(id,null,maxChunkSize);
            status = HttpStatus.OK;
        }else {
            result = imageService.findData(id, rangeList.get(0),maxChunkSize);
            status = HttpStatus.PARTIAL_CONTENT;
        }
        return result
            .map(body -> ResponseEntity
                .status(status)
                .contentType(MediaTypeFactory.getMediaType(body.getResource()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(body))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
