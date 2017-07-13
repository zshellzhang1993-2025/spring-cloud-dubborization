package cc.zshell.eureka.consumer.service;

import cc.zshell.eureka.consumer.api.Consumer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * rpc 风格的服务消费
 */
@Service
public class RpcEurekaServiceConsumer {

    // spring 风格注入
    @Consumer("test-eureka-provider")
    private EurekaService eurekaService;

    public List<String> getList(String query) {
        List<String> result = eurekaService.listService(query);
        System.out.println();
        return result;
    }

}
