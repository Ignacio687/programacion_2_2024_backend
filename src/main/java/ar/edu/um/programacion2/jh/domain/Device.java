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
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "supplier_foreign_id", nullable = false)
    private Long supplierForeignId;

    @NotNull
    @Column(name = "supplier", nullable = false)
    private String supplier;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "currency")
    private String currency;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "saleItems", "device" }, allowSetters = true)
    private Set<Sale> sales = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_device__characteristics",
        joinColumns = @JoinColumn(name = "device_id"),
        inverseJoinColumns = @JoinColumn(name = "characteristics_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices" }, allowSetters = true)
    private Set<Characteristic> characteristics = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_device__options",
        joinColumns = @JoinColumn(name = "device_id"),
        inverseJoinColumns = @JoinColumn(name = "options_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customization", "devices" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_device__extras",
        joinColumns = @JoinColumn(name = "device_id"),
        inverseJoinColumns = @JoinColumn(name = "extras_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices" }, allowSetters = true)
    private Set<Extra> extras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierForeignId() {
        return this.supplierForeignId;
    }

    public Device supplierForeignId(Long supplierForeignId) {
        this.setSupplierForeignId(supplierForeignId);
        return this;
    }

    public void setSupplierForeignId(Long supplierForeignId) {
        this.supplierForeignId = supplierForeignId;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Device supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCode() {
        return this.code;
    }

    public Device code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Device name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Device description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return this.basePrice;
    }

    public Device basePrice(Double basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Device currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Device active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Sale> getSales() {
        return this.sales;
    }

    public void setSales(Set<Sale> sales) {
        if (this.sales != null) {
            this.sales.forEach(i -> i.setDevice(null));
        }
        if (sales != null) {
            sales.forEach(i -> i.setDevice(this));
        }
        this.sales = sales;
    }

    public Device sales(Set<Sale> sales) {
        this.setSales(sales);
        return this;
    }

    public Device addSales(Sale sale) {
        this.sales.add(sale);
        sale.setDevice(this);
        return this;
    }

    public Device removeSales(Sale sale) {
        this.sales.remove(sale);
        sale.setDevice(null);
        return this;
    }

    public Set<Characteristic> getCharacteristics() {
        return this.characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public Device characteristics(Set<Characteristic> characteristics) {
        this.setCharacteristics(characteristics);
        return this;
    }

    public Device addCharacteristics(Characteristic characteristic) {
        this.characteristics.add(characteristic);
        return this;
    }

    public Device removeCharacteristics(Characteristic characteristic) {
        this.characteristics.remove(characteristic);
        return this;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Device options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public Device addOptions(Option option) {
        this.options.add(option);
        return this;
    }

    public Device removeOptions(Option option) {
        this.options.remove(option);
        return this;
    }

    public Set<Extra> getExtras() {
        return this.extras;
    }

    public void setExtras(Set<Extra> extras) {
        this.extras = extras;
    }

    public Device extras(Set<Extra> extras) {
        this.setExtras(extras);
        return this;
    }

    public Device addExtras(Extra extra) {
        this.extras.add(extra);
        return this;
    }

    public Device removeExtras(Extra extra) {
        this.extras.remove(extra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return getId() != null && getId().equals(((Device) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", supplierForeignId=" + getSupplierForeignId() +
            ", supplier='" + getSupplier() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", basePrice=" + getBasePrice() +
            ", currency='" + getCurrency() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
