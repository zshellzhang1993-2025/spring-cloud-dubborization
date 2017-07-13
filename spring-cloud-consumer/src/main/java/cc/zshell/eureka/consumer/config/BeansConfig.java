package cc.zshell.eureka.consumer.config;

import cc.zshell.eureka.consumer.api.ConsumerAnnotationProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ConsumerAnnotationProcessor autoScanner() {
        return new ConsumerAnnotationProcessor();
    }

}
