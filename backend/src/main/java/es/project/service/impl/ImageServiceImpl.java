package es.project.service.impl;

import es.project.domain.Image;
import es.project.errors.ValidationException;
import es.project.repository.ImageRepository;
import es.project.service.ImageService;
import es.project.service.dto.ImageDTO;
import es.project.service.mapper.ImageMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import es.project.service.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Image}.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    public ImageDTO save(ImageDTO imageDTO) {
        log.debug("Request to save Image : {}", imageDTO);
        Image image = imageMapper.toEntity(imageDTO);
        image = imageRepository.save(image);
        return imageMapper.toDto(image);
    }

    @Override
    public ImageDTO createImage(ImageDTO imageDTO, MultipartFile file) {
        log.debug("Request to save Image : {}", imageDTO);
        Image image = imageMapper.toEntity(imageDTO);
        image.setImage(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        image = imageRepository.save(image);
        String pathImage = saveFile(image.getId(), file);
        image.setImage(pathImage);
        imageRepository.save(image);
        return imageMapper.toDto(image);
    }

    @Override
    public ImageDTO update(ImageDTO imageDTO) {
        log.debug("Request to update Image : {}", imageDTO);
        Image image = imageMapper.toEntity(imageDTO);
        image = imageRepository.save(image);
        return imageMapper.toDto(image);
    }

    @Override
    public Optional<ImageDTO> partialUpdate(ImageDTO imageDTO) {
        log.debug("Request to partially update Image : {}", imageDTO);

        return imageRepository
            .findById(imageDTO.getId())
            .map(existingImage -> {
                imageMapper.partialUpdate(existingImage, imageDTO);

                return existingImage;
            })
            .map(imageRepository::save)
            .map(imageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imageRepository.findAll(pageable).map(imageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageDTO> findOne(Long id) {
        log.debug("Request to get Image : {}", id);
        return imageRepository.findById(id).map(imageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
    }

    private String saveFile(Long id, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new ValidationException("Filename is required");
            }
            Path target = FileUtil.getImagePath(id).resolve(filename);
            FileUtil.saveFile(file, target);
            return target.toString();
        } catch (IOException e) {
            throw new ValidationException("Error saving files");
        }
    }
}
