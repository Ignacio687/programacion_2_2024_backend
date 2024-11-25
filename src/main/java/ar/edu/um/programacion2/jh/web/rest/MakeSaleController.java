package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.service.MakeSaleService;
import ar.edu.um.programacion2.jh.service.dto.CompleteSaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleListDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleRequestDTO;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import ar.edu.um.programacion2.jh.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/mksale")
public class MakeSaleController {

    private static final Logger LOG = LoggerFactory.getLogger(MakeSaleController.class);

    private static final String ENTITY_NAME = "sale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MakeSaleService makeSaleService;

    public MakeSaleController(MakeSaleService makeSaleService) {
        this.makeSaleService = makeSaleService;
    }

    @PostMapping("")
    public ResponseEntity<Sale> createSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO) throws URISyntaxException {
        LOG.debug("REST request to perform Sale : {}", saleRequestDTO);
        if (saleRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new sale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sale sale;
        try {
            sale = makeSaleService.save(saleRequestDTO);
        } catch (InvalidSaleRequestException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidsalerequest");
        }
        return ResponseEntity.created(new URI("/api/mksale/" + sale.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sale.getId().toString()))
            .body(sale);
    }

    @GetMapping("")
    public ResponseEntity<List<SaleListDTO>> getAllSales(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "local", required = false) Boolean local
    ) {
        LOG.debug("REST request to get a page of Sales");
        Page<SaleListDTO> page = makeSaleService.findAll(pageable, local);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompleteSaleDTO> getSale(
        @PathVariable("id") Long id,
        @RequestParam(value = "local", required = false) Boolean local
    ) {
        LOG.debug("REST request to get Sale : {}", id);
        Optional<CompleteSaleDTO> sale = makeSaleService.findOne(id, local);
        return ResponseUtil.wrapOrNotFound(sale);
    }
}
