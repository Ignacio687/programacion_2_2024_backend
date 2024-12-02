package ar.edu.um.programacion2.jh.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.um.programacion2.jh.service.DeviceSynchronizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("dev")
class DeviceSynchronizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceSynchronizationService deviceSynchronizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void startThread_withValidSyncTimeLaps_shouldReturnOk(CapturedOutput output) throws Exception {
        mockMvc
            .perform(post("/api/startDeviceSync").param("syncTimeLaps", "3600").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(deviceSynchronizationService, times(1)).startThread(3600L);
        assertThat(output).contains("Request to start device synchronization with syncTimeLaps: 3600");
    }

    @Test
    @WithMockUser
    void startThread_withNegativeSyncTimeLaps_shouldReturnBadRequest(CapturedOutput output) throws Exception {
        mockMvc
            .perform(post("/api/startDeviceSync").param("syncTimeLaps", "-1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(deviceSynchronizationService, never()).startThread(anyLong());
        assertThat(output).contains("Request to start device synchronization with syncTimeLaps: -1");
    }

    @Test
    @WithMockUser
    void stopThread_shouldReturnOk(CapturedOutput output) throws Exception {
        mockMvc.perform(post("/api/stopDeviceSync").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(deviceSynchronizationService, times(1)).stopThread();
        assertThat(output).contains("Request to stop device synchronization");
    }
}
