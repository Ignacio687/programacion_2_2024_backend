package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.dto.*;
import ar.edu.um.programacion2.jh.service.impl.utilities.LocalToExternalObjectUpdaterImpl;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class LocalToExternalObjectUpdaterImplTest {

    private DeviceRepository deviceRepository;
    private CharacteristicRepository characteristicRepository;
    private ExtraRepository extraRepository;
    private OptionRepository optionRepository;
    private CustomizationRepository customizationRepository;

    private LocalToExternalObjectUpdaterImpl localToExternalObjectUpdater;

    private MockedStatic<DeviceDTO> deviceDTOMockedStatic;

    @BeforeEach
    void setUp() {
        this.deviceRepository = mock(DeviceRepository.class);
        this.characteristicRepository = mock(CharacteristicRepository.class);
        this.extraRepository = mock(ExtraRepository.class);
        this.optionRepository = mock(OptionRepository.class);
        this.customizationRepository = mock(CustomizationRepository.class);
        this.localToExternalObjectUpdater = spy(
            new LocalToExternalObjectUpdaterImpl(
                deviceRepository,
                characteristicRepository,
                extraRepository,
                optionRepository,
                customizationRepository
            )
        );
        deviceDTOMockedStatic = mockStatic(DeviceDTO.class);
    }

    @AfterEach
    void tearDown() {
        deviceDTOMockedStatic.close();
    }

    @Test
    void testUpdateAndSaveDeviceNewDevice() {
        DeviceDTO externalDevice = new DeviceDTO();
        externalDevice.setId(101L);
        String supplier = "Supplier1";

        when(deviceRepository.findBySupplierForeignId(101L)).thenReturn(Optional.empty());
        doReturn(new Device()).when(localToExternalObjectUpdater).updateAndSaveDevice(any(Device.class), eq(externalDevice));

        Device result = localToExternalObjectUpdater.updateAndSaveDevice(externalDevice, supplier);

        assertNotNull(result);
        verify(deviceRepository).findBySupplierForeignId(101L);
        ArgumentCaptor<Device> deviceCaptor = ArgumentCaptor.forClass(Device.class);
        verify(localToExternalObjectUpdater).updateAndSaveDevice(deviceCaptor.capture(), eq(externalDevice));
        Device capturedDevice = deviceCaptor.getValue();
        assertNull(capturedDevice.getId());
        assertEquals(101L, capturedDevice.getSupplierForeignId());
        assertEquals("Supplier1", capturedDevice.getSupplier());
    }

    @Test
    void testUpdateAndSaveDeviceExistingDevice() {
        DeviceDTO externalDevice = new DeviceDTO();
        externalDevice.setId(101L);
        Device localDevice = new Device();
        localDevice.setSupplierForeignId(101L);

        when(deviceRepository.findBySupplierForeignId(101L)).thenReturn(Optional.of(localDevice));
        doReturn(localDevice).when(localToExternalObjectUpdater).updateAndSaveDevice(localDevice, externalDevice);

        Device result = localToExternalObjectUpdater.updateAndSaveDevice(externalDevice, "Supplier1");

        assertSame(result, localDevice);
        verify(deviceRepository).findBySupplierForeignId(101L);
        verify(localToExternalObjectUpdater).updateAndSaveDevice(localDevice, externalDevice);
    }

    @Test
    void testUpdateAndSaveDeviceWithLocalDevice() {
        Device localDevice = new Device();
        localDevice.setId(1L);
        localDevice.setSupplierForeignId(101L);
        localDevice.setSupplier("Supplier1");

        DeviceDTO externalDevice = new DeviceDTO();
        externalDevice.setId(101L);

        Device mockedDevice = new Device();
        mockedDevice.setCustomizations(new HashSet<>(Arrays.asList(new Customization(), new Customization())));
        mockedDevice.setExtras(new HashSet<>(Arrays.asList(new Extra(), new Extra())));
        mockedDevice.setCharacteristics(new HashSet<>(Arrays.asList(new Characteristic(), new Characteristic())));

        Customization customizationTest = new Customization();
        Extra extraTest = new Extra();
        Characteristic characteristicTest = new Characteristic();

        when(deviceRepository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(characteristicRepository.save(any(Characteristic.class))).thenAnswer(invocation -> characteristicTest);
        when(extraRepository.save(any(Extra.class))).thenAnswer(invocation -> extraTest);
        when(customizationRepository.save(any(Customization.class))).thenAnswer(invocation -> customizationTest);
        deviceDTOMockedStatic.when(() -> DeviceDTO.toDevice(any(DeviceDTO.class))).thenReturn(mockedDevice);

        Device resultDevice = localToExternalObjectUpdater.updateAndSaveDevice(localDevice, externalDevice);

        assertNotNull(resultDevice);
        assertEquals(localDevice.getId(), resultDevice.getId());
        assertEquals(localDevice.getSupplierForeignId(), resultDevice.getSupplierForeignId());
        assertEquals(localDevice.getSupplier(), resultDevice.getSupplier());
        assertTrue(resultDevice.getActive());
        assertEquals(1, resultDevice.getCustomizations().size());
        assertEquals(1, resultDevice.getExtras().size());
        assertEquals(1, resultDevice.getCharacteristics().size());
        assertSame(customizationTest, resultDevice.getCustomizations().iterator().next());
        assertSame(extraTest, resultDevice.getExtras().iterator().next());
        assertSame(characteristicTest, resultDevice.getCharacteristics().iterator().next());
    }

    @Test
    void testUpdateAndSaveCharacteristicNewCharacteristic() {
        Characteristic externalCharacteristic = new Characteristic();
        externalCharacteristic.setId(101L);

        when(characteristicRepository.findBySupplierForeignId(101L)).thenReturn(Optional.empty());
        doReturn(new Characteristic())
            .when(localToExternalObjectUpdater)
            .updateAndSaveCharacteristic(any(Characteristic.class), eq(externalCharacteristic));

        Characteristic result = localToExternalObjectUpdater.updateAndSaveCharacteristic(externalCharacteristic);

        assertNotNull(result);
        verify(characteristicRepository).findBySupplierForeignId(101L);
        ArgumentCaptor<Characteristic> characteristicCaptor = ArgumentCaptor.forClass(Characteristic.class);
        verify(localToExternalObjectUpdater).updateAndSaveCharacteristic(characteristicCaptor.capture(), eq(externalCharacteristic));
        Characteristic capturedCharacteristic = characteristicCaptor.getValue();
        assertNull(capturedCharacteristic.getId());
        assertEquals(101L, capturedCharacteristic.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveCharacteristicExistingCharacteristic() {
        Characteristic externalCharacteristic = new Characteristic();
        externalCharacteristic.setId(101L);
        Characteristic localCharacteristic = new Characteristic();
        localCharacteristic.setSupplierForeignId(101L);

        when(characteristicRepository.findBySupplierForeignId(101L)).thenReturn(Optional.of(localCharacteristic));
        doReturn(localCharacteristic)
            .when(localToExternalObjectUpdater)
            .updateAndSaveCharacteristic(localCharacteristic, externalCharacteristic);

        Characteristic result = localToExternalObjectUpdater.updateAndSaveCharacteristic(externalCharacteristic);

        assertSame(result, localCharacteristic);
        verify(characteristicRepository).findBySupplierForeignId(101L);
        verify(localToExternalObjectUpdater).updateAndSaveCharacteristic(localCharacteristic, externalCharacteristic);
    }

    @Test
    void testUpdateAndSaveCharacteristicWithLocalCharacteristic() {
        Characteristic externalCharacteristic = new Characteristic();
        externalCharacteristic.setId(102L);
        Characteristic localCharacteristic = new Characteristic();
        localCharacteristic.setId(1L);

        when(characteristicRepository.save(any(Characteristic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Characteristic result = localToExternalObjectUpdater.updateAndSaveCharacteristic(localCharacteristic, externalCharacteristic);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(102L, result.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveExtraNewExtra() {
        Extra externalExtra = new Extra();
        externalExtra.setId(101L);

        when(extraRepository.findBySupplierForeignId(101L)).thenReturn(Optional.empty());
        doReturn(new Extra()).when(localToExternalObjectUpdater).updateAndSaveExtra(any(Extra.class), eq(externalExtra));

        Extra result = localToExternalObjectUpdater.updateAndSaveExtra(externalExtra);

        assertNotNull(result);
        verify(extraRepository).findBySupplierForeignId(101L);
        ArgumentCaptor<Extra> extraCaptor = ArgumentCaptor.forClass(Extra.class);
        verify(localToExternalObjectUpdater).updateAndSaveExtra(extraCaptor.capture(), eq(externalExtra));
        Extra capturedExtra = extraCaptor.getValue();
        assertNull(capturedExtra.getId());
        assertEquals(101L, capturedExtra.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveExtraExistingExtra() {
        Extra externalExtra = new Extra();
        externalExtra.setId(101L);
        Extra localExtra = new Extra();
        localExtra.setSupplierForeignId(101L);

        when(extraRepository.findBySupplierForeignId(101L)).thenReturn(Optional.of(localExtra));
        doReturn(localExtra).when(localToExternalObjectUpdater).updateAndSaveExtra(localExtra, externalExtra);

        Extra result = localToExternalObjectUpdater.updateAndSaveExtra(externalExtra);

        assertSame(result, localExtra);
        verify(extraRepository).findBySupplierForeignId(101L);
        verify(localToExternalObjectUpdater).updateAndSaveExtra(localExtra, externalExtra);
    }

    @Test
    void testUpdateAndSaveExtraWithLocalExtra() {
        Extra externalExtra = new Extra();
        externalExtra.setId(102L);
        Extra localExtra = new Extra();
        localExtra.setId(1L);

        when(extraRepository.save(any(Extra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Extra result = localToExternalObjectUpdater.updateAndSaveExtra(localExtra, externalExtra);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(102L, result.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveOptionNewOption() {
        Option externalOption = new Option();
        externalOption.setId(101L);

        when(optionRepository.findBySupplierForeignId(101L)).thenReturn(Optional.empty());
        doReturn(new Option()).when(localToExternalObjectUpdater).updateAndSaveOption(any(Option.class), eq(externalOption));

        Option result = localToExternalObjectUpdater.updateAndSaveOption(externalOption);

        assertNotNull(result);
        verify(optionRepository).findBySupplierForeignId(101L);
        ArgumentCaptor<Option> optionCaptor = ArgumentCaptor.forClass(Option.class);
        verify(localToExternalObjectUpdater).updateAndSaveOption(optionCaptor.capture(), eq(externalOption));
        Option capturedOption = optionCaptor.getValue();
        assertNull(capturedOption.getId());
        assertEquals(101L, capturedOption.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveOptionExistingOption() {
        Option externalOption = new Option();
        externalOption.setId(101L);
        Option localOption = new Option();
        localOption.setSupplierForeignId(101L);

        when(optionRepository.findBySupplierForeignId(101L)).thenReturn(Optional.of(localOption));
        doReturn(localOption).when(localToExternalObjectUpdater).updateAndSaveOption(localOption, externalOption);

        Option result = localToExternalObjectUpdater.updateAndSaveOption(externalOption);

        assertSame(result, localOption);
        verify(optionRepository).findBySupplierForeignId(101L);
        verify(localToExternalObjectUpdater).updateAndSaveOption(localOption, externalOption);
    }

    @Test
    void testUpdateAndSaveOptionWithLocalOption() {
        Option externalOption = new Option();
        externalOption.setId(102L);
        Option localOption = new Option();
        localOption.setId(1L);

        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Option result = localToExternalObjectUpdater.updateAndSaveOption(localOption, externalOption);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(102L, result.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveCustomizationNewCustomization() {
        Customization externalCustomization = new Customization();
        externalCustomization.setId(101L);

        when(customizationRepository.findBySupplierForeignId(101L)).thenReturn(Optional.empty());
        doReturn(new Customization())
            .when(localToExternalObjectUpdater)
            .updateAndSaveCustomization(any(Customization.class), eq(externalCustomization));

        Customization result = localToExternalObjectUpdater.updateAndSaveCustomization(externalCustomization);

        assertNotNull(result);
        verify(customizationRepository).findBySupplierForeignId(101L);
        ArgumentCaptor<Customization> customizationCaptor = ArgumentCaptor.forClass(Customization.class);
        verify(localToExternalObjectUpdater).updateAndSaveCustomization(customizationCaptor.capture(), eq(externalCustomization));
        Customization capturedCustomization = customizationCaptor.getValue();
        assertNull(capturedCustomization.getId());
        assertEquals(101L, capturedCustomization.getSupplierForeignId());
    }

    @Test
    void testUpdateAndSaveCustomizationExistingCustomization() {
        Customization externalCustomization = new Customization();
        externalCustomization.setId(101L);
        Customization localCustomization = new Customization();
        localCustomization.setSupplierForeignId(101L);

        when(customizationRepository.findBySupplierForeignId(101L)).thenReturn(Optional.of(localCustomization));
        doReturn(localCustomization)
            .when(localToExternalObjectUpdater)
            .updateAndSaveCustomization(localCustomization, externalCustomization);

        Customization result = localToExternalObjectUpdater.updateAndSaveCustomization(externalCustomization);

        assertNotNull(result);
        verify(customizationRepository).findBySupplierForeignId(101L);
        verify(localToExternalObjectUpdater).updateAndSaveCustomization(localCustomization, externalCustomization);
    }

    @Test
    void testUpdateAndSaveCustomizationWithLocalCustomization() {
        Option optionTest1 = new Option();
        optionTest1.setId(1L);
        Option optionTest2 = new Option();
        optionTest2.setId(2L);

        Customization externalCustomization = new Customization();
        externalCustomization.setId(102L);
        externalCustomization.setOptions(new HashSet<>(List.of(optionTest1)));
        Customization localCustomization = new Customization();
        localCustomization.setId(1L);

        when(customizationRepository.save(any(Customization.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doReturn(optionTest2).when(localToExternalObjectUpdater).updateAndSaveOption(eq(optionTest1));

        Customization result = localToExternalObjectUpdater.updateAndSaveCustomization(localCustomization, externalCustomization);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(102L, result.getSupplierForeignId());
        assertEquals(1, result.getOptions().size());
        assertSame(optionTest2, result.getOptions().iterator().next());
    }
}
