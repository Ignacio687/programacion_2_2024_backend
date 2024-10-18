package ar.edu.um.programacion2.jh.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cliente-web", ignoreUnknownFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientewebConfiguration {

    protected String rootUrl;
    protected String token;
}
