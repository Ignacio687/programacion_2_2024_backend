package ar.edu.um.programacion2.jh.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.service.MakeSaleService;
import ar.edu.um.programacion2.jh.service.dto.CompleteSaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleListDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleRequestDTO;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MakeSaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MakeSaleService makeSaleService;

    private SaleRequestDTO saleRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setSaleDate(Instant.now());
        saleRequestDTO.setOptions(new ArrayList<>());
        saleRequestDTO.setExtras(new ArrayList<>());
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(100.0);
    }

    @Test
    @WithMockUser
    void createSaleWithValidRequestShouldReturnCreated(CapturedOutput output) throws Exception {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(1L);
        when(makeSaleService.save(any(SaleRequestDTO.class))).thenReturn(saleDTO);

        mockMvc
            .perform(
                post("/api/mksale").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsBytes(saleRequestDTO))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(saleDTO.getId()));

        verify(makeSaleService, times(1)).save(eq(this.saleRequestDTO));
        assertThat(output).contains("REST request to perform Sale : " + saleRequestDTO);
    }

    @Test
    @WithMockUser
    void createSaleWithAssignedIDShouldNotReturnBadRequest(CapturedOutput output) throws Exception {
        this.saleRequestDTO.setId(1L);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(1L);
        when(makeSaleService.save(any(SaleRequestDTO.class))).thenReturn(saleDTO);

        mockMvc
            .perform(
                post("/api/mksale").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsBytes(saleRequestDTO))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(saleDTO.getId()));

        this.saleRequestDTO.setId(null);

        verify(makeSaleService, times(1)).save(eq(this.saleRequestDTO));
        assertThat(output).contains("REST request to perform Sale : " + saleRequestDTO);
    }

    @Test
    @WithMockUser
    void createSaleWithInvalidSaleRequestShouldReturnBadRequest(CapturedOutput output) throws Exception {
        doThrow(new InvalidSaleRequestException("Invalid sale request")).when(makeSaleService).save(eq(this.saleRequestDTO));

        mockMvc
            .perform(
                post("/api/mksale").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsBytes(saleRequestDTO))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.invalidsalerequest"));

        verify(makeSaleService, times(1)).save(eq(this.saleRequestDTO));
        assertThat(output).contains("Invalid sale request");
    }

    @Test
    @WithMockUser
    void getAllSalesShouldReturnOk(CapturedOutput output) throws Exception {
        Page<SaleListDTO> page = new PageImpl<>(List.of(new SaleListDTO()));
        when(makeSaleService.findAll(any(Pageable.class), eq(true))).thenReturn(page);

        mockMvc
            .perform(get("/api/mksale").param("local", "true").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isNotEmpty());

        verify(makeSaleService, times(1)).findAll(any(Pageable.class), eq(true));
        assertThat(output).contains("REST request to get a page of Sales");
    }

    @Test
    @WithMockUser
    void getAllSalesShouldReturnOkWhenLocalParamIsMissing(CapturedOutput output) throws Exception {
        Page<SaleListDTO> page = new PageImpl<>(List.of(new SaleListDTO()));
        when(makeSaleService.findAll(any(Pageable.class), eq(true))).thenReturn(page);

        mockMvc
            .perform(get("/api/mksale").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isNotEmpty());

        verify(makeSaleService, times(1)).findAll(any(Pageable.class), eq(true));
        assertThat(output).contains("REST request to get a page of Sales");
    }

    @Test
    @WithMockUser
    void getSaleWithValidIdShouldReturnOk(CapturedOutput output) throws Exception {
        CompleteSaleDTO completeSaleDTO = new CompleteSaleDTO();
        when(makeSaleService.findOne(eq(1L), eq(true))).thenReturn(Optional.of(completeSaleDTO));

        mockMvc
            .perform(get("/api/mksale/1").param("local", "true").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());

        verify(makeSaleService, times(1)).findOne(eq(1L), eq(true));
        assertThat(output).contains("REST request to get Sale : 1");
    }

    @Test
    @WithMockUser
    void getSaleWithInvalidIdShouldReturnNotFound(CapturedOutput output) throws Exception {
        when(makeSaleService.findOne(eq(1L), eq(true))).thenReturn(Optional.empty());

        mockMvc
            .perform(get("/api/mksale/1").param("local", "true").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(makeSaleService, times(1)).findOne(eq(1L), eq(true));
        assertThat(output).contains("REST request to get Sale : 1");
    }

    @Test
    @WithMockUser
    void getSaleShouldDefaultLocalParamToTrue(CapturedOutput output) throws Exception {
        CompleteSaleDTO completeSaleDTO = new CompleteSaleDTO();
        when(makeSaleService.findOne(eq(1L), eq(true))).thenReturn(Optional.of(completeSaleDTO));

        mockMvc
            .perform(get("/api/mksale/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());

        verify(makeSaleService, times(1)).findOne(eq(1L), eq(true));
        assertThat(output).contains("REST request to get Sale : 1");
    }
}
