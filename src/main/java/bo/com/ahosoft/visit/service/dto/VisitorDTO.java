package bo.com.ahosoft.visit.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bo.com.ahosoft.visit.domain.Visitor} entity.
 */
public class VisitorDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String documentNumber;

    private String business;

    private String position;

    private String imageName;


    private Long documentTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
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

        VisitorDTO visitorDTO = (VisitorDTO) o;
        if (visitorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visitorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VisitorDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", business='" + getBusiness() + "'" +
            ", position='" + getPosition() + "'" +
            ", imageName='" + getImageName() + "'" +
            ", documentTypeId=" + getDocumentTypeId() +
            "}";
    }
}
