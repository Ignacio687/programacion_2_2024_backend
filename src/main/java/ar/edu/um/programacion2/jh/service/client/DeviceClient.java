package ar.edu.um.programacion2.jh.service.client;

import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "device", url = "$(cliente-web.rootUrl)")
public interface DeviceClient {
    @GetMapping("/api/catedra/dispositivos")
    List<DeviceDTO> getDevices();
}
