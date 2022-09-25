package com.offcn.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //可以作为nacos客户端，需要远程调用
public class UOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(UOssApplication.class, args);
    }

}
