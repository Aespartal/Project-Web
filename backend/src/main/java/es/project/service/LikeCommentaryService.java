package es.project.service;

import es.project.service.dto.LikeCommentaryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.project.domain.LikeCommentary}.
 */
public interface LikeCommentaryService {
    /**
     * Save a likeCommentary.
     *
     * @param likeCommentaryDTO the entity to save.
     * @return the persisted entity.
     */
    LikeCommentaryDTO save(LikeCommentaryDTO likeCommentaryDTO);

    /**
     * Updates a likeCommentary.
     *
     * @param likeCommentaryDTO the entity to update.
     * @return the persisted entity.
     */
    LikeCommentaryDTO update(LikeCommentaryDTO likeCommentaryDTO);

    /**
     * Partially updates a likeCommentary.
     *
     * @param likeCommentaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LikeCommentaryDTO> partialUpdate(LikeCommentaryDTO likeCommentaryDTO);

    /**
     * Get all the likeCommentaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LikeCommentaryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" likeCommentary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LikeCommentaryDTO> findOne(Long id);

    /**
     * Delete the "id" likeCommentary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
