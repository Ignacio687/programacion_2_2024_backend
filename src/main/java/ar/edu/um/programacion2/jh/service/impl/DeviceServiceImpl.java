package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.service.DeviceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Device}.
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        LOG.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Device update(Device device) {
        LOG.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Optional<Device> partialUpdate(Device device) {
        LOG.debug("Request to partially update Device : {}", device);

        return deviceRepository
            .findById(device.getId())
            .map(existingDevice -> {
                if (device.getSupplierForeignId() != null) {
                    existingDevice.setSupplierForeignId(device.getSupplierForeignId());
                }
                if (device.getSupplier() != null) {
                    existingDevice.setSupplier(device.getSupplier());
                }
                if (device.getCode() != null) {
                    existingDevice.setCode(device.getCode());
                }
                if (device.getName() != null) {
                    existingDevice.setName(device.getName());
                }
                if (device.getDescription() != null) {
                    existingDevice.setDescription(device.getDescription());
                }
                if (device.getBasePrice() != null) {
                    existingDevice.setBasePrice(device.getBasePrice());
                }
                if (device.getCurrency() != null) {
                    existingDevice.setCurrency(device.getCurrency());
                }
                if (device.getActive() != null) {
                    existingDevice.setActive(device.getActive());
                }

                return existingDevice;
            })
            .map(deviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        LOG.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> findAllByActive(boolean active, Pageable pageable) {
        LOG.debug("Request to get all Devices by active status: {}", active);
        if (active) {
            return deviceRepository.findByActiveTrue(pageable);
        } else {
            return deviceRepository.findByActiveFalse(pageable);
        }
    }

    public Page<Device> findAllWithEagerRelationships(Pageable pageable) {
        return deviceRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        LOG.debug("Request to get Device : {}", id);
        return deviceRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
