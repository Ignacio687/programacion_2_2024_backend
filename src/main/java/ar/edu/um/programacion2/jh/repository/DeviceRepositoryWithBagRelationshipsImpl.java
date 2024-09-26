package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Device;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DeviceRepositoryWithBagRelationshipsImpl implements DeviceRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DEVICES_PARAMETER = "devices";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Device> fetchBagRelationships(Optional<Device> device) {
        return device.map(this::fetchCharacteristics).map(this::fetchOptions).map(this::fetchExtras);
    }

    @Override
    public Page<Device> fetchBagRelationships(Page<Device> devices) {
        return new PageImpl<>(fetchBagRelationships(devices.getContent()), devices.getPageable(), devices.getTotalElements());
    }

    @Override
    public List<Device> fetchBagRelationships(List<Device> devices) {
        return Optional.of(devices)
            .map(this::fetchCharacteristics)
            .map(this::fetchOptions)
            .map(this::fetchExtras)
            .orElse(Collections.emptyList());
    }

    Device fetchCharacteristics(Device result) {
        return entityManager
            .createQuery("select device from Device device left join fetch device.characteristics where device.id = :id", Device.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Device> fetchCharacteristics(List<Device> devices) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, devices.size()).forEach(index -> order.put(devices.get(index).getId(), index));
        List<Device> result = entityManager
            .createQuery("select device from Device device left join fetch device.characteristics where device in :devices", Device.class)
            .setParameter(DEVICES_PARAMETER, devices)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Device fetchOptions(Device result) {
        return entityManager
            .createQuery("select device from Device device left join fetch device.options where device.id = :id", Device.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Device> fetchOptions(List<Device> devices) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, devices.size()).forEach(index -> order.put(devices.get(index).getId(), index));
        List<Device> result = entityManager
            .createQuery("select device from Device device left join fetch device.options where device in :devices", Device.class)
            .setParameter(DEVICES_PARAMETER, devices)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Device fetchExtras(Device result) {
        return entityManager
            .createQuery("select device from Device device left join fetch device.extras where device.id = :id", Device.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Device> fetchExtras(List<Device> devices) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, devices.size()).forEach(index -> order.put(devices.get(index).getId(), index));
        List<Device> result = entityManager
            .createQuery("select device from Device device left join fetch device.extras where device in :devices", Device.class)
            .setParameter(DEVICES_PARAMETER, devices)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
