package es.project.repository;

import es.project.domain.LikeCommentary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeCommentary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeCommentaryRepository extends JpaRepository<LikeCommentary, Long>, JpaSpecificationExecutor<LikeCommentary> {}
