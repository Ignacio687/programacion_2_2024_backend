package ar.edu.um.programacion2.jh.service.client;

import ar.edu.um.programacion2.jh.service.dto.SaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleRequestDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sale", url = "$(cliente-web.rootUrl)")
public interface SaleClient {
    @PostMapping("/api/catedra/vender")
    SaleDTO createSale(@RequestBody SaleRequestDTO saleRequest);

    @GetMapping("/api/catedra/ventas")
    List<SaleDTO> getSales();

    @GetMapping("/api/catedra/venta/{id}")
    SaleDTO getSaleById(@PathVariable("id") Long id);
}
