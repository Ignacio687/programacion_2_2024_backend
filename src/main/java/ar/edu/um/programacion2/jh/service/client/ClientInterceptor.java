package ar.edu.um.programacion2.jh.service.client;

import ar.edu.um.programacion2.jh.config.WebClientConfiguration;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientInterceptor implements RequestInterceptor {

    @Autowired
    WebClientConfiguration webClientConfiguration;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer " + this.webClientConfiguration.getToken());
    }
}
