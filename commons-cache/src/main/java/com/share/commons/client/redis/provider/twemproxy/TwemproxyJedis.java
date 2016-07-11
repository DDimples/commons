package com.share.commons.client.redis.provider.twemproxy;


import com.share.commons.client.redis.extend.ExtendJedis;

public class TwemproxyJedis extends ExtendJedis {

	public TwemproxyJedis(TwemproxyStatus twemproxyStatus) {
		super(twemproxyStatus.getJedisInfo());
		this.twemproxyStatus = twemproxyStatus;
	}

	private final TwemproxyStatus twemproxyStatus;

	public TwemproxyStatus getTwemproxyStatus() {
		return twemproxyStatus;
	}

}
