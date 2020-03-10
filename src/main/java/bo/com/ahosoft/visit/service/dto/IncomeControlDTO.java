package bo.com.ahosoft.visit.service.dto;

import bo.com.ahosoft.visit.domain.IncomeControl;
import bo.com.ahosoft.visit.domain.Visitor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bo.com.ahosoft.visit.domain.IncomeControl} entity.
 */
@Getter @Setter
public class IncomeControlDTO implements Serializable {

    private Long id;

    @NotNull
    private String reason;

    @NotNull
    private String place;

    @NotNull
    private String answerable;

    private Visitor visitor;

    public IncomeControl toIncomeControl() {
        IncomeControl incomeControl = new IncomeControl();
        incomeControl.setId(id);
        incomeControl.setReason(reason);
        incomeControl.setPlace(place);
        incomeControl.setAnswerable(answerable);
        incomeControl.setVisitor(visitor);
        return incomeControl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomeControlDTO incomeControlDTO = (IncomeControlDTO) o;
        if (incomeControlDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), incomeControlDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IncomeControlDTO{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", place='" + getPlace() + "'" +
            ", responsable='" + getAnswerable() + "'" +
            ", visitor=" + getVisitor() +
            "}";
    }
}
