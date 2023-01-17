package com.safeapp.admin.conf;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Component;

import com.safeapp.admin.web.service.RedisCacheService;

@Component
public class RedisCacheWriterImpl implements org.springframework.data.redis.cache.RedisCacheWriter {

	private final RedisCacheService redisCacheService;

	@Autowired
	public RedisCacheWriterImpl(RedisCacheService redisCacheService) {
		this.redisCacheService = redisCacheService;
	}

	@Override
	public void put(String name, byte[] key, byte[] value, Duration ttl) {
		if (Objects.isNull(ttl)) {
			return;
		}
		redisCacheService.setValue(key.toString(), value.toString(), ttl.getSeconds());
	}

	@Override
	public byte[] get(String name, byte[] key) {
		String value = redisCacheService.getValue(key.toString());
		return value.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
		redisCacheService.setValue(key.toString(), value.toString(), ttl.getSeconds());
		return this.get(name, key);
	}

	@Override
	public void remove(String name, byte[] key) {
	}

	@Override
	public void clean(String name, byte[] pattern) {
	}

    @Override
    public CacheStatistics getCacheStatistics(String cacheName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearStatistics(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public org.springframework.data.redis.cache.RedisCacheWriter withStatisticsCollector(CacheStatisticsCollector cacheStatisticsCollector) {
        // TODO Auto-generated method stub
        return null;
    }
}
