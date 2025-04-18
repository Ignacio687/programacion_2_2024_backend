package ar.edu.um.programacion2.jh.service.impl.utilities;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.utilities.ExternalDevicesChangeChecker;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class ExternalDevicesChangeCheckerImpl implements ExternalDevicesChangeChecker {

    @Override
    public boolean hasChanges(List<DeviceDTO> externalDevices, List<Device> localDevices) {
        TreeSet<DeviceDTO> sortedExternalDevices = new TreeSet<>(Comparator.comparing(DeviceDTO::getId));
        sortedExternalDevices.addAll(externalDevices);
        TreeSet<DeviceDTO> sortedLocalDevices = localDevices
            .stream()
            .filter(Device::getActive)
            .map(DeviceDTO::fromDevice)
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DeviceDTO::getSupplierForeignId))));
        if (sortedExternalDevices.size() != sortedLocalDevices.size()) {
            return true;
        }
        Iterator<DeviceDTO> externalIterator = sortedExternalDevices.iterator();
        Iterator<DeviceDTO> localIterator = sortedLocalDevices.iterator();
        while (externalIterator.hasNext() && localIterator.hasNext()) {
            DeviceDTO externalDevice = externalIterator.next();
            DeviceDTO localDevice = localIterator.next();
            if (!localDevice.equalsExternal(externalDevice)) {
                return true;
            }
        }
        return false;
    }
}
