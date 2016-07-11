package com.share.commons.client.redis.provider.parallel;

import com.share.commons.client.redis.config.AbstractConfigParser;
import com.share.commons.client.redis.config.ClusterConfig;
import com.share.commons.client.redis.config.RedisConnection;
import com.share.commons.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ParallelConfigParser extends AbstractConfigParser {
	public Map<String, List<List<RedisConnection>>> getParallelConnectionPool(
			ClusterConfig config) {
		String hostLists = config.getHostList();
		Map<String, List<List<RedisConnection>>> conMap = new HashMap<String, List<List<RedisConnection>>>();
		if (StringUtil.isNotBlank(hostLists)) {
			String[] hostListArray = hostLists.split("\\s*;\\s*");
			for (String hostList : hostListArray) {
				Map<String, List<RedisConnection>> connections = getConnections(
						hostList, config, true);
				if (connections != null) {
					for (Entry<String, List<RedisConnection>> entry : connections
							.entrySet()) {
						List<List<RedisConnection>> list = conMap.get(entry
								.getKey());
						if (list == null) {
							list = new LinkedList<List<RedisConnection>>();
							conMap.put(entry.getKey(), list);
						}
						list.add(entry.getValue());
					}
				}
			}
		}
		return conMap;
	}
}
