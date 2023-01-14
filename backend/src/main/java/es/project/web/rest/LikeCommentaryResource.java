package es.project.web.rest;

import es.project.repository.LikeCommentaryRepository;
import es.project.service.LikeCommentaryQueryService;
import es.project.service.LikeCommentaryService;
import es.project.service.criteria.LikeCommentaryCriteria;
import es.project.service.dto.LikeCommentaryDTO;
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
 * REST controller for managing {@link es.project.domain.LikeCommentary}.
 */
@RestController
@RequestMapping("/api")
public class LikeCommentaryResource {

    private final Logger log = LoggerFactory.getLogger(LikeCommentaryResource.class);

    private static final String ENTITY_NAME = "likeCommentary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeCommentaryService likeCommentaryService;

    private final LikeCommentaryRepository likeCommentaryRepository;

    private final LikeCommentaryQueryService likeCommentaryQueryService;

    public LikeCommentaryResource(
        LikeCommentaryService likeCommentaryService,
        LikeCommentaryRepository likeCommentaryRepository,
        LikeCommentaryQueryService likeCommentaryQueryService
    ) {
        this.likeCommentaryService = likeCommentaryService;
        this.likeCommentaryRepository = likeCommentaryRepository;
        this.likeCommentaryQueryService = likeCommentaryQueryService;
    }

    /**
     * {@code POST  /like-commentaries} : Create a new likeCommentary.
     *
     * @param likeCommentaryDTO the likeCommentaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeCommentaryDTO, or with status {@code 400 (Bad Request)} if the likeCommentary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-commentaries")
    public ResponseEntity<LikeCommentaryDTO> createLikeCommentary(@Valid @RequestBody LikeCommentaryDTO likeCommentaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save LikeCommentary : {}", likeCommentaryDTO);
        if (likeCommentaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new likeCommentary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikeCommentaryDTO result = likeCommentaryService.save(likeCommentaryDTO);
        return ResponseEntity
            .created(new URI("/api/like-commentaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-commentaries/:id} : Updates an existing likeCommentary.
     *
     * @param id the id of the likeCommentaryDTO to save.
     * @param likeCommentaryDTO the likeCommentaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeCommentaryDTO,
     * or with status {@code 400 (Bad Request)} if the likeCommentaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeCommentaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-commentaries/{id}")
    public ResponseEntity<LikeCommentaryDTO> updateLikeCommentary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LikeCommentaryDTO likeCommentaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikeCommentary : {}, {}", id, likeCommentaryDTO);
        if (likeCommentaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeCommentaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeCommentaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikeCommentaryDTO result = likeCommentaryService.update(likeCommentaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeCommentaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-commentaries/:id} : Partial updates given fields of an existing likeCommentary, field will ignore if it is null
     *
     * @param id the id of the likeCommentaryDTO to save.
     * @param likeCommentaryDTO the likeCommentaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeCommentaryDTO,
     * or with status {@code 400 (Bad Request)} if the likeCommentaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likeCommentaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeCommentaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-commentaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeCommentaryDTO> partialUpdateLikeCommentary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LikeCommentaryDTO likeCommentaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeCommentary partially : {}, {}", id, likeCommentaryDTO);
        if (likeCommentaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeCommentaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeCommentaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeCommentaryDTO> result = likeCommentaryService.partialUpdate(likeCommentaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeCommentaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /like-commentaries} : get all the likeCommentaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeCommentaries in body.
     */
    @GetMapping("/like-commentaries")
    public ResponseEntity<List<LikeCommentaryDTO>> getAllLikeCommentaries(
        LikeCommentaryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LikeCommentaries by criteria: {}", criteria);
        Page<LikeCommentaryDTO> page = likeCommentaryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /like-commentaries/count} : count all the likeCommentaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/like-commentaries/count")
    public ResponseEntity<Long> countLikeCommentaries(LikeCommentaryCriteria criteria) {
        log.debug("REST request to count LikeCommentaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(likeCommentaryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /like-commentaries/:id} : get the "id" likeCommentary.
     *
     * @param id the id of the likeCommentaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeCommentaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-commentaries/{id}")
    public ResponseEntity<LikeCommentaryDTO> getLikeCommentary(@PathVariable Long id) {
        log.debug("REST request to get LikeCommentary : {}", id);
        Optional<LikeCommentaryDTO> likeCommentaryDTO = likeCommentaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likeCommentaryDTO);
    }

    /**
     * {@code DELETE  /like-commentaries/:id} : delete the "id" likeCommentary.
     *
     * @param id the id of the likeCommentaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-commentaries/{id}")
    public ResponseEntity<Void> deleteLikeCommentary(@PathVariable Long id) {
        log.debug("REST request to delete LikeCommentary : {}", id);
        likeCommentaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
