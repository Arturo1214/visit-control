package bo.com.ahosoft.visit.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VisitorMapperTest {

    private VisitorMapper visitorMapper;

    @BeforeEach
    public void setUp() {
        visitorMapper = new VisitorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(visitorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(visitorMapper.fromId(null)).isNull();
    }
}
