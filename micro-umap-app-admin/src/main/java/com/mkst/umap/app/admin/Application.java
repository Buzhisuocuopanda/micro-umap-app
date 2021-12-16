package com.mkst.umap.app.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import com.mkst.mini.systemplus.common.config.Global;

/**
 * 启动程序
 * 
 * @author admin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScans(value = {@ComponentScan("com.mkst.mini.systemplus"), @ComponentScan("com.mkst.umap")})
@MapperScan("com.mkst.**.mapper")
public class Application {
    public static void main(String[] args){
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n======================================================================\r\n");
        sb.append("\r\n    欢迎使用 "+ Global.getName()+" Version: "+Global.getVersion()+ "- Powered By http://www.szmkst.com\r\n");
        sb.append("\r\n======================================================================\r\n");
        System.out.println(sb.toString());
    } 
    
}