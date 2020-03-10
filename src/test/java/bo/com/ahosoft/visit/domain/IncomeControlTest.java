package bo.com.ahosoft.visit.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bo.com.ahosoft.visit.web.rest.TestUtil;

public class IncomeControlTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeControl.class);
        IncomeControl incomeControl1 = new IncomeControl();
        incomeControl1.setId(1L);
        IncomeControl incomeControl2 = new IncomeControl();
        incomeControl2.setId(incomeControl1.getId());
        assertThat(incomeControl1).isEqualTo(incomeControl2);
        incomeControl2.setId(2L);
        assertThat(incomeControl1).isNotEqualTo(incomeControl2);
        incomeControl1.setId(null);
        assertThat(incomeControl1).isNotEqualTo(incomeControl2);
    }
}
