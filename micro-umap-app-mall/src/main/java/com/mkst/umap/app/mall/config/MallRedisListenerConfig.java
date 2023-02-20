package com.mkst.umap.app.mall.config;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.mkst.umap.app.mall.listener.RedisKeyExpirationListener;
import com.mkst.umap.app.mall.service.OrderInfoService;

/**
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 */
@Configuration
@AllArgsConstructor
public class MallRedisListenerConfig {

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisConfigProperties redisConfigProperties;
	private final OrderInfoService orderInfoService;

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(new RedisKeyExpirationListener(redisTemplate, redisConfigProperties, orderInfoService), new PatternTopic(StrUtil.format("__keyevent@{}__:expired", redisConfigProperties.getDatabase())));
		return container;
	}
}

