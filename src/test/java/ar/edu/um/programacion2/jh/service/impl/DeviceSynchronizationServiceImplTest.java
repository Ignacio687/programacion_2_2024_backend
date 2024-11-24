package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.utilities.ExternalDevicesChangeChecker;
import ar.edu.um.programacion2.jh.service.utilities.LocalToExternalObjectUpdater;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

class DeviceSynchronizationServiceImplTest {

    private DeviceRepository deviceRepository;

    private DeviceClient deviceClient;

    private ExternalDevicesChangeChecker externalDevicesChangeChecker;

    private LocalToExternalObjectUpdater localToExternalObjectUpdater;

    private Logger log;

    private DeviceSynchronizationServiceImpl deviceSynchronizationService;

    @BeforeEach
    void setUp() {
        this.deviceRepository = mock(DeviceRepository.class);
        this.deviceClient = mock(DeviceClient.class);
        this.externalDevicesChangeChecker = mock(ExternalDevicesChangeChecker.class);
        this.localToExternalObjectUpdater = mock(LocalToExternalObjectUpdater.class);
        this.log = mock(Logger.class);
        this.deviceSynchronizationService = new DeviceSynchronizationServiceImpl(
            this.deviceRepository,
            this.deviceClient,
            this.externalDevicesChangeChecker,
            this.localToExternalObjectUpdater
        );
        ReflectionTestUtils.setField(deviceSynchronizationService, "log", this.log);
    }

    @Test
    void testSynchronizeNoChanges() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = Collections.singletonList(externalDeviceDTO);

        Device localDevice = new Device();
        localDevice.setSupplierForeignId(101L);
        List<Device> localDevices = Collections.singletonList(localDevice);

        when(deviceClient.getDevices()).thenReturn(externalDevices);
        when(deviceRepository.findByActiveTrue()).thenReturn(localDevices);
        when(externalDevicesChangeChecker.hasChanges(externalDevices, localDevices)).thenReturn(false);

        deviceSynchronizationService.synchronize();

        verify(log).info("Synchronizing devices...");
        verify(log).info("No changes detected between local and external devices, except for deactivations.");
        verifyNoInteractions(localToExternalObjectUpdater);
        verify(deviceRepository).findByActiveTrue();
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void testSynchronizeUpdateDevices() {
        DeviceDTO externalDeviceDTO1 = new DeviceDTO();
        externalDeviceDTO1.setId(101L);
        DeviceDTO externalDeviceDTO2 = new DeviceDTO();
        externalDeviceDTO2.setId(102L);
        List<DeviceDTO> externalDevices = Arrays.asList(externalDeviceDTO1, externalDeviceDTO2);

        Device localDevice = new Device();
        localDevice.setSupplierForeignId(101L);
        localDevice.setActive(true);
        List<Device> localDevices = Collections.singletonList(localDevice);

        when(deviceClient.getDevices()).thenReturn(externalDevices);
        when(deviceRepository.findByActiveTrue()).thenReturn(localDevices);
        when(externalDevicesChangeChecker.hasChanges(externalDevices, localDevices)).thenReturn(true);

        deviceSynchronizationService.synchronize();

        verify(log).info("Synchronizing devices...");
        verify(localToExternalObjectUpdater).updateAndSaveDevice(externalDeviceDTO1, "APICatedraProgramacion2_2024");
        verify(localToExternalObjectUpdater).updateAndSaveDevice(externalDeviceDTO2, "APICatedraProgramacion2_2024");
        verifyNoMoreInteractions(localToExternalObjectUpdater);
        verify(log).info("Device synchronization completed.");
        verify(deviceRepository).findByActiveTrue();
        verifyNoMoreInteractions(deviceRepository);
    }

    @Test
    void testSynchronizeDeactivateDevices() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = List.of(externalDeviceDTO);

        Device localDevice = new Device();
        localDevice.setSupplierForeignId(102L); // Different ID to simulate deactivation
        localDevice.setActive(true);

        Device localDeviceSameAsExternal = new Device();
        localDeviceSameAsExternal.setSupplierForeignId(101L); // Same ID as externalDeviceDTO
        localDeviceSameAsExternal.setActive(true);

        List<Device> localDevices = new ArrayList<>() {
            {
                add(localDevice);
                add(localDeviceSameAsExternal);
            }
        };

        when(deviceClient.getDevices()).thenReturn(externalDevices);
        when(deviceRepository.findByActiveTrue()).thenReturn(localDevices);
        when(externalDevicesChangeChecker.hasChanges(externalDevices, localDevices)).thenReturn(false);

        deviceSynchronizationService.synchronize();

        verify(log).info("Synchronizing devices...");
        verify(log).info("Device with id {} was deactivated.", localDevice.getId());
        verify(deviceRepository).save(localDevice);
        assertFalse(localDevice.getActive());
        verify(log).info("No changes detected between local and external devices, except for deactivations.");
        verify(deviceRepository).findByActiveTrue();
        verifyNoMoreInteractions(deviceRepository);
        verifyNoMoreInteractions(localToExternalObjectUpdater);
    }

    @Test
    void testSynchronizeCreateNewDevices() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = Collections.singletonList(externalDeviceDTO);

        List<Device> localDevices = Collections.emptyList(); // No local devices

        when(deviceClient.getDevices()).thenReturn(externalDevices);
        when(deviceRepository.findByActiveTrue()).thenReturn(localDevices);
        when(externalDevicesChangeChecker.hasChanges(externalDevices, localDevices)).thenReturn(true);

        deviceSynchronizationService.synchronize();

        verify(log).info("Synchronizing devices...");
        verify(localToExternalObjectUpdater).updateAndSaveDevice(externalDeviceDTO, "APICatedraProgramacion2_2024");
        verify(log).info("Device synchronization completed.");
        verify(deviceRepository).findByActiveTrue();
        verifyNoMoreInteractions(deviceRepository);
    }
}
