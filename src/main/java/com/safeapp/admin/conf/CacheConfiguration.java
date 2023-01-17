package com.safeapp.admin.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfiguration.class);
	private final RedisCacheWriterImpl bfRedisCacheWriter;

	@Autowired
	public CacheConfiguration(RedisCacheWriterImpl bfRedisCacheWriter) {
		this.bfRedisCacheWriter = bfRedisCacheWriter;
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		RedisCacheConfiguration redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(customRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		return new RedisCacheManager(bfRedisCacheWriter, redisConfiguration, cacheConfigurations);
	}

	private static RedisSerializer customRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		return new RedisSerializer() {
			@Override
			public byte[] serialize(Object o) throws SerializationException {
				try {
					Map<String, String> cachingData = new HashMap<>();
					cachingData.put("type", o.getClass().getName());
					cachingData.put("data", objectMapper.writeValueAsString(o));
					return objectMapper.writeValueAsBytes(cachingData);
				} catch (IOException e) {
					LOGGER.error("캐시 생성 과정에서 오류가 발생하였습니다 : {}", e.getMessage());
					return null;
				}
			}

			@Override
			public Object deserialize(byte[] bytes) throws SerializationException {
				try {
					Map<String, String> cachingData = objectMapper.readValue(bytes, Map.class);
					Class classType = Class.forName(cachingData.get("type"));
					String data = cachingData.get("data");
					return objectMapper.readValue(data, classType);
				} catch (ClassNotFoundException e) {
					LOGGER.error("캐시 클래스 조회 과정에서 오류가 발생하였습니다. : {}", e.getMessage());
					return null;
				} catch (IOException e) {
					LOGGER.error("캐시 조회 후 파싱 과정에서 오류가 발생하였습니다. : {}", e.getMessage());
					return null;
				}
			}
		};
	}
}
