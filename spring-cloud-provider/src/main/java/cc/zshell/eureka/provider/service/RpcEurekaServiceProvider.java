package cc.zshell.eureka.provider.service;

import cc.zshell.eureka.provider.api.Provider;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * rpc 风格的服务提供者
 */
@Provider
public class RpcEurekaServiceProvider implements EurekaService {

    @Override
    public List<String> listService(String query) {
        return Lists.newArrayList("eureka", "ribbon", "hystrix", query);
    }

}
