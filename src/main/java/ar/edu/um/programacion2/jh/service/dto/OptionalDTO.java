package ar.edu.um.programacion2.jh.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface OptionalDTO {
    Long getId();

    void setId(Long id);

    Long getSupplierForeignId();

    void setSupplierForeignId(Long supplierForeignId);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);
}
