package es.project.service;

import es.project.service.dto.ExtendedUserDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.project.domain.ExtendedUser}.
 */
public interface ExtendedUserService {
    /**
     * Save a extendedUser.
     *
     * @param extendedUserDTO the entity to save.
     * @return the persisted entity.
     */
    ExtendedUserDTO save(ExtendedUserDTO extendedUserDTO);

    /**
     * Updates a extendedUser.
     *
     * @param extendedUserDTO the entity to update.
     * @return the persisted entity.
     */
    ExtendedUserDTO update(ExtendedUserDTO extendedUserDTO);

    /**
     * Partially updates a extendedUser.
     *
     * @param extendedUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtendedUserDTO> partialUpdate(ExtendedUserDTO extendedUserDTO);

    /**
     * Get all the extendedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtendedUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" extendedUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtendedUserDTO> findOne(Long id);

    /**
     * Delete the "id" extendedUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ExtendedUserDTO> getCurrentExtendedUser();
}
