package ar.edu.um.programacion2.jh.service;

public interface DeviceSynchronizationService {
    void synchronize();
    void startThread(Long syncTimeLapse);
    void stopThread();
}
