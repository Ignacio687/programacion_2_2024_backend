package ar.edu.um.programacion2.jh.service.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ClientInterceptorTest {

    private ClientInterceptor clientInterceptor;
    private RequestTemplate requestTemplate;

    @BeforeEach
    void setUp() {
        clientInterceptor = new ClientInterceptor();
        requestTemplate = new RequestTemplate();
    }

    @Test
    void applyAddsAuthorizationHeader() {
        ReflectionTestUtils.setField(clientInterceptor, "token", "test-token");

        clientInterceptor.apply(requestTemplate);

        assertEquals("Bearer test-token", requestTemplate.headers().get("Authorization").iterator().next());
    }
}
