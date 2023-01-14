package es.project.repository;

import es.project.domain.Commentary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Commentary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long>, JpaSpecificationExecutor<Commentary> {}
