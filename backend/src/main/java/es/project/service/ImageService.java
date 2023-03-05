package es.project.service;

import es.project.service.dto.ImageDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link es.project.domain.Image}.
 */
public interface ImageService {
    /**
     * Save a image.
     *
     * @param imageDTO the entity to save.
     * @return the persisted entity.
     */
    ImageDTO save(ImageDTO imageDTO);

    /**
     * Updates a image.
     *
     * @param imageDTO the entity to update.
     * @return the persisted entity.
     */
    ImageDTO update(ImageDTO imageDTO);

    /**
     * Partially updates a image.
     *
     * @param imageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImageDTO> partialUpdate(ImageDTO imageDTO);

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" image.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImageDTO> findOne(Long id);

    /**
     * Delete the "id" image.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ImageDTO createImage(ImageDTO imageDTO, MultipartFile file);

    ImageDTO updateImage(ImageDTO imageDTO, MultipartFile file);

    Page<ImageDTO> findPopularImages(Pageable page);

    Page<ImageDTO> findRecentImages(Pageable pageable);

    void likeImage(ImageDTO imageDTO);

    void deleteImage(Long id);
}
