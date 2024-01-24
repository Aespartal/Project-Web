package es.project.repository;

import es.project.domain.LikeImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Spring Data JPA repository for the LikeImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeImageRepository extends JpaRepository<LikeImage, Long>, JpaSpecificationExecutor<LikeImage> {
    Optional<LikeImage> findByExtendedUserIdAndImageId(Long extendedUserId, Long imageId);

    void deleteByImageId(Long id);
}
