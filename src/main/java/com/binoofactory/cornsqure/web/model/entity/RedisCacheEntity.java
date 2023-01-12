package com.binoofactory.cornsqure.web.model.entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@RedisHash("cacheId")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisCacheEntity {
	@Id
	private Long cacheId;
	private String key;
	private Object value;
}
