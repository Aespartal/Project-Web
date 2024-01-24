package es.project.service.impl;

import es.project.domain.Image;
import es.project.domain.ImageExt;
import es.project.exception.CurrentUserNotFoundException;
import es.project.exception.ValidationException;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import es.project.service.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRange;
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

        Optional<ExtendedUserDTO> currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser();

        imageDTO.setExtendedUser(currentExtendedUserDTO.get());

        Image image = createImageEntity(imageDTO, file);

        image = imageRepository.save(image);

        // Save the file
        saveFile(file, FileUtil.getImagePath(currentExtendedUserDTO.get().getId()).resolve(image.getFileName()));

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
        Optional<ExtendedUserDTO> currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser();
        List<ImageExt> imageExtList;
        long total;
        boolean allUsers = true;
        boolean isPrivate = false;
        boolean isPopular = true;
        if (currentExtendedUserDTO.isPresent()) {
            Long id = currentExtendedUserDTO.get().getId();
            imageExtList = imageRepositoryCustom.findImages(id, isPrivate, page.getPageNumber(), page.getPageSize(), allUsers, isPopular);
            total = imageRepositoryCustom.countImages(id, isPrivate, allUsers);
        } else {
            imageExtList = imageRepositoryCustom.findImages(null, isPrivate, page.getPageNumber(), page.getPageSize(), allUsers, isPopular);
            total = imageRepositoryCustom.countImages( null, isPrivate, allUsers);
        }
        return new PageImpl<>(imageExtMapper.toDto(imageExtList), page, total);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageDTO> findRecentImages(Pageable page) {
        log.debug("find by page: {}", page);
        Optional<ExtendedUserDTO> currentExtendedUserDTO = extendedUserService.getCurrentExtendedUser();
        boolean allUsers = true;
        boolean isPrivate = false;
        boolean isPopular = false;

        Long id = currentExtendedUserDTO.map(ExtendedUserDTO::getId).orElse(null);

        List<ImageExt> imageExtList = imageRepositoryCustom.findImages(id, isPrivate, page.getPageNumber(), page.getPageSize(), allUsers, isPopular);
        long total = imageRepositoryCustom.countImages(id, isPrivate, allUsers);

        return new PageImpl<>(imageExtMapper.toDto(imageExtList), page, total);
    }

    @Override
    public void likeImage(ImageDTO imageDTO) {
        ExtendedUserDTO currentExtendedUserDTO = getCurrentUser();

        Optional<LikeImageDTO> likeImageDTOOptional = likeImageService.findByExtendedUserIdAndImageId(currentExtendedUserDTO.getId(), imageDTO.getId());

        likeImageDTOOptional.ifPresent(likeImageDTO -> likeImageService.delete(likeImageDTO.getId()));

        likeImageDTOOptional.orElseGet(() -> {
            LikeImageDTO likeImageDTO = new LikeImageDTO();
            likeImageDTO.setImage(imageDTO);
            likeImageDTO.setCreationDate(Instant.now());
            likeImageDTO.setExtendedUser(currentExtendedUserDTO);
            return likeImageService.save(likeImageDTO);
        });
    }

    @Override
    public void deleteImage(Long id) {
        likeImageService.removeFromImageId(id);
        imageRepository.deleteById(id);
    }

    @Override
    public Optional<ResourceRegion> findData(Long id, HttpRange range, long maxChunkSize) {
        Optional<ImageDTO> imageOpt = findOne(id);
        if (!imageOpt.isPresent()) {
            return Optional.empty();
        }
        ImageDTO imageDTO = imageOpt.get();
        try {
            Resource resource = new FileSystemResource(new File(imageDTO.getPath()));
            long contentLength = resource.contentLength();
            long position = 0;
            long count = contentLength;
            if (range != null) {
                position = range.getRangeStart(contentLength);
                count = Math.min(maxChunkSize, range.getRangeEnd(contentLength) - position + 1);
            }
            return Optional.of(new ResourceRegion(resource, position, count));
        } catch (IOException e) {
            return Optional.empty();
        }
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


    private Image createImageEntity(ImageDTO imageDTO, MultipartFile file) {
        Image image = imageMapper.toEntity(imageDTO);
        setDefaultImageValues(image, file);

        // Set image path based on user and filename
        setImagePath(image, imageDTO.getExtendedUser().getId());

        return image;
    }

    private void setDefaultImageValues(Image image, MultipartFile file) {
        // Set default values for new image
        image.setTotalCommentaries(0);
        image.setTotalLikes(0);

        // Set filename with timestamp
        Instant today = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSSZ");
        String formattedDate = today.atZone(ZoneId.systemDefault()).format(formatter);

        image.setFileName(file.getOriginalFilename().concat("-").concat(formattedDate));


        // Set creation date
        image.setCreationDate(today);
    }

    private void setImagePath(Image image, Long userId) {
        // Set image path based on user and filename
        Path pathImage = FileUtil.getImagePath(userId).resolve(image.getFileName());
        image.setPath(pathImage.toString());
    }

    private ExtendedUserDTO getCurrentUser() {
        return extendedUserService.getCurrentExtendedUser().orElseThrow(CurrentUserNotFoundException::new);
    }
}
