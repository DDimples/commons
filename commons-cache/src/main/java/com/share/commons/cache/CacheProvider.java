package com.share.commons.cache;


import com.share.commons.cache.config.CacheSetting;

public interface CacheProvider {
	public String name();

	Cache buildCache(CacheSetting setting)
			throws CacheException;

	void start() throws CacheException;

	void stop();
}
