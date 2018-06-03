package cc.zshell.eureka.consumer.service;

import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * rest 风格的服务消费
 */
@Service
public class RestEurekaServiceConsumer {

    private static final String PROVIDER_SERVICE_NAME = "test-eureka-provider";

    @Resource
    private RestTemplate restTemplate;

    @HystrixCommand(
            fallbackMethod = "fallbackGetList",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")}
    )
    @SuppressWarnings("unchecked")
    public List<String> getList(String query) {
        List<String> result = restTemplate.getForObject("http://" + PROVIDER_SERVICE_NAME + "/listService?query=" + query, List.class);
        System.out.println();
        return result;
    }

    public List<String> fallbackGetList(String query) {
        return Lists.newArrayList("fallback", query);
    }

}
