package ar.edu.um.programacion2.jh.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.web.rest.DeviceSynchronizationController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

class DeviceSynchronizationServiceThreadTest {

    @Mock
    private Logger LOG = LoggerFactory.getLogger(DeviceSynchronizationController.class);

    @Spy
    private DeviceSynchronizationService deviceSynchronizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(deviceSynchronizationService, "log", LOG);
        doNothing().when(deviceSynchronizationService).synchronize();
    }

    @AfterEach
    void tearDown() {
        deviceSynchronizationService.stopThread();
    }

    @Test
    void testStartThread() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(1500);
        verify(deviceSynchronizationService, atLeastOnce()).synchronize();
        verify(deviceSynchronizationService, never()).stopThread();
        assertTrue(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThread() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationService.stopThread();
        Thread.sleep(1500);
        verify(deviceSynchronizationService, atMost(1)).synchronize();
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testThreadInterruption() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationService.stopThread();
        Thread.sleep(1500);
        verify(LOG).debug("Device synchronization was interrupted");
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThreadLogsMessage() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationService.stopThread();
        Thread.sleep(1000);
        verify(LOG).info("Device synchronization has stopped.");
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testStopThreadAfterSettingRunningFalse() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationService.setRunning(false);
        Thread.sleep(1500);
        assertFalse(deviceSynchronizationService.getThread().isAlive());
    }

    @Test
    void testThreadJoinCalled() throws InterruptedException {
        Thread mockThread = mock(Thread.class);
        deviceSynchronizationService.setThread(mockThread);
        deviceSynchronizationService.stopThread();
        verify(mockThread).join();
    }

    @Test
    void testStartThreadWaitsForPreviousThreadToDie() throws InterruptedException {
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(500);
        Thread thread = deviceSynchronizationService.getThread();
        deviceSynchronizationService.startThread(1L);
        Thread.sleep(1500);
        assertFalse(thread.isAlive());
    }
}
