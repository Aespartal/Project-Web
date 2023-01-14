package es.project.service;

import es.project.domain.*; // for static metamodels
import es.project.domain.LikeImage;
import es.project.repository.LikeImageRepository;
import es.project.service.criteria.LikeImageCriteria;
import es.project.service.dto.LikeImageDTO;
import es.project.service.mapper.LikeImageMapper;
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
 * Service for executing complex queries for {@link LikeImage} entities in the database.
 * The main input is a {@link LikeImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LikeImageDTO} or a {@link Page} of {@link LikeImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LikeImageQueryService extends QueryService<LikeImage> {

    private final Logger log = LoggerFactory.getLogger(LikeImageQueryService.class);

    private final LikeImageRepository likeImageRepository;

    private final LikeImageMapper likeImageMapper;

    public LikeImageQueryService(LikeImageRepository likeImageRepository, LikeImageMapper likeImageMapper) {
        this.likeImageRepository = likeImageRepository;
        this.likeImageMapper = likeImageMapper;
    }

    /**
     * Return a {@link List} of {@link LikeImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LikeImageDTO> findByCriteria(LikeImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LikeImage> specification = createSpecification(criteria);
        return likeImageMapper.toDto(likeImageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LikeImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LikeImageDTO> findByCriteria(LikeImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LikeImage> specification = createSpecification(criteria);
        return likeImageRepository.findAll(specification, page).map(likeImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LikeImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LikeImage> specification = createSpecification(criteria);
        return likeImageRepository.count(specification);
    }

    /**
     * Function to convert {@link LikeImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LikeImage> createSpecification(LikeImageCriteria criteria) {
        Specification<LikeImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LikeImage_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), LikeImage_.creationDate));
            }
            if (criteria.getImageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImageId(), root -> root.join(LikeImage_.image, JoinType.LEFT).get(Image_.id))
                    );
            }
            if (criteria.getExtendedUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExtendedUserId(),
                            root -> root.join(LikeImage_.extendedUser, JoinType.LEFT).get(ExtendedUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
