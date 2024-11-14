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
 * A Characteristic.
 */
@Entity
@Table(name = "characteristic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Characteristic implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "characteristics")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sales", "characteristics", "options", "extras" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Characteristic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierForeignId() {
        return this.supplierForeignId;
    }

    public Characteristic supplierForeignId(Long supplierForeignId) {
        this.setSupplierForeignId(supplierForeignId);
        return this;
    }

    public void setSupplierForeignId(Long supplierForeignId) {
        this.supplierForeignId = supplierForeignId;
    }

    public String getName() {
        return this.name;
    }

    public Characteristic name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Characteristic description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.removeCharacteristics(this));
        }
        if (devices != null) {
            devices.forEach(i -> i.addCharacteristics(this));
        }
        this.devices = devices;
    }

    public Characteristic devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public Characteristic addDevices(Device device) {
        this.devices.add(device);
        device.getCharacteristics().add(this);
        return this;
    }

    public Characteristic removeDevices(Device device) {
        this.devices.remove(device);
        device.getCharacteristics().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Characteristic)) {
            return false;
        }
        return getId() != null && getId().equals(((Characteristic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Characteristic{" +
            "id=" + getId() +
            ", supplierForeignId=" + getSupplierForeignId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
