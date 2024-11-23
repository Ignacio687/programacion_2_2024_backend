package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.DeviceSynchronizationService;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.dto.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DeviceSynchronizationServiceImplTest {

    @Autowired
    private DeviceClient deviceClient;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private ExtraRepository extraRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CustomizationRepository customizationRepository;

    @Autowired
    private DeviceSynchronizationService deviceSynchronizationService;

    @Mock
    private Logger log = LoggerFactory.getLogger(DeviceSynchronizationService.class);

    private Device exampleDevice;

    private DeviceDTO exampleDeviceDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.deviceClient = spy(this.deviceClient);
        this.deviceRepository = spy(this.deviceRepository);
        this.characteristicRepository = spy(this.characteristicRepository);
        this.extraRepository = spy(this.extraRepository);
        this.optionRepository = spy(this.optionRepository);
        this.customizationRepository = spy(this.customizationRepository);
        this.deviceSynchronizationService = spy(this.deviceSynchronizationService);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "log", log);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "deviceClient", this.deviceClient);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "deviceRepository", this.deviceRepository);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "characteristicRepository", this.characteristicRepository);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "extraRepository", this.extraRepository);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "optionRepository", this.optionRepository);
        ReflectionTestUtils.setField(this.deviceSynchronizationService, "customizationRepository", this.customizationRepository);

        // Define example device
        this.exampleDevice = new Device();
        this.exampleDevice.setSupplierForeignId(101L);
        this.exampleDevice.setSupplier("Catedra");
        this.exampleDevice.setCode("NTB01");
        this.exampleDevice.setName("Notebook 1");
        this.exampleDevice.setDescription("Descripción detallada de la Notebook 1");
        this.exampleDevice.setBasePrice(2000.0);
        this.exampleDevice.setCurrency("USD");
        this.exampleDevice.setActive(true);

        Characteristic characteristic1 = new Characteristic();
        characteristic1.setSupplierForeignId(101L);
        characteristic1.setName("Characteristic 1");
        characteristic1.setDescription("Descripción de la Characteristic 1");

        Characteristic characteristic2 = new Characteristic();
        characteristic2.setSupplierForeignId(102L);
        characteristic2.setName("Characteristic 2");
        characteristic2.setDescription("Descripción de la Characteristic 2");

        this.exampleDevice.setCharacteristics(
                new HashSet<>(this.characteristicRepository.saveAll(Arrays.asList(characteristic1, characteristic2)))
            );

        Option option1 = new Option();
        option1.setSupplierForeignId(101L);
        option1.setName("Option 1");
        option1.setDescription("Descripción de la Option 1");
        option1.setCode("OPT01");
        option1.setAdditionalPrice(100.0);

        Option option2 = new Option();
        option2.setSupplierForeignId(102L);
        option2.setName("Option 2");
        option2.setDescription("Descripción de la Option 2");
        option2.setCode("OPT02");
        option2.setAdditionalPrice(150.0);

        Option option3 = new Option();
        option3.setSupplierForeignId(103L);
        option3.setName("Option 3");
        option3.setDescription("Descripción de la Option 3");
        option3.setCode("OPT03");
        option3.setAdditionalPrice(200.0);

        Option option4 = new Option();
        option4.setSupplierForeignId(104L);
        option4.setName("Option 4");
        option4.setDescription("Descripción de la Option 4");
        option4.setCode("OPT04");
        option4.setAdditionalPrice(250.0);

        this.exampleDevice.setOptions(new HashSet<>(this.optionRepository.saveAll(Arrays.asList(option1, option2, option3, option4))));

        Extra extra1 = new Extra();
        extra1.setSupplierForeignId(101L);
        extra1.setName("Extra 1");
        extra1.setPrice(50.0);
        extra1.setFreePrice(10.0);
        extra1.setDescription("Descripción de la Extra 1");

        Extra extra2 = new Extra();
        extra2.setSupplierForeignId(102L);
        extra2.setName("Extra 2");
        extra2.setPrice(75.0);
        extra2.setFreePrice(20.0);
        extra2.setDescription("Descripción de la Extra 2");

        this.exampleDevice.setExtras(new HashSet<>(this.extraRepository.saveAll(Arrays.asList(extra1, extra2))));

        Customization customization1 = new Customization();
        customization1.setName("Customization 1");
        customization1.setSupplierForeignId(101L);
        customization1.setOptions(
            this.exampleDevice.getOptions()
                .stream()
                .filter(option -> option.getSupplierForeignId() == 101L || option.getSupplierForeignId() == 103L)
                .collect(Collectors.toSet())
        );

        Customization customization2 = new Customization();
        customization2.setName("Customization 2");
        customization2.setSupplierForeignId(102L);
        customization2.setOptions(
            new HashSet<>(
                this.exampleDevice.getOptions()
                    .stream()
                    .filter(option -> option.getSupplierForeignId() == 102L || option.getSupplierForeignId() == 104L)
                    .collect(Collectors.toSet())
            )
        );

        this.customizationRepository.saveAll(Arrays.asList(customization1, customization2));
        this.exampleDevice = this.deviceRepository.save(this.exampleDevice);

        // Define example device DTO
        this.exampleDeviceDTO = new DeviceDTO();
        this.exampleDeviceDTO.setId(101L);
        this.exampleDeviceDTO.setCode("NTB01");
        this.exampleDeviceDTO.setName("Notebook 1");
        this.exampleDeviceDTO.setDescription("Descripción detallada de la Notebook 1");
        this.exampleDeviceDTO.setBasePrice(2000.0);
        this.exampleDeviceDTO.setCurrency("USD");

        CharacteristicDTO characteristicDTO1 = new CharacteristicDTO();
        characteristicDTO1.setId(101L);
        characteristicDTO1.setName("Characteristic 1");
        characteristicDTO1.setDescription("Descripción de la Characteristic 1");

        CharacteristicDTO characteristicDTO2 = new CharacteristicDTO();
        characteristicDTO2.setId(102L);
        characteristicDTO2.setName("Characteristic 2");
        characteristicDTO2.setDescription("Descripción de la Characteristic 2");

        this.exampleDeviceDTO.setCharacteristics(Arrays.asList(characteristicDTO1, characteristicDTO2));

        ExtraDTO extraDTO1 = new ExtraDTO();
        extraDTO1.setId(101L);
        extraDTO1.setName("Extra 1");
        extraDTO1.setPrice(50.0);
        extraDTO1.setFreePrice(10.0);
        extraDTO1.setDescription("Descripción de la Extra 1");

        ExtraDTO extraDTO2 = new ExtraDTO();
        extraDTO2.setId(102L);
        extraDTO2.setName("Extra 2");
        extraDTO2.setPrice(75.0);
        extraDTO2.setFreePrice(20.0);
        extraDTO2.setDescription("Descripción de la Extra 2");

        this.exampleDeviceDTO.setExtras(Arrays.asList(extraDTO1, extraDTO2));

        OptionDTO optionDTO1 = new OptionDTO();
        optionDTO1.setId(101L);
        optionDTO1.setName("Option 1");
        optionDTO1.setDescription("Descripción de la Option 1");
        optionDTO1.setCode("OPT01");
        optionDTO1.setAdditionalPrice(100.0);

        OptionDTO optionDTO2 = new OptionDTO();
        optionDTO2.setId(102L);
        optionDTO2.setName("Option 2");
        optionDTO2.setDescription("Descripción de la Option 2");
        optionDTO2.setCode("OPT02");
        optionDTO2.setAdditionalPrice(150.0);

        OptionDTO optionDTO3 = new OptionDTO();
        optionDTO3.setId(103L);
        optionDTO3.setName("Option 3");
        optionDTO3.setDescription("Descripción de la Option 3");
        optionDTO3.setCode("OPT03");
        optionDTO3.setAdditionalPrice(200.0);

        OptionDTO optionDTO4 = new OptionDTO();
        optionDTO4.setId(104L);
        optionDTO4.setName("Option 4");
        optionDTO4.setDescription("Descripción de la Option 4");
        optionDTO4.setCode("OPT04");
        optionDTO4.setAdditionalPrice(250.0);

        CustomizationDTO customizationDTO1 = new CustomizationDTO();
        customizationDTO1.setId(101L);
        customizationDTO1.setName("Customization 1");
        customizationDTO1.setOptions(Arrays.asList(optionDTO1, optionDTO3));

        CustomizationDTO customizationDTO2 = new CustomizationDTO();
        customizationDTO2.setId(102L);
        customizationDTO2.setName("Customization 2");
        customizationDTO2.setOptions(Arrays.asList(optionDTO2, optionDTO4));

        this.exampleDeviceDTO.setCustomizations(Arrays.asList(customizationDTO1, customizationDTO2));
    }

    @Test
    void testSynchronizeNoChanges() {
        when(this.deviceClient.getDevices()).thenReturn(Collections.singletonList(this.exampleDeviceDTO));
        deviceSynchronizationService.synchronize();
        List<Device> allDevicesAfter = deviceRepository.findAll();
        Assertions.assertEquals(1, allDevicesAfter.size());
        verifyNoInteractions(deviceRepository, characteristicRepository, extraRepository, optionRepository, customizationRepository);
        verify(log).info("Synchronizing devices...");
        verify(log).info("No changes detected between local and external devices.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSynchronizeUpdateDevices() {}

    @Test
    void testSynchronizeDeactivateDevices() {}

    @Test
    void testSynchronizeCreateNewDevices() {}

    @Test
    void testSynchronizeWithChanges() {}
}
