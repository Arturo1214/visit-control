package bo.com.ahosoft.visit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Visitor.
 */
@Entity
@Table(name = "visitor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "business")
    private String business;

    @Column(name = "position")
    private String position;

    @Column(name = "image_name")
    private String imageName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("visitors")
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Visitor fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Visitor documentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBusiness() {
        return business;
    }

    public Visitor business(String business) {
        this.business = business;
        return this;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getPosition() {
        return position;
    }

    public Visitor position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImageName() {
        return imageName;
    }

    public Visitor imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public Visitor documentType(DocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visitor)) {
            return false;
        }
        return id != null && id.equals(((Visitor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Visitor{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", business='" + getBusiness() + "'" +
            ", position='" + getPosition() + "'" +
            ", imageName='" + getImageName() + "'" +
            "}";
    }
}
