package com.offcn.filter;

import com.offcn.common.utils.JWTUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * JWT验证的过滤器
 *
 **/
public class JwtCheckGatewayFilterFactory  extends AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {

    public JwtCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //从头文件中取出token令牌也叫签名，Authorization是头文件中的安全加密字段，用Authorization去标记，表示令牌的名字
            String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            //校验jwtToken的合法性
            if (jwtToken != null) {
                // 校验token合法
                boolean verifyResult = JWTUtil.verify(jwtToken);
                if(!verifyResult){
                    //token验证失败
                    //不合法(响应未登录的异常)
                    ServerHttpResponse response = exchange.getResponse();
                    //设置headers
                    HttpHeaders httpHeaders = response.getHeaders();
                    httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
                    //不持久化、不缓存、必须重新验证通过、最大生命周期
                    httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                    //设置body
                    String warningStr = "token验证失败";
                    //响应异常情况
                    DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());

                    return response.writeWith(Mono.just(bodyDataBuffer));

                }
                // 将用户id作为参数传递下去
                return chain.filter(exchange);
            }

            //不合法(响应未登录的异常)
            ServerHttpResponse response = exchange.getResponse();
            //设置headers
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            //设置body
            String warningStr = "未登录或登录超时";
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());

            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }

    public static class Config {
    }
}
