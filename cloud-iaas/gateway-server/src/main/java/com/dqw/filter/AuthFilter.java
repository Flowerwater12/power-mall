package com.dqw.filter;


import com.dqw.config.WhiteUrlsConfig;
import com.dqw.constant.AuthConstants;
import com.dqw.constant.BusinessEnum;
import com.dqw.model.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/*
* 全局TOKEN过滤器
* */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private WhiteUrlsConfig whiteUrlsConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        String path = request.getPath().toString();
        // 判断当前请求是否存在于白名单
        if (whiteUrlsConfig.getAllowUrls().contains(path)){
            return chain.filter(exchange);
        }
        // 当前请求不存在于白名单中，需要验证其身份
        // 从约定好的位置（即请求头中的Authorization）获取令牌
        // 从请求头中获取Authorization的值,该值的格式为：bearer token
        String authorizationValue = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorizationValue)){
            // 判断token是否为空
            String token= authorizationValue.replaceFirst(AuthConstants.BEARER,"");
            if (StringUtils.hasText(token)&&stringRedisTemplate.hasKey(AuthConstants.LOGIN_TOKEN_PREFIX + token)){
                return chain.filter(exchange);
            }
        }

        // 令牌token有问题，请求不合法
        log.error("拦截非法请求，时间{}，请求的API路径：{}",new Date(),path);

        // 处理非法请求，返回一个错误消息
        // 获取响应结果对象
        ServerHttpResponse response = exchange.getResponse();

        // 401 请求未授权
        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
        // 创建objectMapper，处理对象与json之间的互转
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes=new byte[100];
        try {
             bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return -10;
    }
}
