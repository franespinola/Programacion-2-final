package fran.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdenesMapperTest {

    private OrdenesMapper ordenesMapper;

    @BeforeEach
    public void setUp() {
        ordenesMapper = new OrdenesMapperImpl();
    }
}
