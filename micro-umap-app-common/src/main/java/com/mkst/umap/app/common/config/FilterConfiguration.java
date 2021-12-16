package com.mkst.umap.app.common.config;

import com.mkst.umap.app.common.filter.sensitive.SenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @ClassName FilterConfiguration
 * @Description 过滤器config
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-21 17:28
 */
@Configuration
public class FilterConfiguration {

    @Bean
    FilterRegistrationBean<SenFilter> senFilterFilterRegistrationBean(){

        // 将需要过滤的url放到这里，一般只过滤对外的接口
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("/api/intelProperty/*");

        FilterRegistrationBean<SenFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new SenFilter());
        bean.setUrlPatterns(urlList);
        bean.setOrder(99899);
        bean.setName("senFilter");
        return bean;
    }

}
