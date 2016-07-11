package com.share.commons.cache.provider.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.share.commons.cache.Cache;
import com.share.commons.cache.CacheException;
import com.share.commons.cache.config.CacheExpire;
import com.share.commons.cache.config.CacheSetting;
import com.share.commons.client.redis.RedisClient;
import com.share.commons.util.StringUtil;


public class RedisCache implements Cache {
	protected final CacheSetting setting;
	protected final char splitTag;
	protected final RedisClient client;
	protected final boolean sliding;

	public RedisCache(CacheSetting setting, RedisClient client) {
		this.setting = setting;
		this.client = client;
		splitTag = client.getSplitTag(setting.getRegion()).charAt(0);
		sliding = setting.getCacheExpire() == CacheExpire.SlidingTime;
	}

	@Override
	public CacheSetting getSetting() {
		return this.setting;
	}

	@Override
	public Object get(String key) throws CacheException {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key.toString());
		try {
			Object b = client.get(setting.getRegion(), cacheKey);
			if (b != null && sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
			}
			return b;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void put(String key, Object value) throws CacheException {
		if (value == null) {
			remove(key);
		} else {
			String cacheKey = StringUtil.joinString(splitTag,
					setting.getConfigKey(), key.toString());
			try {
				client.setex(setting.getRegion(), cacheKey,
						setting.getExpireTime(), value);
			} catch (Exception e) {
				throw new CacheException(e);
			}
		}
	}

	@Override
	public void update(String key, Object value) throws CacheException {
		put(key, value);
	}

	@Override
	public List<?> keys() throws CacheException {

		// Twemproxy不支持keys
		/*
		 * RedisClient client = RedisCacheProvider.getClient(); ShardedJedis
		 * jedis = client.borrow(setting.getRegion(), RedisOption.Read);
		 * Set<byte[]> list; try { List<Object> keys = new ArrayList<Object>();
		 * list = jedis.hkeys(String.valueOf("*").getBytes()); if (null != list
		 * && list.size() > 0) { for (byte[] bs : list) { keys.add(bs == null ?
		 * null : byte2obj(bs)); } } return keys; } catch (Exception e) {
		 * 
		 * throw new CacheException(e); } finally {
		 * 
		 * client.giveback(jedis, setting.getRegion(), RedisOption.Read); }
		 */
		throw new CacheException("unsafe method:keys");
	}

	private void remove(Object key, boolean batch) throws CacheException {

		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key.toString());
		try {
			client.expire(setting.getRegion(), cacheKey, 0);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void remove(String key) throws CacheException {
		remove(key, false);
	}

	@Override
	public void clear() throws CacheException {
		throw new CacheException("unsafe method:clear");
	}

	@Override
	public void destroy() throws Exception {
		client.close();
	}

	@Override
	public Map<String, Object> getMap(String key, String... fields) {
		Map<String, Object> map = null;
		String cacheKey = getCacheKey(key);
		try {
			if (fields.length == 0) {
				map = client.hgetAll(setting.getRegion(), cacheKey);
			} else {
				List<Object> list = client.hmget(setting.getRegion(), cacheKey,
						fields);
				if (list != null && list.size() > 0) {
					map = new HashMap<String, Object>(list.size());
					for (int i = 0; i < fields.length; i++) {
						map.put(fields[i], list.get(i));
					}
				}
			}
			if (map != null && !map.isEmpty() && sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
			}
			return map;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void setMap(String key, String field, Object value) {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			client.hset(setting.getRegion(), cacheKey, field, value);
			client.expire(setting.getRegion(), cacheKey,
					setting.getExpireTime());

		} catch (Exception e) {

			throw new CacheException(e);

		} finally {

		}

	}

	@Override
	public List<?> mget(String... keys) throws CacheException {

		List<String> cacheKeys = new ArrayList<String>(keys.length);
		String cacheKey;
		for (Object key : keys) {
			cacheKey = StringUtil.joinString(splitTag, setting.getConfigKey(),
					key.toString());
			cacheKeys.add(cacheKey);
		}
		try {
			List<Object> b = client.mget(setting.getRegion(),
					cacheKeys.toArray(new String[cacheKeys.size()]));
			if (b != null && sliding) {
				for (int i = 0, j = cacheKeys.size(); i < j; i++) {
					Object o = b.get(i);
					if (o == null
							|| (String.class.isInstance(o) && "nil"
									.equalsIgnoreCase((String) o))) {
						// nil
					} else {
						client.expire(setting.getRegion(), cacheKeys.get(i),
								setting.getExpireTime());
					}
				}
			}
			return b;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void setMap(String key, Map<String, Object> value) {

		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			client.hmset(setting.getRegion(), cacheKey, value);
			client.expire(setting.getRegion(), cacheKey,
					setting.getExpireTime());

		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
		}
	}

	@Override
	public void lpush(String key, Object... value) {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			client.lpush(setting.getRegion(), cacheKey, value);
			client.expire(setting.getRegion(), cacheKey,
					setting.getExpireTime());

		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
		}
	}

	@Override
	public Object lpop(String key) {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			Object obj = client.lpop(setting.getRegion(), cacheKey);
			if (obj != null && sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
			}
			return obj;
		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
		}
	}

	@Override
	public Object hget(String key, String field) {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			Object obj = client.hget(setting.getRegion(), cacheKey, field);
			if (obj != null && sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
			}
			return obj;
		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
		}
	}

	@Override
	public List<Object> hmget(String key, String... fields) {
		String cacheKey = StringUtil.joinString(splitTag,
				setting.getConfigKey(), key);
		try {
			List<Object> obj = client.hmget(setting.getRegion(), cacheKey,
					fields);
			if (obj != null && sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
			}
			return obj;
		} catch (Exception e) {
			throw new CacheException(e);
		} finally {
		}
	}

	protected String getCacheKey(String key) {
		return StringUtil.joinString(splitTag, setting.getConfigKey(),
				key.toString());
	}

	@Override
	public void hmset(String key, Map<String, Object> hash) {
		setMap(key, hash);

	}

	@Override
	public void hset(String key, String field, Object value) {
		setMap(key, field, value);
	}

	@Override
	public boolean hsupport() {
		return true;
	}

	@Override
	public boolean msupport() {
		return true;
	}

	@Override
	public boolean hmsupport() {
		return true;
	}

	@Override
	public boolean lsupport() {
		return true;
	}

	@Override
	public boolean incrsupport() {
		return true;
	}

	@Override
	public long incr(String key) throws CacheException {
		String cacheKey = getCacheKey(key);
		try {
			long b = client.incr(setting.getRegion(), cacheKey);
//			if (sliding) {
				client.expire(setting.getRegion(), cacheKey,
						setting.getExpireTime());
//			}
			return b;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}
}
