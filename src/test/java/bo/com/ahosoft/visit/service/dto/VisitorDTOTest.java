package bo.com.ahosoft.visit.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bo.com.ahosoft.visit.web.rest.TestUtil;

public class VisitorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitorDTO.class);
        VisitorDTO visitorDTO1 = new VisitorDTO();
        visitorDTO1.setId(1L);
        VisitorDTO visitorDTO2 = new VisitorDTO();
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
        visitorDTO2.setId(visitorDTO1.getId());
        assertThat(visitorDTO1).isEqualTo(visitorDTO2);
        visitorDTO2.setId(2L);
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
        visitorDTO1.setId(null);
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
    }
}
