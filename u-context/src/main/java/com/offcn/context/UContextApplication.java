package com.offcn.context;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.offcn.context.dao")
@EnableDiscoveryClient
public class UContextApplication {

    public static void main(String[] args) {
        SpringApplication.run(UContextApplication.class, args);
    }

}
