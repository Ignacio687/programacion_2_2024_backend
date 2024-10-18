package ar.edu.um.programacion2.jh.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Device getDeviceSample1() {
        return new Device()
            .id(1L)
            .supplierForeignKey(1L)
            .supplier("supplier1")
            .code("code1")
            .name("name1")
            .description("description1")
            .currency("currency1");
    }

    public static Device getDeviceSample2() {
        return new Device()
            .id(2L)
            .supplierForeignKey(2L)
            .supplier("supplier2")
            .code("code2")
            .name("name2")
            .description("description2")
            .currency("currency2");
    }

    public static Device getDeviceRandomSampleGenerator() {
        return new Device()
            .id(longCount.incrementAndGet())
            .supplierForeignKey(longCount.incrementAndGet())
            .supplier(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
