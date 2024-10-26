package ar.edu.um.programacion2.jh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceSynchronizationService {

    private Thread thread;
    private volatile boolean running = true;
    private final Logger log = LoggerFactory.getLogger(DeviceSynchronizationService.class);

    public void synchronize() {
        log.info("Synchronizing devices...");
    }

    public void startThread(Long syncTimeLapse) {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                synchronize();
                try {
                    Thread.sleep(syncTimeLapse);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("Device synchronization was interrupted");
                } catch (Exception e) {
                    log.error("An unexpected error occurred during device synchronization", e);
                    log.trace("An unexpected error occurred during device synchronization", e);
                }
            }
            log.info("Device synchronization has stopped.");
        });
        thread.start();
    }

    public void stopThread() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }
}
