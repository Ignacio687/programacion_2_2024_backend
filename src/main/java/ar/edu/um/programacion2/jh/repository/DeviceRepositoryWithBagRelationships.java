package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DeviceRepositoryWithBagRelationships {
    Optional<Device> fetchBagRelationships(Optional<Device> device);

    List<Device> fetchBagRelationships(List<Device> devices);

    Page<Device> fetchBagRelationships(Page<Device> devices);
}
