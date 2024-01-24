package es.project.web.rest;

import es.project.repository.CommentaryRepository;
import es.project.service.CommentaryQueryService;
import es.project.service.CommentaryService;
import es.project.service.criteria.CommentaryCriteria;
import es.project.service.dto.CommentaryDTO;
import es.project.exception.BadRequestAlertException;
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
 * REST controller for managing {@link es.project.domain.Commentary}.
 */
@RestController
@RequestMapping("/api")
public class CommentaryResource {

    private final Logger log = LoggerFactory.getLogger(CommentaryResource.class);

    private static final String ENTITY_NAME = "commentary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentaryService commentaryService;

    private final CommentaryRepository commentaryRepository;

    private final CommentaryQueryService commentaryQueryService;

    public CommentaryResource(
        CommentaryService commentaryService,
        CommentaryRepository commentaryRepository,
        CommentaryQueryService commentaryQueryService
    ) {
        this.commentaryService = commentaryService;
        this.commentaryRepository = commentaryRepository;
        this.commentaryQueryService = commentaryQueryService;
    }

    /**
     * {@code POST  /commentaries} : Create a new commentary.
     *
     * @param commentaryDTO the commentaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentaryDTO, or with status {@code 400 (Bad Request)} if the commentary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commentaries")
    public ResponseEntity<CommentaryDTO> createCommentary(@Valid @RequestBody CommentaryDTO commentaryDTO) throws URISyntaxException {
        log.debug("REST request to save Commentary : {}", commentaryDTO);
        if (commentaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentaryDTO result = commentaryService.save(commentaryDTO);
        return ResponseEntity
            .created(new URI("/api/commentaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commentaries/:id} : Updates an existing commentary.
     *
     * @param id the id of the commentaryDTO to save.
     * @param commentaryDTO the commentaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaryDTO,
     * or with status {@code 400 (Bad Request)} if the commentaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commentaries/{id}")
    public ResponseEntity<CommentaryDTO> updateCommentary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentaryDTO commentaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Commentary : {}, {}", id, commentaryDTO);
        if (commentaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommentaryDTO result = commentaryService.update(commentaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commentaries/:id} : Partial updates given fields of an existing commentary, field will ignore if it is null
     *
     * @param id the id of the commentaryDTO to save.
     * @param commentaryDTO the commentaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaryDTO,
     * or with status {@code 400 (Bad Request)} if the commentaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commentaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentaryDTO> partialUpdateCommentary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentaryDTO commentaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commentary partially : {}, {}", id, commentaryDTO);
        if (commentaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentaryDTO> result = commentaryService.partialUpdate(commentaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commentaries} : get all the commentaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentaries in body.
     */
    @GetMapping("/commentaries")
    public ResponseEntity<List<CommentaryDTO>> getAllCommentaries(
        CommentaryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Commentaries by criteria: {}", criteria);
        Page<CommentaryDTO> page = commentaryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commentaries/count} : count all the commentaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/commentaries/count")
    public ResponseEntity<Long> countCommentaries(CommentaryCriteria criteria) {
        log.debug("REST request to count Commentaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentaryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /commentaries/:id} : get the "id" commentary.
     *
     * @param id the id of the commentaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commentaries/{id}")
    public ResponseEntity<CommentaryDTO> getCommentary(@PathVariable Long id) {
        log.debug("REST request to get Commentary : {}", id);
        Optional<CommentaryDTO> commentaryDTO = commentaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentaryDTO);
    }

    /**
     * {@code DELETE  /commentaries/:id} : delete the "id" commentary.
     *
     * @param id the id of the commentaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commentaries/{id}")
    public ResponseEntity<Void> deleteCommentary(@PathVariable Long id) {
        log.debug("REST request to delete Commentary : {}", id);
        commentaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
