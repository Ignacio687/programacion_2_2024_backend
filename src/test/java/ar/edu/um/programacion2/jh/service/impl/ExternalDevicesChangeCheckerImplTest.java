package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.impl.utilities.ExternalDevicesChangeCheckerImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ExternalDevicesChangeCheckerImplTest {

    @Spy
    private ExternalDevicesChangeCheckerImpl externalDevicesChangeChecker;

    private MockedStatic<DeviceDTO> deviceDTOMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceDTOMockedStatic = mockStatic(DeviceDTO.class);
    }

    @AfterEach
    void tearDown() {
        deviceDTOMockedStatic.close();
    }

    @Test
    void testNoChanges() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = Collections.singletonList(externalDeviceDTO);

        Device localDevice = new Device();
        localDevice.setSupplierForeignId(101L);
        localDevice.setActive(true);
        List<Device> localDevices = Collections.singletonList(localDevice);

        DeviceDTO localDeviceDTO = spy(new DeviceDTO());
        localDeviceDTO.setSupplierForeignId(101L);

        deviceDTOMockedStatic.when(() -> DeviceDTO.fromDevice(any(Device.class))).thenReturn(localDeviceDTO);
        when(localDeviceDTO.equalsExternal(externalDeviceDTO)).thenReturn(true);

        boolean result = externalDevicesChangeChecker.hasChanges(externalDevices, localDevices);

        assertFalse(result);
    }

    @Test
    void testDifferentSizes() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = Collections.singletonList(externalDeviceDTO);

        Device localDevice1 = new Device();
        localDevice1.setSupplierForeignId(101L);
        localDevice1.setActive(true);
        Device localDevice2 = new Device();
        localDevice2.setSupplierForeignId(102L);
        localDevice2.setActive(true);
        List<Device> localDevices = List.of(localDevice1, localDevice2);

        DeviceDTO localDeviceDTO1 = spy(new DeviceDTO());
        localDeviceDTO1.setSupplierForeignId(101L);
        DeviceDTO localDeviceDTO2 = spy(new DeviceDTO());
        localDeviceDTO2.setSupplierForeignId(102L);

        deviceDTOMockedStatic
            .when(() -> DeviceDTO.fromDevice(any(Device.class)))
            .thenAnswer(invocation -> {
                Device device = invocation.getArgument(0);
                if (device.getSupplierForeignId() == 101L) {
                    return localDeviceDTO1;
                } else {
                    return localDeviceDTO2;
                }
            });
        when(localDeviceDTO1.equalsExternal(externalDeviceDTO)).thenReturn(true);
        when(localDeviceDTO2.equalsExternal(externalDeviceDTO)).thenReturn(true);

        boolean result = externalDevicesChangeChecker.hasChanges(externalDevices, localDevices);

        assertTrue(result);
    }

    @Test
    void testSameDevicesDifferentOrder() {
        DeviceDTO externalDeviceDTO1 = new DeviceDTO();
        externalDeviceDTO1.setId(101L);
        DeviceDTO externalDeviceDTO2 = new DeviceDTO();
        externalDeviceDTO2.setId(102L);
        List<DeviceDTO> externalDevices = List.of(externalDeviceDTO1, externalDeviceDTO2);

        Device localDevice1 = new Device();
        localDevice1.setSupplierForeignId(102L);
        localDevice1.setActive(true);
        Device localDevice2 = new Device();
        localDevice2.setSupplierForeignId(101L);
        localDevice2.setActive(true);
        List<Device> localDevices = List.of(localDevice1, localDevice2);

        DeviceDTO localDeviceDTO1 = spy(new DeviceDTO());
        localDeviceDTO1.setSupplierForeignId(102L);
        DeviceDTO localDeviceDTO2 = spy(new DeviceDTO());
        localDeviceDTO2.setSupplierForeignId(101L);

        deviceDTOMockedStatic
            .when(() -> DeviceDTO.fromDevice(any(Device.class)))
            .thenAnswer(invocation -> {
                Device device = invocation.getArgument(0);
                if (device.getSupplierForeignId() == 102L) {
                    return localDeviceDTO1;
                } else {
                    return localDeviceDTO2;
                }
            });
        when(localDeviceDTO1.equalsExternal(externalDeviceDTO1)).thenReturn(false);
        when(localDeviceDTO2.equalsExternal(externalDeviceDTO2)).thenReturn(false);
        when(localDeviceDTO1.equalsExternal(externalDeviceDTO2)).thenReturn(true);
        when(localDeviceDTO2.equalsExternal(externalDeviceDTO1)).thenReturn(true);

        boolean result = externalDevicesChangeChecker.hasChanges(externalDevices, localDevices);

        assertFalse(result);
    }

    @Test
    void testInactiveLocalDevice() {
        DeviceDTO externalDeviceDTO = new DeviceDTO();
        externalDeviceDTO.setId(101L);
        List<DeviceDTO> externalDevices = Collections.singletonList(externalDeviceDTO);

        Device localDevice = new Device();
        localDevice.setSupplierForeignId(101L);
        localDevice.setActive(false); // Inactive device
        List<Device> localDevices = Collections.singletonList(localDevice);

        DeviceDTO localDeviceDTO = spy(new DeviceDTO());
        localDeviceDTO.setSupplierForeignId(101L);

        deviceDTOMockedStatic.when(() -> DeviceDTO.fromDevice(any(Device.class))).thenReturn(localDeviceDTO);
        when(localDeviceDTO.equalsExternal(externalDeviceDTO)).thenReturn(false);

        boolean result = externalDevicesChangeChecker.hasChanges(externalDevices, localDevices);

        assertTrue(result);
    }
}
