package es.project.service.impl;

import es.project.domain.ExtendedUser;
import es.project.domain.User;
import es.project.repository.ExtendedUserRepository;
import es.project.repository.UserRepository;
import es.project.service.ExtendedUserService;
import es.project.service.dto.ExtendedUserDTO;
import es.project.service.mapper.ExtendedUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExtendedUser}.
 */
@Service
@Transactional
public class ExtendedUserServiceImpl implements ExtendedUserService {

    private final Logger log = LoggerFactory.getLogger(ExtendedUserServiceImpl.class);

    private final ExtendedUserRepository extendedUserRepository;

    private final ExtendedUserMapper extendedUserMapper;

    private final UserRepository userRepository;

    public ExtendedUserServiceImpl(
        ExtendedUserRepository extendedUserRepository,
        ExtendedUserMapper extendedUserMapper,
        UserRepository userRepository
    ) {
        this.extendedUserRepository = extendedUserRepository;
        this.extendedUserMapper = extendedUserMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ExtendedUserDTO save(ExtendedUserDTO extendedUserDTO) {
        log.debug("Request to save ExtendedUser : {}", extendedUserDTO);
        ExtendedUser extendedUser = extendedUserMapper.toEntity(extendedUserDTO);
        Long userId = extendedUserDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(extendedUser::user);
        extendedUser = extendedUserRepository.save(extendedUser);
        return extendedUserMapper.toDto(extendedUser);
    }

    @Override
    public ExtendedUserDTO update(ExtendedUserDTO extendedUserDTO) {
        log.debug("Request to update ExtendedUser : {}", extendedUserDTO);
        ExtendedUser extendedUser = extendedUserMapper.toEntity(extendedUserDTO);
        Long userId = extendedUserDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(extendedUser::user);
        extendedUser = extendedUserRepository.save(extendedUser);
        return extendedUserMapper.toDto(extendedUser);
    }

    @Override
    public Optional<ExtendedUserDTO> partialUpdate(ExtendedUserDTO extendedUserDTO) {
        log.debug("Request to partially update ExtendedUser : {}", extendedUserDTO);

        return extendedUserRepository
            .findById(extendedUserDTO.getId())
            .map(existingExtendedUser -> {
                extendedUserMapper.partialUpdate(existingExtendedUser, extendedUserDTO);

                return existingExtendedUser;
            })
            .map(extendedUserRepository::save)
            .map(extendedUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtendedUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExtendedUsers");
        return extendedUserRepository.findAll(pageable).map(extendedUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtendedUserDTO> findOne(Long id) {
        log.debug("Request to get ExtendedUser : {}", id);
        return extendedUserRepository.findById(id).map(extendedUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExtendedUser : {}", id);
        Optional<ExtendedUser> extendedUserOptional = extendedUserRepository.findById(id);
        if (!extendedUserOptional.isPresent()) {
            return;
        }
        extendedUserRepository.deleteById(id);
        userRepository.deleteById(id);
    }
}
