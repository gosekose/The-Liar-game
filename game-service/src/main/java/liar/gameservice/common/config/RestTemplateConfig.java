package liar.gameservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private RestTemplate restTemplate;

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}