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

/**
 * Criteria class for the {@link bo.com.ahosoft.visit.domain.Visitor} entity. This class is used
 * in {@link bo.com.ahosoft.visit.web.rest.VisitorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /visitors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VisitorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter documentNumber;

    private StringFilter business;

    private StringFilter position;

    private StringFilter imageName;

    private LongFilter documentTypeId;

    public VisitorCriteria() {
    }

    public VisitorCriteria(VisitorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.documentNumber = other.documentNumber == null ? null : other.documentNumber.copy();
        this.business = other.business == null ? null : other.business.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.imageName = other.imageName == null ? null : other.imageName.copy();
        this.documentTypeId = other.documentTypeId == null ? null : other.documentTypeId.copy();
    }

    @Override
    public VisitorCriteria copy() {
        return new VisitorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(StringFilter documentNumber) {
        this.documentNumber = documentNumber;
    }

    public StringFilter getBusiness() {
        return business;
    }

    public void setBusiness(StringFilter business) {
        this.business = business;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getImageName() {
        return imageName;
    }

    public void setImageName(StringFilter imageName) {
        this.imageName = imageName;
    }

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VisitorCriteria that = (VisitorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(documentNumber, that.documentNumber) &&
            Objects.equals(business, that.business) &&
            Objects.equals(position, that.position) &&
            Objects.equals(imageName, that.imageName) &&
            Objects.equals(documentTypeId, that.documentTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        documentNumber,
        business,
        position,
        imageName,
        documentTypeId
        );
    }

    @Override
    public String toString() {
        return "VisitorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (documentNumber != null ? "documentNumber=" + documentNumber + ", " : "") +
                (business != null ? "business=" + business + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (imageName != null ? "imageName=" + imageName + ", " : "") +
                (documentTypeId != null ? "documentTypeId=" + documentTypeId + ", " : "") +
            "}";
    }

}
