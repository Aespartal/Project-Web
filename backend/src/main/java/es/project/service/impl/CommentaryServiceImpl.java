package es.project.service.impl;

import es.project.domain.Commentary;
import es.project.repository.CommentaryRepository;
import es.project.service.CommentaryService;
import es.project.service.dto.CommentaryDTO;
import es.project.service.mapper.CommentaryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commentary}.
 */
@Service
@Transactional
public class CommentaryServiceImpl implements CommentaryService {

    private final Logger log = LoggerFactory.getLogger(CommentaryServiceImpl.class);

    private final CommentaryRepository commentaryRepository;

    private final CommentaryMapper commentaryMapper;

    public CommentaryServiceImpl(CommentaryRepository commentaryRepository, CommentaryMapper commentaryMapper) {
        this.commentaryRepository = commentaryRepository;
        this.commentaryMapper = commentaryMapper;
    }

    @Override
    public CommentaryDTO save(CommentaryDTO commentaryDTO) {
        log.debug("Request to save Commentary : {}", commentaryDTO);
        Commentary commentary = commentaryMapper.toEntity(commentaryDTO);
        commentary = commentaryRepository.save(commentary);
        return commentaryMapper.toDto(commentary);
    }

    @Override
    public CommentaryDTO update(CommentaryDTO commentaryDTO) {
        log.debug("Request to update Commentary : {}", commentaryDTO);
        Commentary commentary = commentaryMapper.toEntity(commentaryDTO);
        commentary = commentaryRepository.save(commentary);
        return commentaryMapper.toDto(commentary);
    }

    @Override
    public Optional<CommentaryDTO> partialUpdate(CommentaryDTO commentaryDTO) {
        log.debug("Request to partially update Commentary : {}", commentaryDTO);

        return commentaryRepository
            .findById(commentaryDTO.getId())
            .map(existingCommentary -> {
                commentaryMapper.partialUpdate(existingCommentary, commentaryDTO);

                return existingCommentary;
            })
            .map(commentaryRepository::save)
            .map(commentaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaries");
        return commentaryRepository.findAll(pageable).map(commentaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentaryDTO> findOne(Long id) {
        log.debug("Request to get Commentary : {}", id);
        return commentaryRepository.findById(id).map(commentaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commentary : {}", id);
        commentaryRepository.deleteById(id);
    }
}
