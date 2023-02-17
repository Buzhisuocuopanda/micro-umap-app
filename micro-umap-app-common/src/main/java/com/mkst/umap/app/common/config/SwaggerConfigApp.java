package com.mkst.umap.app.common.config;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.mkst.mini.systemplus.common.config.Global;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
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
                .apis(RequestHandlerSelectors.basePackage("com.mkst.umap.app.admin.api"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(getParameterList());
    }

    @Bean
    public Docket mallApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("商城接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.mkst.umap.app.mall.api"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(getParameterList());
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

    
	/**
	 * 添加head参数配置
	 * 
	 * @return
	 */
    private List<Parameter> getParameterList() {
        ParameterBuilder clientIdTicket = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        clientIdTicket.name("is-test").description("测试免认证")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build(); //设置false，表示参数 非必填,可传可不传！
        pars.add(clientIdTicket.build());
        return pars;
    }
}
