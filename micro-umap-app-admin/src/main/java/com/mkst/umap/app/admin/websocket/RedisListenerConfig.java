package com.mkst.umap.app.admin.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory){

        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();

        listenerContainer.setConnectionFactory(connectionFactory);
        return listenerContainer;
    }

    @Bean
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer listenerContainer){

        return new RedisListener(listenerContainer);
    }
}
