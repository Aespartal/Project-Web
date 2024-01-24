package es.project.web.rest;

import es.project.domain.User;
import es.project.exception.InvalidPasswordException;
import es.project.service.ExtendedUserQueryService;
import es.project.service.ExtendedUserService;
import es.project.service.UserService;
import es.project.service.dto.ExtendedUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import java.util.Optional;

/**
 * REST controller for managing {@link es.project.domain.ExtendedUser}.
 */
@RestController
@RequestMapping("/api")
public class ExtendedUserResourceExt {

    private final Logger log = LoggerFactory.getLogger(ExtendedUserResourceExt.class);

    private static final String ENTITY_NAME = "extendedUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtendedUserService extendedUserService;

    private final ExtendedUserQueryService extendedUserQueryService;

    private final UserService userService;

    public ExtendedUserResourceExt(
        ExtendedUserService extendedUserService,
        ExtendedUserQueryService extendedUserQueryService,
        UserService userService) {
        this.extendedUserService = extendedUserService;
        this.userService = userService;
        this.extendedUserQueryService = extendedUserQueryService;
    }

      /**
         * {@code GET  /extended-users/me} : GET actual user
         *
         */
        @GetMapping("/extended-users/me")
        public ResponseEntity<ExtendedUserDTO> getActualUser() {
            log.debug("REST request to get actual user");
            User loggedInUser = userService.getUserWithAuthorities().orElseThrow(InvalidPasswordException::new);
            Optional<ExtendedUserDTO> agentDTO = extendedUserService.findOne(loggedInUser.getId());
            return ResponseUtil.wrapOrNotFound(agentDTO);
        }
}
