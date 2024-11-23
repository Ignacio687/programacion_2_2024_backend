package ar.edu.um.programacion2.jh.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Option.
 */
@Entity
@Table(name = "jhi_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "supplier_foreign_id", nullable = false)
    private Long supplierForeignId;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "additional_price", nullable = false)
    private Double additionalPrice;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "options")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "options", "devices" }, allowSetters = true)
    private Set<Customization> customizations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Option id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierForeignId() {
        return this.supplierForeignId;
    }

    public Option supplierForeignId(Long supplierForeignId) {
        this.setSupplierForeignId(supplierForeignId);
        return this;
    }

    public void setSupplierForeignId(Long supplierForeignId) {
        this.supplierForeignId = supplierForeignId;
    }

    public String getCode() {
        return this.code;
    }

    public Option code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Option name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Option description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAdditionalPrice() {
        return this.additionalPrice;
    }

    public Option additionalPrice(Double additionalPrice) {
        this.setAdditionalPrice(additionalPrice);
        return this;
    }

    public void setAdditionalPrice(Double additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public Set<Customization> getCustomizations() {
        return this.customizations;
    }

    public void setCustomizations(Set<Customization> customizations) {
        if (this.customizations != null) {
            this.customizations.forEach(i -> i.removeOptions(this));
        }
        if (customizations != null) {
            customizations.forEach(i -> i.addOptions(this));
        }
        this.customizations = customizations;
    }

    public Option customizations(Set<Customization> customizations) {
        this.setCustomizations(customizations);
        return this;
    }

    public Option addCustomization(Customization customization) {
        this.customizations.add(customization);
        customization.getOptions().add(this);
        return this;
    }

    public Option removeCustomization(Customization customization) {
        this.customizations.remove(customization);
        customization.getOptions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return getId() != null && getId().equals(((Option) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", supplierForeignId=" + getSupplierForeignId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", additionalPrice=" + getAdditionalPrice() +
            "}";
    }
}
