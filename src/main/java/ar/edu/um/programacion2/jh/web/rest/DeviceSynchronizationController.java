package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.service.DeviceSynchronizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeviceSynchronizationController {

    private final DeviceSynchronizationService deviceSynchronizationService;
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSynchronizationController.class);

    public DeviceSynchronizationController(DeviceSynchronizationService deviceSynchronizationService) {
        this.deviceSynchronizationService = deviceSynchronizationService;
    }

    @PostMapping("/startDeviceSync")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> startThread(@RequestParam(defaultValue = "3600") Long syncTimeLaps) {
        LOG.debug("Request to start device synchronization with syncTimeLaps: {}", syncTimeLaps);
        if (syncTimeLaps < 0) {
            return ResponseEntity.badRequest().body("syncTimeLaps cannot be negative");
        }
        deviceSynchronizationService.startThread(syncTimeLaps);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stopDeviceSync")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void stopThread() {
        LOG.debug("Request to stop device synchronization");
        deviceSynchronizationService.stopThread();
    }
}
