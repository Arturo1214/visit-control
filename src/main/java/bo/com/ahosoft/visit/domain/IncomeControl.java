package bo.com.ahosoft.visit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A IncomeControl.
 */
@Entity
@Table(name = "income_control")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IncomeControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotNull
    @Column(name = "place", nullable = false)
    private String place;

    @NotNull
    @Column(name = "answerable", nullable = false)
    private String answerable;

    @NotNull
    @Column(name = "admission_date", nullable = false)
    private ZonedDateTime admissionDate;

    @Column(name = "departure_date")
    private ZonedDateTime departureDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("incomeControls")
    private Visitor visitor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public IncomeControl reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPlace() {
        return place;
    }

    public IncomeControl place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAnswerable() {
        return answerable;
    }

    public IncomeControl answerable(String answerable) {
        this.answerable = answerable;
        return this;
    }

    public void setAnswerable(String answerable) {
        this.answerable = answerable;
    }

    public ZonedDateTime getAdmissionDate() {
        return admissionDate;
    }

    public IncomeControl admissionDate(ZonedDateTime admissionDate) {
        this.admissionDate = admissionDate;
        return this;
    }

    public void setAdmissionDate(ZonedDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public IncomeControl departureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public IncomeControl visitor(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomeControl)) {
            return false;
        }
        return id != null && id.equals(((IncomeControl) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IncomeControl{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", place='" + getPlace() + "'" +
            ", answerable='" + getAnswerable() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", departureDate='" + getDepartureDate() + "'" +
            "}";
    }
}
