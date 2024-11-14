package ar.edu.um.programacion2.jh.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExtraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Extra getExtraSample1() {
        return new Extra().id(1L).supplierForeignId(1L).name("name1").description("description1");
    }

    public static Extra getExtraSample2() {
        return new Extra().id(2L).supplierForeignId(2L).name("name2").description("description2");
    }

    public static Extra getExtraRandomSampleGenerator() {
        return new Extra()
            .id(longCount.incrementAndGet())
            .supplierForeignId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
