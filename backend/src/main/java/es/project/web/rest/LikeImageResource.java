package es.project.web.rest;

import es.project.repository.LikeImageRepository;
import es.project.service.LikeImageQueryService;
import es.project.service.LikeImageService;
import es.project.service.criteria.LikeImageCriteria;
import es.project.service.dto.LikeImageDTO;
import es.project.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.project.domain.LikeImage}.
 */
@RestController
@RequestMapping("/api")
public class LikeImageResource {

    private final Logger log = LoggerFactory.getLogger(LikeImageResource.class);

    private static final String ENTITY_NAME = "likeImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeImageService likeImageService;

    private final LikeImageRepository likeImageRepository;

    private final LikeImageQueryService likeImageQueryService;

    public LikeImageResource(
        LikeImageService likeImageService,
        LikeImageRepository likeImageRepository,
        LikeImageQueryService likeImageQueryService
    ) {
        this.likeImageService = likeImageService;
        this.likeImageRepository = likeImageRepository;
        this.likeImageQueryService = likeImageQueryService;
    }

    /**
     * {@code POST  /like-images} : Create a new likeImage.
     *
     * @param likeImageDTO the likeImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeImageDTO, or with status {@code 400 (Bad Request)} if the likeImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-images")
    public ResponseEntity<LikeImageDTO> createLikeImage(@Valid @RequestBody LikeImageDTO likeImageDTO) throws URISyntaxException {
        log.debug("REST request to save LikeImage : {}", likeImageDTO);
        if (likeImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new likeImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikeImageDTO result = likeImageService.save(likeImageDTO);
        return ResponseEntity
            .created(new URI("/api/like-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-images/:id} : Updates an existing likeImage.
     *
     * @param id the id of the likeImageDTO to save.
     * @param likeImageDTO the likeImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeImageDTO,
     * or with status {@code 400 (Bad Request)} if the likeImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-images/{id}")
    public ResponseEntity<LikeImageDTO> updateLikeImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LikeImageDTO likeImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikeImage : {}, {}", id, likeImageDTO);
        if (likeImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikeImageDTO result = likeImageService.update(likeImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-images/:id} : Partial updates given fields of an existing likeImage, field will ignore if it is null
     *
     * @param id the id of the likeImageDTO to save.
     * @param likeImageDTO the likeImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeImageDTO,
     * or with status {@code 400 (Bad Request)} if the likeImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likeImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeImageDTO> partialUpdateLikeImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LikeImageDTO likeImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeImage partially : {}, {}", id, likeImageDTO);
        if (likeImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeImageDTO> result = likeImageService.partialUpdate(likeImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /like-images} : get all the likeImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeImages in body.
     */
    @GetMapping("/like-images")
    public ResponseEntity<List<LikeImageDTO>> getAllLikeImages(
        LikeImageCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LikeImages by criteria: {}", criteria);
        Page<LikeImageDTO> page = likeImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /like-images/count} : count all the likeImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/like-images/count")
    public ResponseEntity<Long> countLikeImages(LikeImageCriteria criteria) {
        log.debug("REST request to count LikeImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(likeImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /like-images/:id} : get the "id" likeImage.
     *
     * @param id the id of the likeImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-images/{id}")
    public ResponseEntity<LikeImageDTO> getLikeImage(@PathVariable Long id) {
        log.debug("REST request to get LikeImage : {}", id);
        Optional<LikeImageDTO> likeImageDTO = likeImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likeImageDTO);
    }

    /**
     * {@code DELETE  /like-images/:id} : delete the "id" likeImage.
     *
     * @param id the id of the likeImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-images/{id}")
    public ResponseEntity<Void> deleteLikeImage(@PathVariable Long id) {
        log.debug("REST request to delete LikeImage : {}", id);
        likeImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
