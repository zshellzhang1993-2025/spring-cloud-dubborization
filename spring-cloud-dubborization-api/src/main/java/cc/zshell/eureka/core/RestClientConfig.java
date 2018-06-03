package cc.zshell.eureka.core;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * rpc 风格服务消费的内核
 */
@Configuration
@ComponentScan
public class RestClientConfig {

    @Bean("rpcRestClient")
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
