package com.mkst.umap.app.common.config;


import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.mkst.mini.systemplus.api.common.config.SwaggerConfig;
import com.mkst.mini.systemplus.common.config.Global;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfigApp{

    @Bean
    public Docket appApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("app接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.mkst.umap.app"))
                .paths(PathSelectors.any()).build();
    }



    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo()
    {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder().title("系统接口文档").description("描述：用于系统接口调用说明文档")
                .contact(new Contact(Global.getName(), null, null)).version("版本号:" + Global.getVersion()).build();
    }

}
