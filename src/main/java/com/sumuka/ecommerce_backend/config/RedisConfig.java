package com.sumuka.ecommerce_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import java.time.Duration;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.url}")
    private String redisUrl;

@Bean
public LettuceConnectionFactory redisConnectionFactory() throws URISyntaxException {
    URI redisUri = new URI(redisUrl);

    String host = redisUri.getHost();
    int port = redisUri.getPort();
    String userInfo = redisUri.getUserInfo();
    String password = userInfo != null && userInfo.contains(":") ? userInfo.split(":", 2)[1] : null;

    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(host);
    config.setPort(port);
    if (password != null) {
        config.setPassword(RedisPassword.of(password));
    }

    // ✅ ENABLE SSL for Upstash (rediss://)
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .useSsl()
            .build();

    return new LettuceConnectionFactory(config, clientConfig);
}
    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws URISyntaxException {
        RedisTemplate<String, Object> redis = new RedisTemplate<>();
        redis.setConnectionFactory(redisConnectionFactory());
        return redis;
    }
}
