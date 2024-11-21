package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.impl.DeviceSynchronizationServiceImpl;
import ar.edu.um.programacion2.jh.web.rest.DeviceSynchronizationController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

class DeviceSynchronizationServiceImplThreadTest {

    @Mock
    private Logger LOG = LoggerFactory.getLogger(DeviceSynchronizationController.class);

    @Mock
    private DeviceClient deviceClient;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private CharacteristicRepository characteristicRepository;

    @Mock
    private ExtraRepository extraRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private CustomizationRepository customizationRepository;

    @InjectMocks
    private DeviceSynchronizationServiceImpl deviceSynchronizationServiceImpl;

    @BeforeEach
    void setUp() {
        this.deviceSynchronizationServiceImpl = spy(
            new DeviceSynchronizationServiceImpl(
                deviceClient,
                deviceRepository,
                characteristicRepository,
                extraRepository,
                optionRepository,
                customizationRepository
            )
        );
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(deviceSynchronizationServiceImpl, "log", LOG);
        doNothing().when(deviceSynchronizationServiceImpl).synchronize();
    }

    @AfterEach
    void tearDown() {
        deviceSynchronizationServiceImpl.stopThread();
    }

    @Test
    void testStartThread() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(1500);
        verify(deviceSynchronizationServiceImpl, atLeastOnce()).synchronize();
        verify(deviceSynchronizationServiceImpl, never()).stopThread();
        assertTrue(deviceSynchronizationServiceImpl.getThread().isAlive());
    }

    @Test
    void testStopThread() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationServiceImpl.stopThread();
        Thread.sleep(1500);
        verify(deviceSynchronizationServiceImpl, atMost(1)).synchronize();
        assertFalse(deviceSynchronizationServiceImpl.getThread().isAlive());
    }

    @Test
    void testThreadInterruption() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationServiceImpl.stopThread();
        Thread.sleep(1500);
        verify(LOG).debug("Device synchronization was interrupted");
        assertFalse(deviceSynchronizationServiceImpl.getThread().isAlive());
    }

    @Test
    void testStopThreadLogsMessage() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationServiceImpl.stopThread();
        Thread.sleep(1000);
        verify(LOG).info("Device synchronization has stopped.");
        assertFalse(deviceSynchronizationServiceImpl.getThread().isAlive());
    }

    @Test
    void testStopThreadAfterSettingRunningFalse() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(500);
        deviceSynchronizationServiceImpl.setRunning(false);
        Thread.sleep(1500);
        assertFalse(deviceSynchronizationServiceImpl.getThread().isAlive());
    }

    @Test
    void testThreadJoinCalled() throws InterruptedException {
        Thread mockThread = mock(Thread.class);
        deviceSynchronizationServiceImpl.setThread(mockThread);
        deviceSynchronizationServiceImpl.stopThread();
        verify(mockThread).join();
    }

    @Test
    void testStartThreadWaitsForPreviousThreadToDie() throws InterruptedException {
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(500);
        Thread thread = deviceSynchronizationServiceImpl.getThread();
        deviceSynchronizationServiceImpl.startThread(1L);
        Thread.sleep(1500);
        assertFalse(thread.isAlive());
    }
}
