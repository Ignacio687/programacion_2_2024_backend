package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.service.dto.CompleteSaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleListDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleRequestDTO;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MakeSaleService {
    Sale save(SaleRequestDTO saleRequestDTO) throws InvalidSaleRequestException;
    Page<SaleListDTO> findAll(Pageable pageable, boolean local);
    Optional<CompleteSaleDTO> findOne(Long id, boolean local);
    Boolean verifyValidSale(SaleRequestDTO saleRequestDTO);
}
