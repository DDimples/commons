package com.share.commons.cache.provider.mongo;

import com.share.commons.SpringContext;
import com.share.commons.cache.Cache;
import com.share.commons.cache.CacheException;
import com.share.commons.cache.CacheProvider;
import com.share.commons.cache.config.CacheSetting;
import com.mongodb.Mongo;
import com.share.commons.cache.CacheProvider;
import com.share.commons.client.mongo.MongoClient;
import com.share.commons.log.impl.LogUtil;

public class MongoCacheProvider implements CacheProvider {

	@Override
	public String name() {
		return "mongodb";
	}

	@Override
	public Cache buildCache(CacheSetting setting) throws CacheException {
		Mongo mongo = getMongo();
		return mongo != null ? new MongoCache(setting, new MongoClient(mongo,
				setting.getRegion(), setting.getConfigKey())) : null;
	}

	private Mongo getMongo() {
		Mongo mongo = null;
		try {
			mongo = SpringContext.getBean("mongo");
		} catch (Exception e) {
			LogUtil.getApplicationLogger().warn(
					"error in get mongo by beanname [mongo]", e);
		}
		if (null != mongo) {
			return mongo;
		}
		try {
			mongo = SpringContext.getBean(Mongo.class);
		} catch (Exception e) {
			LogUtil.getApplicationLogger().warn(
					"error in get mongo by beantype [com.mongodb.Mongo.class]",
					e);
		}
		return mongo;

	}

	@Override
	public void start() throws CacheException {
		// spring would have it done
	}

	@Override
	public void stop() {
		// spring will have it done
	}

}
