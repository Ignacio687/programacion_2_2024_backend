package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.config.ClientewebConfiguration;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteInterceptor implements RequestInterceptor {

    @Autowired
    ClientewebConfiguration clientewebConfiguration;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer " + this.clientewebConfiguration.getToken());
    }
}
