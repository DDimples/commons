package com.share.commons.client.redis.provider.twemproxy;

import com.share.commons.client.redis.config.ClusterConfig;
import com.share.commons.client.redis.provider.PoolProvider;

public class Twemproxy extends PoolProvider<TwemproxyJedis> {

	public Twemproxy(ClusterConfig config) {
		super(config);
		pool = new TwemproxyClientPool(config);
	}

}
