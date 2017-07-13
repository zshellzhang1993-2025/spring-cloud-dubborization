package cc.zshell.eureka.provider.service;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * rest 风格的服务提供者
 */
@RestController
public class RestEurekaServiceProvider implements EurekaService {

    @Override
    @RequestMapping("/list")
    public List<String> listService(String query) {
        return Lists.newArrayList("eureka", "ribbon", "hystrix", query);
    }

}
