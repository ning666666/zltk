package com.offcn.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

//解决跨域的配置
@Configuration
public class GatewayCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 配置跨域
        corsConfiguration.addAllowedHeader("*"); // 允许所有请求头跨域
        corsConfiguration.addAllowedMethod("*"); // 允许所有请求方法跨域
        corsConfiguration.addAllowedOrigin("*"); // 允许所有请求来源跨域
        corsConfiguration.setAllowCredentials(true); //允许携带cookie跨域，否则跨域请求会丢失cookie信息

        source.registerCorsConfiguration("/**", corsConfiguration);//所有来源的路径，上面配置好的规则

        return new CorsWebFilter(source);
    }

    @Bean
    public com.offcn.filter.JwtCheckGatewayFilterFactory jwtCheckGatewayFilterFactory(){
        return new com.offcn.filter.JwtCheckGatewayFilterFactory();
    }
}
