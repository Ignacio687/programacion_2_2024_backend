package ar.edu.um.programacion2.jh.service.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientInterceptor implements RequestInterceptor {

    @Value("${cliente-web.token}")
    protected String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer " + this.token);
    }
}
