package com.bee.tc.eureka.service;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EurekaServiceImpl implements EurekaService {

    @Override
    @RequestMapping("/list")
    public List<String> listService(String query) {
        return Lists.newArrayList("eureka", "ribbon", "hystrix", query);
    }

}
