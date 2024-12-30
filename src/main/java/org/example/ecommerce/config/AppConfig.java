package org.example.ecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
