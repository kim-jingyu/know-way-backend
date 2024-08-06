package com.knowway.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig<K extends String, V extends String> {

  @Value("${spring.data.redis.password}")
  private String password;
  @Value("${spring.data.redis.host}")
  private String host;
  @Value("${spring.data.redis.port}")
  private int redisPort;


  @Qualifier("redisTemplate")
  @Bean
  public RedisTemplate<String,String> blackListRedisFactory(
      @Qualifier("blackListRedisFactory") RedisConnectionFactory blackListRedisFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(blackListRedisFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
  @Qualifier("refreshRedisTemplate")
  @Bean
  public RedisTemplate<K, V> refreshRedisTemplate(
      @Qualifier("refreshRedisFactory") RedisConnectionFactory blackListRedisFactory) {
    RedisTemplate<K, V> template = new RedisTemplate<>();
    template.setConnectionFactory(blackListRedisFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }


  @Qualifier("refreshRedisFactory")
  @Bean
  public RedisConnectionFactory refreshRedisFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(redisPort);
    redisStandaloneConfiguration.setPassword(password);
    redisStandaloneConfiguration.setDatabase(15);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Qualifier("blackListRedisFactory")
  @Bean
  public RedisConnectionFactory blackListRedisFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(redisPort);
    redisStandaloneConfiguration.setPassword(password);
    redisStandaloneConfiguration.setDatabase(14);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }
}
