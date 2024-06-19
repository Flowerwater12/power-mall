package com.dqw.config;

import com.alibaba.fastjson.JSON;
import com.dqw.constant.AuthConstants;
import com.dqw.impl.UserDetailsServiceImpl;
import com.dqw.model.LoginResult;
import com.dqw.model.Result;
import com.dqw.util.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.time.Duration;
import java.util.UUID;

@Configuration
@Slf4j
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭跨站请求伪造
        // 关闭跨域请求
        // 关闭security使用的session策略
        // 配置登录信息
        // 配置登出的信息
        // 其它所有请求都必须进行认证
        http.csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().loginProcessingUrl(AuthConstants.LOGIN_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout().logoutUrl(AuthConstants.LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return (request, response, authentication) -> {


        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request, response, authentication) -> {
            // 使用UUID生成的字符串来当作令牌token --> redis.key
            String token = UUID.randomUUID().toString();
            // 从security容器中获取当前登录的用户身份信息    --> redis.value
            String securityUserJsonStr = JSON.toJSONString(authentication.getPrincipal());

            // 将uuid当前token存放到redis中的key,SecurityUser安全用户对象的json格式字符串当作它的value存放到redis
            // 目的主要是解决：token在未过期的情况下，用户退出
            stringRedisTemplate.opsForValue().set(AuthConstants.LOGIN_TOKEN_PREFIX+token,securityUserJsonStr, Duration.ofSeconds(AuthConstants.TOKEN_TIME));

            // 将token和token的有效时长给前端 -> 将响应的2个值封装到一个对象：登录统一结果对象
            LoginResult loginResult = new LoginResult(token,AuthConstants.TOKEN_TIME);
            // 创建一个统一响应结果对象
            Result<LoginResult> result = Result.success(loginResult);
            // 返回结果给前端
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            /*PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();*/
            ResponseUtils.writer(response,s);
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (request, response, exception) -> {

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }




}
