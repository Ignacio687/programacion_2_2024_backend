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
 * A Extra.
 */
@Entity
@Table(name = "extra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Extra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "supplier_foreign_id", nullable = false)
    private Long supplierForeignId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "free_price", nullable = false)
    private Double freePrice;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "extras")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sales", "characteristics", "options", "extras" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Extra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierForeignId() {
        return this.supplierForeignId;
    }

    public Extra supplierForeignId(Long supplierForeignId) {
        this.setSupplierForeignId(supplierForeignId);
        return this;
    }

    public void setSupplierForeignId(Long supplierForeignId) {
        this.supplierForeignId = supplierForeignId;
    }

    public String getName() {
        return this.name;
    }

    public Extra name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Extra description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public Extra price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFreePrice() {
        return this.freePrice;
    }

    public Extra freePrice(Double freePrice) {
        this.setFreePrice(freePrice);
        return this;
    }

    public void setFreePrice(Double freePrice) {
        this.freePrice = freePrice;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.removeExtras(this));
        }
        if (devices != null) {
            devices.forEach(i -> i.addExtras(this));
        }
        this.devices = devices;
    }

    public Extra devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public Extra addDevices(Device device) {
        this.devices.add(device);
        device.getExtras().add(this);
        return this;
    }

    public Extra removeDevices(Device device) {
        this.devices.remove(device);
        device.getExtras().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extra)) {
            return false;
        }
        return getId() != null && getId().equals(((Extra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Extra{" +
            "id=" + getId() +
            ", supplierForeignId=" + getSupplierForeignId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", freePrice=" + getFreePrice() +
            "}";
    }
}
