package es.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedUserMapperTest {

    private ExtendedUserMapper extendedUserMapper;

    @BeforeEach
    public void setUp() {
        extendedUserMapper = new ExtendedUserMapperImpl();
    }
}
