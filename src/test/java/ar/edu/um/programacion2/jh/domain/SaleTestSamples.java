package ar.edu.um.programacion2.jh.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sale getSaleSample1() {
        return new Sale().id(1L).currency("currency1");
    }

    public static Sale getSaleSample2() {
        return new Sale().id(2L).currency("currency2");
    }

    public static Sale getSaleRandomSampleGenerator() {
        return new Sale().id(longCount.incrementAndGet()).currency(UUID.randomUUID().toString());
    }
}
