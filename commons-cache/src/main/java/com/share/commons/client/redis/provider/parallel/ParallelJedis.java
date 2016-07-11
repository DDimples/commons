package com.share.commons.client.redis.provider.parallel;

import com.share.commons.client.redis.extend.ExtendJedis;


public class ParallelJedis extends ExtendJedis {
	private final ParallelStatus parallelStatus;

	public ParallelJedis(ParallelStatus parallelStatus) {
		super(parallelStatus.getRedisConnection());
		this.parallelStatus = parallelStatus;
	}

	public ParallelStatus getParallelStatus() {
		return parallelStatus;
	}

}
