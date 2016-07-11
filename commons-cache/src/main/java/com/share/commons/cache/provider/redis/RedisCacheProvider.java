package com.share.commons.cache.provider.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.share.commons.SpringContext;
import com.share.commons.cache.Cache;
import com.share.commons.cache.CacheException;
import com.share.commons.cache.CacheProvider;
import com.share.commons.cache.config.CacheSetting;
import com.share.commons.client.redis.RedisClient;
import com.share.commons.client.redis.config.RedisConfig;
import com.share.commons.log.impl.LogUtil;

public class RedisCacheProvider implements CacheProvider {

	private RedisConfig config;
	private RedisClient client;
	private final Map<CacheSetting, RedisCache> cacheManager = new ConcurrentHashMap<CacheSetting, RedisCache>();

	@Override
	public String name() {
		return "redis";
	}

	@Override
	public Cache buildCache(CacheSetting setting) throws CacheException {
		RedisCache redisCache = cacheManager.get(setting);
		if (redisCache == null) {
			try {
				if (config == null) {
					return null;
				}
				synchronized (cacheManager) {
					redisCache = cacheManager.get(setting);
					if (redisCache == null) {
						if (client == null) {
							client = new RedisClient(config);
						}
						redisCache = new RedisCache(setting, client);
						// cache= cache.getClient();
						cacheManager.put(setting, redisCache);
					}
				}
			} catch (Exception e) {
				throw new CacheException(e);
			}
		}
		return redisCache;// redisCache.getClient();
	}

	@Override
	public void start() throws CacheException {
		if (config == null) {
			config = (RedisConfig) SpringContext.getBean("redisconfig");
		}
	}

	@Override
	public void stop() {
		synchronized (cacheManager) {
			for (RedisCache redisCache : cacheManager.values()) {
				try {
					redisCache.destroy();
				} catch (Exception e) {
					LogUtil.getApplicationLogger().error(
							"error in destory " + name() + " " + redisCache, e);
				}
			}
		}
	}

}
