package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.DeviceSynchronizationService;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.utilities.ExternalDevicesChangeChecker;
import ar.edu.um.programacion2.jh.service.utilities.LocalToExternalObjectUpdater;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@Service
public class DeviceSynchronizationServiceImpl implements DeviceSynchronizationService {

    private Thread thread;
    private volatile boolean running = true;
    private final Logger log = LoggerFactory.getLogger(DeviceSynchronizationServiceImpl.class);

    private final DeviceRepository deviceRepository;
    private final DeviceClient deviceClient;
    private final ExternalDevicesChangeChecker externalDevicesChangeChecker;
    private final LocalToExternalObjectUpdater localToExternalObjectUpdater;

    @Autowired
    public DeviceSynchronizationServiceImpl(
        DeviceRepository deviceRepository,
        DeviceClient deviceClient,
        ExternalDevicesChangeChecker externalDevicesChangeChecker,
        LocalToExternalObjectUpdater localToExternalObjectUpdater
    ) {
        this.deviceRepository = deviceRepository;
        this.deviceClient = deviceClient;
        this.externalDevicesChangeChecker = externalDevicesChangeChecker;
        this.localToExternalObjectUpdater = localToExternalObjectUpdater;
    }

    public void synchronize() {
        this.log.info("Synchronizing devices...");
        List<DeviceDTO> externalDevices = this.deviceClient.getDevices();
        List<Device> localDevices = this.deviceRepository.findByActiveTrue();
        Iterator<Device> iterator = localDevices.iterator();
        while (iterator.hasNext()) {
            Device device = iterator.next();
            if (externalDevices.stream().noneMatch(externalDevice -> externalDevice.getId().equals(device.getSupplierForeignId()))) {
                this.log.info("Device with id {} was deactivated.", device.getId());
                this.deactivateDevice(device);
                iterator.remove();
            }
        }
        if (!this.externalDevicesChangeChecker.hasChanges(externalDevices, localDevices)) {
            this.log.info("No changes detected between local and external devices, except for deactivations.");
            return;
        }
        externalDevices.forEach(device -> this.localToExternalObjectUpdater.updateAndSaveDevice(device, "APICatedraProgramacion2_2024"));
        this.log.info("Device synchronization completed.");
    }

    private Device deactivateDevice(Device device) {
        device.setActive(false);
        return this.deviceRepository.save(device);
    }

    public void startThread(Long syncTimeLapse) {
        if (thread != null) {
            this.stopThread();
        }
        this.running = true;
        thread = new Thread(() -> {
            while (this.running) {
                try {
                    this.synchronize();
                    Thread.sleep(syncTimeLapse * 1000);
                } catch (InterruptedException e) {
                    this.log.debug("Device synchronization was interrupted");
                    break;
                } catch (Exception e) {
                    this.log.error("An unexpected error occurred during device synchronization", e);
                    this.log.trace("An unexpected error occurred during device synchronization", e);
                    break;
                }
            }
            this.log.info("Device synchronization has stopped.");
        });
        this.thread.start();
    }

    public void stopThread() {
        this.running = false;
        if (thread != null) {
            this.thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                this.log.debug("Thread join was interrupted", e);
            }
        }
    }
}
