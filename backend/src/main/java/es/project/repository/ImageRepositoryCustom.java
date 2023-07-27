package es.project.repository;

import es.project.domain.ImageExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Image entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageRepositoryCustom extends JpaRepository<ImageExt, Long>, JpaSpecificationExecutor<ImageExt> {

    @Query(value = "SELECT * FROM f_images(:userId, :isPrivate, :page, :size, :allUsers, :isPopular);", nativeQuery = true)
    List<ImageExt> findImages(Long userId, Boolean isPrivate, int page, int size, boolean allUsers, boolean isPopular);

    @Query(value = "SELECT total FROM f_count_images(:userId, :isPrivate, :allUsers);", nativeQuery = true)
    long countImages(Long userId, Boolean isPrivate, boolean allUsers);


    // Page<ImageExt> findRecentImagesForUser(Pageable pageable, long id);
}
