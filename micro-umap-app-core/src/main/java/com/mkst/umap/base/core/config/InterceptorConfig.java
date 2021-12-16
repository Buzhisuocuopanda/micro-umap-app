package com.mkst.umap.base.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mkst.umap.base.core.interceptor.TokenAuthInterceptor;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 拦截所有请求
        registry.addInterceptor(tokenAuthInterceptor()).addPathPatterns("/api/**");
    }

    @Bean
    public TokenAuthInterceptor tokenAuthInterceptor()
    {
        return new TokenAuthInterceptor();
    }


}