package com.binoofactory.cornsqure.web.service;

public interface RedisCacheService {
	void setValue(String key, Object value, long secondes);

	String getValue(String key);
}
