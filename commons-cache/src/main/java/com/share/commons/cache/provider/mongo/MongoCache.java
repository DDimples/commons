package com.share.commons.cache.provider.mongo;

import java.util.concurrent.TimeUnit;

import com.share.commons.cache.AbstractCache;
import com.share.commons.cache.CacheException;
import com.share.commons.cache.config.CacheExpire;
import com.share.commons.cache.config.CacheSetting;
import com.share.commons.client.mongo.MongoClient;

public class MongoCache extends AbstractCache {
	private final CacheSetting setting;
	private final MongoGetter mongoGetter;
	private final MongoClient mongoClient;

	private class MongoGetter {
		Object get(String key) {
			return mongoClient.get(key);
		}
	}

	private class SlidingMongoGetter extends MongoGetter {
		private final long expire;

		public SlidingMongoGetter(long expire) {
			this.expire = expire;
		}

		@Override
		Object get(String key) {
			return mongoClient.get(key, expire);
		}
	}

	public MongoCache(CacheSetting setting, MongoClient mongoClient) {
		this.mongoClient = mongoClient;
		this.setting = setting;
		this.mongoGetter = this.setting.getCacheExpire() == CacheExpire.AbsoluteTime ? new MongoGetter()
				: new SlidingMongoGetter(TimeUnit.SECONDS.toMillis(this.setting
						.getExpireTime()));
	}

	@Override
	public Object get(String key) throws CacheException {
		return mongoGetter.get(key);
	}

	@Override
	public void put(String key, Object value) throws CacheException {
		mongoClient.setex(key, value,
				TimeUnit.SECONDS.toMillis(setting.getExpireTime()));
	}

	@Override
	public void update(String key, Object value) throws CacheException {
		put(key, value);
	}

	@Override
	public void remove(String key) throws CacheException {
		mongoClient.remove(key);
	}

	@Override
	public void destroy() throws CacheException {

	}

	@Override
	public CacheSetting getSetting() {
		return this.setting;
	}

}
