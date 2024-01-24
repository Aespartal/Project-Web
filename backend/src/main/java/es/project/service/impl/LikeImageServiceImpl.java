package es.project.service.impl;

import es.project.domain.LikeImage;
import es.project.repository.LikeImageRepository;
import es.project.service.LikeImageService;
import es.project.service.dto.LikeImageDTO;
import es.project.service.mapper.LikeImageMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LikeImage}.
 */
@Service
@Transactional
public class LikeImageServiceImpl implements LikeImageService {

    private final Logger log = LoggerFactory.getLogger(LikeImageServiceImpl.class);

    private final LikeImageRepository likeImageRepository;

    private final LikeImageMapper likeImageMapper;

    public LikeImageServiceImpl(LikeImageRepository likeImageRepository, LikeImageMapper likeImageMapper) {
        this.likeImageRepository = likeImageRepository;
        this.likeImageMapper = likeImageMapper;
    }

    @Override
    public LikeImageDTO save(LikeImageDTO likeImageDTO) {
        log.debug("Request to save LikeImage : {}", likeImageDTO);
        LikeImage likeImage = likeImageMapper.toEntity(likeImageDTO);
        likeImage = likeImageRepository.save(likeImage);
        return likeImageMapper.toDto(likeImage);
    }

    @Override
    public LikeImageDTO update(LikeImageDTO likeImageDTO) {
        log.debug("Request to update LikeImage : {}", likeImageDTO);
        LikeImage likeImage = likeImageMapper.toEntity(likeImageDTO);
        likeImage = likeImageRepository.save(likeImage);
        return likeImageMapper.toDto(likeImage);
    }

    @Override
    public Optional<LikeImageDTO> partialUpdate(LikeImageDTO likeImageDTO) {
        log.debug("Request to partially update LikeImage : {}", likeImageDTO);

        return likeImageRepository
            .findById(likeImageDTO.getId())
            .map(existingLikeImage -> {
                likeImageMapper.partialUpdate(existingLikeImage, likeImageDTO);

                return existingLikeImage;
            })
            .map(likeImageRepository::save)
            .map(likeImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LikeImages");
        return likeImageRepository.findAll(pageable).map(likeImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LikeImageDTO> findOne(Long id) {
        log.debug("Request to get LikeImage : {}", id);
        return likeImageRepository.findById(id).map(likeImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LikeImage : {}", id);
        likeImageRepository.deleteById(id);
    }

    @Override
    public Optional<LikeImageDTO> findByExtendedUserIdAndImageId(Long extendedUserId, Long imageId) {
        return likeImageRepository.findByExtendedUserIdAndImageId(extendedUserId, imageId).map(likeImageMapper::toDto);
    }

    @Override
    @Transactional()
    public void removeFromImageId(Long id) {
        likeImageRepository.deleteByImageId(id);
    }
}
