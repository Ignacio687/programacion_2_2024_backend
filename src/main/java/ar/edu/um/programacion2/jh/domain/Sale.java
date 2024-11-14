package ar.edu.um.programacion2.jh.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_foreign_id")
    private Long supplierForeignId;

    @NotNull
    @Column(name = "device_price", nullable = false)
    private Double devicePrice;

    @NotNull
    @Column(name = "final_price", nullable = false)
    private Double finalPrice;

    @NotNull
    @Column(name = "sale_date", nullable = false)
    private Instant saleDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "finalized")
    private Boolean finalized;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "option", "extra", "sale" }, allowSetters = true)
    private Set<SaleItem> saleItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sales", "characteristics", "options", "extras" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierForeignId() {
        return this.supplierForeignId;
    }

    public Sale supplierForeignId(Long supplierForeignId) {
        this.setSupplierForeignId(supplierForeignId);
        return this;
    }

    public void setSupplierForeignId(Long supplierForeignId) {
        this.supplierForeignId = supplierForeignId;
    }

    public Double getDevicePrice() {
        return this.devicePrice;
    }

    public Sale devicePrice(Double devicePrice) {
        this.setDevicePrice(devicePrice);
        return this;
    }

    public void setDevicePrice(Double devicePrice) {
        this.devicePrice = devicePrice;
    }

    public Double getFinalPrice() {
        return this.finalPrice;
    }

    public Sale finalPrice(Double finalPrice) {
        this.setFinalPrice(finalPrice);
        return this;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Instant getSaleDate() {
        return this.saleDate;
    }

    public Sale saleDate(Instant saleDate) {
        this.setSaleDate(saleDate);
        return this;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Sale currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getFinalized() {
        return this.finalized;
    }

    public Sale finalized(Boolean finalized) {
        this.setFinalized(finalized);
        return this;
    }

    public void setFinalized(Boolean finalized) {
        this.finalized = finalized;
    }

    public Set<SaleItem> getSaleItems() {
        return this.saleItems;
    }

    public void setSaleItems(Set<SaleItem> saleItems) {
        if (this.saleItems != null) {
            this.saleItems.forEach(i -> i.setSale(null));
        }
        if (saleItems != null) {
            saleItems.forEach(i -> i.setSale(this));
        }
        this.saleItems = saleItems;
    }

    public Sale saleItems(Set<SaleItem> saleItems) {
        this.setSaleItems(saleItems);
        return this;
    }

    public Sale addSaleItems(SaleItem saleItem) {
        this.saleItems.add(saleItem);
        saleItem.setSale(this);
        return this;
    }

    public Sale removeSaleItems(SaleItem saleItem) {
        this.saleItems.remove(saleItem);
        saleItem.setSale(null);
        return this;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Sale device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sale)) {
            return false;
        }
        return getId() != null && getId().equals(((Sale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sale{" +
            "id=" + getId() +
            ", supplierForeignId=" + getSupplierForeignId() +
            ", devicePrice=" + getDevicePrice() +
            ", finalPrice=" + getFinalPrice() +
            ", saleDate='" + getSaleDate() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", finalized='" + getFinalized() + "'" +
            "}";
    }
}
