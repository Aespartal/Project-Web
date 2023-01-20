package es.project.repository;

import es.project.domain.LikeImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeImageRepository extends JpaRepository<LikeImage, Long>, JpaSpecificationExecutor<LikeImage> {}
