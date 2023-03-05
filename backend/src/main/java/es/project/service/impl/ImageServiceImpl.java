package es.project.service.impl;

import es.project.domain.ExtendedUser;
import es.project.domain.Image;
import es.project.domain.LikeImage;
import es.project.errors.CurrentUserNotFoundException;
import es.project.errors.ValidationException;
import es.project.repository.ImageRepository;
import es.project.repository.ImageRepositoryCustom;
import es.project.service.ExtendedUserService;
import es.project.service.ImageService;
import es.project.service.LikeImageService;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.dto.ImageDTO;
import es.project.service.dto.LikeImageDTO;
import es.project.service.mapper.ImageExtMapper;
import es.project.service.mapper.ImageMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
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
    private final ImageRepositoryCustom imageRepositoryCustom;
    private final ImageMapper imageMapper;
    private final ImageExtMapper imageExtMapper;
    private final ExtendedUserService extendedUserService;
    private final LikeImageService likeImageService;
    public ImageServiceImpl(ImageRepository imageRepository, ImageRepositoryCustom imageRepositoryCustom, ImageMapper imageMapper, ImageExtMapper imageExtMapper, ExtendedUserService extendedUserService, LikeImageService likeImageService) {
        this.imageRepository = imageRepository;
        this.imageRepositoryCustom = imageRepositoryCustom;
        this.imageMapper = imageMapper;
        this.imageExtMapper = imageExtMapper;
        this.extendedUserService = extendedUserService;
        this.likeImageService = likeImageService;
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
        image.setFileName(file.getOriginalFilename());
        image.setCreationDate(Instant.now());
        Path pathImage = FileUtil.getImagePath(imageDTO.getExtendedUser().getId()).resolve(image.getFileName());
        image.setPath(pathImage.toString());
        image = imageRepository.save(image);
        saveFile(file, pathImage);
        return imageMapper.toDto(image);
    }

    @Override
    public ImageDTO updateImage(ImageDTO imageDTO, MultipartFile file) {
        log.debug("Request to save Image : {}", imageDTO);
        Image image = imageMapper.toEntity(imageDTO);
        image.setModificationDate(Instant.now());
        if (file != null) {
            image.setFileName(file.getOriginalFilename());
            Path pathImage = FileUtil.getImagePath(imageDTO.getExtendedUser().getId()).resolve(image.getFileName());
            image.setPath(pathImage.toString());
            saveFile(file, pathImage);
        }
        image = imageRepository.save(image);
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

    @Override
    @Transactional(readOnly = true)
    public Page<ImageDTO> findPopularImages(Pageable page) {
        log.debug("find by page: {}", page);
        Optional<ExtendedUserDTO> currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser();
        if (currentExtendedUserDTO.isPresent()) {
            return imageRepositoryCustom.findPopularImagesForUser(page, currentExtendedUserDTO.get().getId()).map(imageExtMapper::toDto);
        }
        return imageRepositoryCustom.findPopularImages(page).map(imageExtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageDTO> findRecentImages(Pageable page) {
        log.debug("find by page: {}", page);
        Optional<ExtendedUserDTO> currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser();
        if (currentExtendedUserDTO.isPresent()) {
            return imageRepositoryCustom.findRecentImagesForUser(page, currentExtendedUserDTO.get().getId()).map(imageExtMapper::toDto);
        }
        return imageRepositoryCustom.findRecentImages(page).map(imageExtMapper::toDto);
    }

    @Override
    public void likeImage(ImageDTO imageDTO) {
        ExtendedUserDTO currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser()
            .orElseThrow(CurrentUserNotFoundException::new);

        Optional<LikeImageDTO> likeImageDTOOptional = likeImageService.findByExtendedUserIdAndImageId(currentExtendedUserDTO.getId(), imageDTO.getId());

        if (!likeImageDTOOptional.isPresent()) {
            LikeImageDTO likeImageDTO = new LikeImageDTO();
            likeImageDTO.setImage(imageDTO);
            likeImageDTO.setCreationDate(Instant.now());
            likeImageDTO.setExtendedUser(currentExtendedUserDTO);
            likeImageService.save(likeImageDTO);
        } else {
            likeImageService.delete(likeImageDTOOptional.get().getId());
        }
    }

    @Override
    public void deleteImage(Long id) {
        likeImageService.removeFromImageId(id);
        likeImageService.delete(id);
    }

    private String saveFile(MultipartFile file, Path target) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new ValidationException("Filename is required");
            }
            FileUtil.saveFile(file, target);
            return target.toString();
        } catch (IOException e) {
            throw new ValidationException("Error saving files");
        }
    }
}
