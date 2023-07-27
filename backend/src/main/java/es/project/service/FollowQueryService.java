package es.project.service;

import es.project.domain.*; // for static metamodels
import es.project.domain.Follow;
import es.project.repository.FollowRepository;
import es.project.service.criteria.FollowCriteria;
import es.project.service.dto.FollowDTO;
import es.project.service.mapper.FollowMapper;
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
 * Service for executing complex queries for {@link Follow} entities in the database.
 * The main input is a {@link FollowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FollowDTO} or a {@link Page} of {@link FollowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FollowQueryService extends QueryService<Follow> {

    private final Logger log = LoggerFactory.getLogger(FollowQueryService.class);

    private final FollowRepository followRepository;

    private final FollowMapper followMapper;

    public FollowQueryService(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
    }

    /**
     * Return a {@link List} of {@link FollowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FollowDTO> findByCriteria(FollowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Follow> specification = createSpecification(criteria);
        return followMapper.toDto(followRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FollowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FollowDTO> findByCriteria(FollowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Follow> specification = createSpecification(criteria);
        return followRepository.findAll(specification, page).map(followMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FollowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Follow> specification = createSpecification(criteria);
        return followRepository.count(specification);
    }

    /**
     * Function to convert {@link FollowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Follow> createSpecification(FollowCriteria criteria) {
        Specification<Follow> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Follow_.id));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Follow_.state));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Follow_.creationDate));
            }
            if (criteria.getAcceptanceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcceptanceDate(), Follow_.acceptanceDate));
            }
            if (criteria.getFollowerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFollowerId(),
                            root -> root.join(Follow_.follower, JoinType.LEFT).get(ExtendedUser_.id)
                        )
                    );
            }
            if (criteria.getFollowingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFollowingId(),
                            root -> root.join(Follow_.following, JoinType.LEFT).get(ExtendedUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
