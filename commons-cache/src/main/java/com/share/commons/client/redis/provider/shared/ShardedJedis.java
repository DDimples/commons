package com.share.commons.client.redis.provider.shared;


import com.share.commons.client.redis.extend.ExtendJedis;

public class ShardedJedis extends ExtendJedis {

	public ShardedJedis(ExtendShardInfo extendShardInfo) {
		super(extendShardInfo);
		this.extendShardInfo = extendShardInfo;
	}

	private final ExtendShardInfo extendShardInfo;

	public ExtendShardInfo getExtendShardInfo() {
		return extendShardInfo;
	}

}
