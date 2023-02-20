/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * reids相关配置
 *
 * @author
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperties {

	private String host = "host";
	private String port = "port";
	private String password = "password";
	private String database = "database";

}
