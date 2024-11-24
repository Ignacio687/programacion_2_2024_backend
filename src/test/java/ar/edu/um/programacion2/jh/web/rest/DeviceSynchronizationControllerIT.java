package ar.edu.um.programacion2.jh.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.service.DeviceSynchronizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class DeviceSynchronizationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DeviceSynchronizationService deviceSynchronizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startThread_withValidSyncTimeLaps_shouldReturnOk(CapturedOutput output) throws Exception {
        mockMvc
            .perform(post("/api/startDeviceSync").param("syncTimeLaps", "3600").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deviceSynchronizationService, times(1)).startThread(3600L);
        assertThat(output).contains("Request to start device synchronization with syncTimeLaps: 3600");
    }

    @Test
    void startThread_withNegativeSyncTimeLaps_shouldReturnBadRequest(CapturedOutput output) throws Exception {
        mockMvc
            .perform(post("/api/startDeviceSync").param("syncTimeLaps", "-1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(deviceSynchronizationService, never()).startThread(anyLong());
        assertThat(output).contains("Request to start device synchronization with syncTimeLaps: -1");
    }

    @Test
    void stopThread_shouldReturnOk(CapturedOutput output) throws Exception {
        mockMvc.perform(post("/api/stopDeviceSync").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(deviceSynchronizationService, times(1)).stopThread();
        assertThat(output).contains("Request to stop device synchronization");
    }
}
