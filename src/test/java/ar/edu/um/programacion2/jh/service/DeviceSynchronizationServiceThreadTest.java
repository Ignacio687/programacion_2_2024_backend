package ar.edu.um.programacion2.jh.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DeviceSynchronizationServiceThreadTest {

    private DeviceSynchronizationService deviceSynchronizationService;
    private Logger log;

    @BeforeEach
    void setUp() {
        deviceSynchronizationService = spy(new DeviceSynchronizationService());
        log = LoggerFactory.getLogger(DeviceSynchronizationService.class);
        doNothing().when(deviceSynchronizationService).synchronize();
    }

    @AfterEach
    void tearDown() {
        deviceSynchronizationService.stopThread();
    }

    @Test
    void testStartThread() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(1500); // Espera para asegurar que el hilo se ejecute al menos una vez
        verify(deviceSynchronizationService, atLeastOnce()).synchronize();
        verify(deviceSynchronizationService, never()).stopThread();
        assertTrue(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThread() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500); // Espera para asegurar que el hilo se inicie
        deviceSynchronizationService.stopThread();
        Thread.sleep(1500); // Espera para asegurar que el hilo se detenga
        verify(deviceSynchronizationService, atMost(1)).synchronize();
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testThreadInterruption() throws InterruptedException {
        doThrow(new InterruptedException()).when(deviceSynchronizationService).synchronize();
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500); // Espera para asegurar que el hilo se inicie
        verify(log).debug("Device synchronization was interrupted");
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThreadLogsMessage() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500); // Espera para asegurar que el hilo se inicie
        deviceSynchronizationService.stopThread();
        verify(log).info("Device synchronization has stopped.");
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThreadAfterSettingRunningFalse() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500); // Espera para asegurar que el hilo se inicie
        deviceSynchronizationService.setRunning(false);
        deviceSynchronizationService.stopThread();
        verify(deviceSynchronizationService).stopThread();
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }
}
