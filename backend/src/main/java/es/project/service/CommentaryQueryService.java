package es.project.service;

import es.project.domain.*; // for static metamodels
import es.project.domain.Commentary;
import es.project.repository.CommentaryRepository;
import es.project.service.criteria.CommentaryCriteria;
import es.project.service.dto.CommentaryDTO;
import es.project.service.mapper.CommentaryMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Commentary} entities in the database.
 * The main input is a {@link CommentaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentaryDTO} or a {@link Page} of {@link CommentaryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentaryQueryService extends QueryService<Commentary> {

    private final Logger log = LoggerFactory.getLogger(CommentaryQueryService.class);

    private final CommentaryRepository commentaryRepository;

    private final CommentaryMapper commentaryMapper;

    public CommentaryQueryService(CommentaryRepository commentaryRepository, CommentaryMapper commentaryMapper) {
        this.commentaryRepository = commentaryRepository;
        this.commentaryMapper = commentaryMapper;
    }

    /**
     * Return a {@link List} of {@link CommentaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentaryDTO> findByCriteria(CommentaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Commentary> specification = createSpecification(criteria);
        return commentaryMapper.toDto(commentaryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentaryDTO> findByCriteria(CommentaryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commentary> specification = createSpecification(criteria);
        return commentaryRepository.findAll(specification, page).map(commentaryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Commentary> specification = createSpecification(criteria);
        return commentaryRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentaryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Commentary> createSpecification(CommentaryCriteria criteria) {
        Specification<Commentary> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Commentary_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Commentary_.description));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Commentary_.creationDate));
            }
            if (criteria.getExtendedUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExtendedUserId(),
                            root -> root.join(Commentary_.extendedUser, JoinType.LEFT).get(ExtendedUser_.id)
                        )
                    );
            }
            if (criteria.getImageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImageId(), root -> root.join(Commentary_.image, JoinType.LEFT).get(Image_.id))
                    );
            }
        }
        return specification;
    }
}
