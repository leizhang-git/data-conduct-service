package cn.lz.data.bootstrap.config;

import cn.lz.data.bootstrap.config.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:09
 */
@Configuration
public class SecurityConfiguration {
    @Bean
    public FilterRegistrationBean<?> registerCORSFilter(){
        FilterRegistrationBean<CORSFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(this.corsFilter());
        registration.setName("corsFilter");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<?> registrationBodyReqFilter() {
        FilterRegistrationBean<BodyReqFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(this.bodyReqFilter());
        registration.setName("bodyReqFilter");
        registration.setOrder(1);
        return registration;
    }
    //
    @Bean
    public FilterRegistrationBean<?> registrationUrlPatternFilter() {
        FilterRegistrationBean<UrlPatternFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(this.urlPatternFilter());
        registration.setName("urlPatternFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<?> registrationJWTFilter() {
        FilterRegistrationBean<JWTFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter());
        registration.setName("jwtFilter");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<?> registrationInvocationFilter() {
        FilterRegistrationBean<InvocationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(this.invocationFilter());
        registration.setName("invocationFilter");
        registration.setOrder(4);
        return registration;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter();
    }

    @Bean
    public InvocationFilter invocationFilter(){
        return new InvocationFilter();
    }

    @Bean
    public BodyReqFilter bodyReqFilter(){
        return new BodyReqFilter();
    }

    @Bean
    public UrlPatternFilter urlPatternFilter(){
        return new UrlPatternFilter();
    }

    @Bean
    public CORSFilter corsFilter(){
        return new CORSFilter();
    }
}
