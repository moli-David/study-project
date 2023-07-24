package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //以下是验证请求拦截和放行配置
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();  //将所有请求全部拦截，一律需要验证
                })
                //以下是表单登录相关配置
                .formLogin(conf -> {
                    conf.loginPage("/api/auth/login"); //将登录页面设置为自己的登录页面
//                    conf.loginProcessingUrl("/doLogin");  //登录表单提交的地址，可以自定义
                    conf.defaultSuccessUrl("/");  //登录成功后跳转的页面
                    conf.successHandler(this::onAuthenticationSuccess);  //登录成功
                    conf.failureHandler(this::onAuthenticationFailure);  //登录失败
                    conf.permitAll();  //登录地址放行
                    //用户名和密码的表单字段
                    conf.usernameParameter("username");
                    conf.passwordParameter("password");
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");  //退出登录页面
                    conf.logoutSuccessUrl("/api/auth/login");  //退出登录成功后返回的页面
                    conf.permitAll();  //放行
                })
                .userDetailsService(authorizeService)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(conf -> {  //没有权限访问其他页面的时候
                    conf.authenticationEntryPoint(this::commence);
                })
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //登录成功处理
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }

    //登录失败处理
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }

    //未登录访问其他页面
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, authException.getMessage())));
    }
}
