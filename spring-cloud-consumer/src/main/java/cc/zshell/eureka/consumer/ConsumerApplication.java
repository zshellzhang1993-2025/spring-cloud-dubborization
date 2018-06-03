package cc.zshell.eureka.consumer;

import cc.zshell.eureka.consumer.service.RestEurekaServiceConsumer;
import cc.zshell.eureka.consumer.service.RpcEurekaServiceConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@SpringCloudApplication
@EnableEurekaClient
@EnableHystrix
@RestController
public class ConsumerApplication {

    @Resource
    private RestEurekaServiceConsumer restEurekaServiceConsumer;

    @Resource
    private RpcEurekaServiceConsumer rpcEurekaServiceConsumer;

    @RequestMapping("/rest/list")
    public List<String> getRestList(String query) {
        List<String> result = restEurekaServiceConsumer.getList(query);
        System.out.println(result.size());
        return result;
    }

    @RequestMapping("/rpc/list")
    public List<String> getRpcList(String query) {
        List<String> result = rpcEurekaServiceConsumer.getList(query);
        System.out.println(result.size());
        return result;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
