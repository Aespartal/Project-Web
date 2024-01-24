package es.project.service;

import es.project.service.dto.LikeImageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.project.domain.LikeImage}.
 */
public interface LikeImageService {
    /**
     * Save a likeImage.
     *
     * @param likeImageDTO the entity to save.
     * @return the persisted entity.
     */
    LikeImageDTO save(LikeImageDTO likeImageDTO);

    /**
     * Updates a likeImage.
     *
     * @param likeImageDTO the entity to update.
     * @return the persisted entity.
     */
    LikeImageDTO update(LikeImageDTO likeImageDTO);

    /**
     * Partially updates a likeImage.
     *
     * @param likeImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LikeImageDTO> partialUpdate(LikeImageDTO likeImageDTO);

    /**
     * Get all the likeImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LikeImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" likeImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LikeImageDTO> findOne(Long id);

    /**
     * Delete the "id" likeImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<LikeImageDTO> findByExtendedUserIdAndImageId(Long id, Long id1);

    void removeFromImageId(Long id);
}
