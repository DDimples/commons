package com.share.commons.client.redis.provider;

 
import com.share.commons.client.redis.config.ClusterConfig;
import com.share.commons.client.redis.extend.ExtendJedisCommands;
import com.share.commons.client.redis.provider.parallel.Parallel;
import com.share.commons.client.redis.provider.shared.Sharded;
import com.share.commons.client.redis.provider.twemproxy.Twemproxy;

public class CommandFactory {

	public static ExtendJedisCommands getCommand(ClusterConfig config) {
		ExtendJedisCommands command =null;

		switch (config.getClusterType()) {
		case Twemproxy: 
           command = new Twemproxy(config);
			break;
		case Sharded:
			 command = new Sharded(config);
			break;
		case ParallelWrite:
			 command = new Parallel(config);
			break;
		default:
			break;
		}
		return command;
	}

}
