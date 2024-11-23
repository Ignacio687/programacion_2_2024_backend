package ar.edu.um.programacion2.jh.service.utilities;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import java.util.List;

public interface ExternalDevicesChangeChecker {
    boolean hasChanges(List<DeviceDTO> externalDevices, List<Device> localDevices);
}
