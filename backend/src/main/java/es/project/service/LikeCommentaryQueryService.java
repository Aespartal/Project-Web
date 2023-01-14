package es.project.service;

import es.project.domain.*; // for static metamodels
import es.project.domain.LikeCommentary;
import es.project.repository.LikeCommentaryRepository;
import es.project.service.criteria.LikeCommentaryCriteria;
import es.project.service.dto.LikeCommentaryDTO;
import es.project.service.mapper.LikeCommentaryMapper;
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
 * Service for executing complex queries for {@link LikeCommentary} entities in the database.
 * The main input is a {@link LikeCommentaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LikeCommentaryDTO} or a {@link Page} of {@link LikeCommentaryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LikeCommentaryQueryService extends QueryService<LikeCommentary> {

    private final Logger log = LoggerFactory.getLogger(LikeCommentaryQueryService.class);

    private final LikeCommentaryRepository likeCommentaryRepository;

    private final LikeCommentaryMapper likeCommentaryMapper;

    public LikeCommentaryQueryService(LikeCommentaryRepository likeCommentaryRepository, LikeCommentaryMapper likeCommentaryMapper) {
        this.likeCommentaryRepository = likeCommentaryRepository;
        this.likeCommentaryMapper = likeCommentaryMapper;
    }

    /**
     * Return a {@link List} of {@link LikeCommentaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LikeCommentaryDTO> findByCriteria(LikeCommentaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LikeCommentary> specification = createSpecification(criteria);
        return likeCommentaryMapper.toDto(likeCommentaryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LikeCommentaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LikeCommentaryDTO> findByCriteria(LikeCommentaryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LikeCommentary> specification = createSpecification(criteria);
        return likeCommentaryRepository.findAll(specification, page).map(likeCommentaryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LikeCommentaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LikeCommentary> specification = createSpecification(criteria);
        return likeCommentaryRepository.count(specification);
    }

    /**
     * Function to convert {@link LikeCommentaryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LikeCommentary> createSpecification(LikeCommentaryCriteria criteria) {
        Specification<LikeCommentary> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LikeCommentary_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), LikeCommentary_.creationDate));
            }
            if (criteria.getCommentaryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommentaryId(),
                            root -> root.join(LikeCommentary_.commentary, JoinType.LEFT).get(Commentary_.id)
                        )
                    );
            }
            if (criteria.getExtendedUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExtendedUserId(),
                            root -> root.join(LikeCommentary_.extendedUser, JoinType.LEFT).get(ExtendedUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
