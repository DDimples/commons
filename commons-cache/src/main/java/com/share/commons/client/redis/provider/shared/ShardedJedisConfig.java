package com.share.commons.client.redis.provider.shared;


import com.share.commons.client.redis.extend.JedisConfig;

public class ShardedJedisConfig extends JedisConfig {

	public ShardedJedisConfig(String host, int port) {
		super(host, port);
	}

	public ShardedJedisConfig(String host, int port, int timeout) {
		super(host, port, timeout);
	}

	private int weight;

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}
}
