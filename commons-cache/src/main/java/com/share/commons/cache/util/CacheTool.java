package com.share.commons.cache.util;

import com.share.commons.cache.CacheClient;
import com.share.commons.cache.config.CacheItemConfig;
import com.share.commons.log.impl.LogUtil;

public class CacheTool {

	/**
	 * 将所有获取缓存的地方统一起来
	 */

	public static Object getObjectFromCache(String region, String configkey,
			String key) {
		try {
			CacheClient client = getCacheClient(region, configkey);
			if (client != null) {
				return client.get(key).getValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Get Cache error for key=" + key, e);
		}
		return null;
	}

	public static Object getObjectFromCache(CacheItemConfig config, String key) {
		try {
			CacheClient client = getCacheClient(config);
			if (client != null) {
				return client.get(key).getValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Get Cache error for key=" + key, e);
		}
		return null;
	}

	public static Object getObjectFromCache(CacheClient client, String key) {
		try {
			if (client != null) {
				return client.get(key).getValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Get Cache error for key=" + key, e);
		}
		return null;
	}

	/**
	 * 将所有写缓存的地方统一起来
	 */

	public static boolean setObject2Cache(String region, String configkey,
			String key, Object obj) {
		try {
			CacheClient client = getCacheClient(region, configkey);
			if (obj != null && client != null) {
				client.set(key, obj);
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	public static boolean setObject2Cache(CacheItemConfig config, String key,
			Object obj) {
		try {
			CacheClient client = getCacheClient(config);
			if (obj != null && client != null) {
				client.set(key, obj);
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	public static boolean setObject2Cache(CacheClient client, String key,
			Object obj) {
		try {
			if (obj != null && client != null) {
				client.set(key, obj);
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	/**
	 * 移除缓存
	 */

	public static boolean removeValueForCache(String region, String configkey,
			String key) {
		try {
			CacheClient client = getCacheClient(region, configkey);
			client.remove(key);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	public static boolean removeValueForCache(CacheItemConfig config, String key) {
		try {
			CacheClient client = getCacheClient(config);
			client.remove(key);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	public static boolean removeValueForCache(CacheClient client, String key) {
		try {
			if (client != null) {
				client.remove(key);
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getCommonLogger()
					.error("Set Cache error for key=" + key, e);
		}
		return false;
	}

	/**
	 * 获取客户端
	 */

	public static CacheClient getCacheClient(String region, String configkey) {
		try {
			CacheClient client = CacheClient.getInstance(region, configkey);
			if (client != null) {
				return client;
			}
		} catch (Exception e) {
			LogUtil.getCommonLogger().error(
					"Get CacheClient Error,region=" + region + ",configkey="
							+ configkey, e);
		}
		return null;
	}

	public static CacheClient getCacheClient(CacheItemConfig config) {
		try {
			CacheClient client = CacheClient.getInstance(config);
			if (client != null) {
				return client;
			}
		} catch (Exception e) {
			LogUtil.getCommonLogger().error(
					"Get CacheClient Error,region=" + config.getRegion()
							+ ",configkey=" + config.getConfigKey(), e);
		}
		return null;
	}
}
