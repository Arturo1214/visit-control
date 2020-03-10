package bo.com.ahosoft.visit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link bo.com.ahosoft.visit.domain.IncomeControl} entity. This class is used
 * in {@link bo.com.ahosoft.visit.web.rest.IncomeControlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /income-controls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IncomeControlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reason;

    private StringFilter place;

    private StringFilter answerable;

    private ZonedDateTimeFilter admissionDate;

    private ZonedDateTimeFilter departureDate;

    private LongFilter visitorId;

    public IncomeControlCriteria() {
    }

    public IncomeControlCriteria(IncomeControlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.place = other.place == null ? null : other.place.copy();
        this.answerable = other.answerable == null ? null : other.answerable.copy();
        this.admissionDate = other.admissionDate == null ? null : other.admissionDate.copy();
        this.departureDate = other.departureDate == null ? null : other.departureDate.copy();
        this.visitorId = other.visitorId == null ? null : other.visitorId.copy();
    }

    @Override
    public IncomeControlCriteria copy() {
        return new IncomeControlCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReason() {
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public StringFilter getPlace() {
        return place;
    }

    public void setPlace(StringFilter place) {
        this.place = place;
    }

    public StringFilter getAnswerable() {
        return answerable;
    }

    public void setAnswerable(StringFilter answerable) {
        this.answerable = answerable;
    }

    public ZonedDateTimeFilter getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(ZonedDateTimeFilter admissionDate) {
        this.admissionDate = admissionDate;
    }

    public ZonedDateTimeFilter getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(ZonedDateTimeFilter departureDate) {
        this.departureDate = departureDate;
    }

    public LongFilter getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(LongFilter visitorId) {
        this.visitorId = visitorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IncomeControlCriteria that = (IncomeControlCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(place, that.place) &&
            Objects.equals(answerable, that.answerable) &&
            Objects.equals(admissionDate, that.admissionDate) &&
            Objects.equals(departureDate, that.departureDate) &&
            Objects.equals(visitorId, that.visitorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reason,
        place,
        answerable,
        admissionDate,
        departureDate,
        visitorId
        );
    }

    @Override
    public String toString() {
        return "IncomeControlCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reason != null ? "reason=" + reason + ", " : "") +
                (place != null ? "place=" + place + ", " : "") +
                (answerable != null ? "answerable=" + answerable + ", " : "") +
                (admissionDate != null ? "admissionDate=" + admissionDate + ", " : "") +
                (departureDate != null ? "departureDate=" + departureDate + ", " : "") +
                (visitorId != null ? "visitorId=" + visitorId + ", " : "") +
            "}";
    }

}
