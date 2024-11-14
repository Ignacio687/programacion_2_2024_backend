package ar.edu.um.programacion2.jh.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customization getCustomizationSample1() {
        return new Customization().id(1L).supplierForeignId(1L).name("name1").description("description1");
    }

    public static Customization getCustomizationSample2() {
        return new Customization().id(2L).supplierForeignId(2L).name("name2").description("description2");
    }

    public static Customization getCustomizationRandomSampleGenerator() {
        return new Customization()
            .id(longCount.incrementAndGet())
            .supplierForeignId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
