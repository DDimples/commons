package com.share.commons.client.redis.extend;

import com.share.commons.client.redis.provider.shared.ExtendShardInfo;
import com.share.commons.client.redis.config.RedisConnection;
import com.share.commons.log.impl.LogUtil;
import redis.clients.jedis.Jedis;

/**
 * extend jedis,append host port modified by Xiang.Liu
 * 
 * @author Liwl
 *
 */
public class ExtendJedis extends Jedis {

	public ExtendJedis(ExtendShardInfo info) {
		super(info.getHost(), info.getPort(), info.getTimeout());
	}

	public ExtendJedis(String host, int port, int timeout) {
		super(host, port, timeout);
	}

	public ExtendJedis(RedisConnection redisConnection) {
		super(redisConnection.getHost(), redisConnection.getPort(),
				redisConnection.getTimeout());
	}

	public String getHost() {
		return client.getHost();
	}

	private int version;

	public int getPort() {
		return client.getPort();
	}

	private Object poolObject;

	public Object getPoolObject() {
		return poolObject;
	}

	public void setPoolObject(Object poolObject) {
		this.poolObject = poolObject;
	}

	/**
	 * never throw exception
	 */
	@Override
	public void disconnect() {
		try {
			super.disconnect();
		} catch (Exception e) {
			LogUtil.getApplicationLogger().error("error in disconnect", e);
		}
	}

	/**
	 * whether an exception has been occur
	 */
	public boolean broken() {
		return broken;
	}

	/**
	 * mark, an exception has been occur, this client should be destroy
	 */
	public void unable() {
		this.broken = true;
	}

	private boolean broken = false;

	@Override
	public String toString() {
		return this.getHost() + ":" + this.getPort() + "@" + this.hashCode();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
