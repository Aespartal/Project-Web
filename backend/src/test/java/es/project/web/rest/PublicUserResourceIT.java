package es.project.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.project.IntegrationTest;
import es.project.domain.User;
import es.project.repository.UserRepository;
import es.project.security.AuthoritiesConstants;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class PublicUserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.initTestUser(userRepository, em);
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        restUserMockMvc
            .perform(get("/api/authorities").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasItems(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)));
    }

}
