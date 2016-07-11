package com.share.commons.client.redis.provider.shared;


import com.share.commons.client.redis.config.ClusterConfig;
import com.share.commons.client.redis.extend.ExtendJedis;
import com.share.commons.client.redis.provider.PoolProvider;

public class Sharded extends PoolProvider<ExtendJedis> {

	public Sharded(ClusterConfig config) {
		super(config);
		pool = new ShardedPool(config);
	}

	 
}
