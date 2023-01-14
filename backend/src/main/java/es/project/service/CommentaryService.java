package es.project.service;

import es.project.service.dto.CommentaryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.project.domain.Commentary}.
 */
public interface CommentaryService {
    /**
     * Save a commentary.
     *
     * @param commentaryDTO the entity to save.
     * @return the persisted entity.
     */
    CommentaryDTO save(CommentaryDTO commentaryDTO);

    /**
     * Updates a commentary.
     *
     * @param commentaryDTO the entity to update.
     * @return the persisted entity.
     */
    CommentaryDTO update(CommentaryDTO commentaryDTO);

    /**
     * Partially updates a commentary.
     *
     * @param commentaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentaryDTO> partialUpdate(CommentaryDTO commentaryDTO);

    /**
     * Get all the commentaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentaryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentaryDTO> findOne(Long id);

    /**
     * Delete the "id" commentary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
