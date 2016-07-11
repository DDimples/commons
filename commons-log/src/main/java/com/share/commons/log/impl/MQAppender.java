package com.share.commons.log.impl;

import com.share.commons.log.model.OnlineWebLog;
import com.share.commons.util.OSUtil;
import com.rabbitmq.client.*;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MQAppender extends AppenderSkeleton {
	/**
	 * default "mq4log"
	 */
	protected String queueName = "mq4log";
	private String host;
	/*
	 * usually 5672
	 */
	private int port = 5672;
	private String username;
	private String password;
	private String virtualHost = "/";
	/**
	 * block concurrent thread to publish the message if less or equals to 0
	 */
	private int asyncThreadNum = 0;
	private Appender appender;
	private Formater formater;
	/**
	 * cache message, default 1000
	 */
	private int maxBuffer = 1000;
	/**
	 * check period in millisecond, default 5 minutes
	 */
	private long period = TimeUnit.MINUTES.toMillis(5);
	/**
	 * init:Integer.MIN_VALUE --> 0<br>
	 * wait for a new command after checking:0 --> 1<br>
	 * order while sleeping:1 --> 0<br>
	 * close:set to -1
	 *
	 */
	private final AtomicInteger status = new AtomicInteger(Integer.MIN_VALUE);
	private final Semaphore semaphore = new Semaphore(0);
	private volatile long timetodie = Long.MAX_VALUE;
	private long delay = TimeUnit.SECONDS.toMillis(10);

	class ChannelKeeper implements Runnable {

		@Override
		public void run() {
			try {
				delay();
				while (status.get() >= 0) {
					final Appender currentappender = appender;
					if (currentappender != null && !currentappender.isLocal()
							&& currentappender.isOpen()) {
						// connection is OK
						LogUtil.prt("MQAppender connection is OK");
					} else {
						boolean failed = true;
						LogUtil.prt("MQAppender starting...");
						Appender appenderTemp = null;
						try {
							Channel channel = createChannel();
							if (channel != null) {
								int size = asyncThreadNum;
								appenderTemp = new ConnectedAppender(channel);
								if (size > 0) {
									appenderTemp = new Appender.AsyncAppender(size,
											Math.max(size, maxBuffer),
											appenderTemp);
								}
								LogUtil.prt("MQAppender started"
										+ (size <= 0 ? "" : " with async["
												+ size + "]"));
								failed = false;
							}
						} catch (Exception e) {
							LogUtil.prt("MQAppender failed: "
									+ LogUtil.getThrowableDetail(e));
						}
						if (failed) {
							if (currentappender == null
									|| !currentappender.isLocal()) {
								appenderTemp = getBufferedAppender();
							}
						}
						if (appenderTemp != null
								&& (appender = appenderTemp).isOpen()) {
							if (currentappender != null) {
								try {
									if (currentappender.isLocal()) {
										LogUtil.prt("flush buffer...");
										long bufferCount = appenderTemp
												.flush(currentappender);
										LogUtil.prt("flush buffer end, count:"
												+ bufferCount);
									}
								} catch (Exception e) {
									LogUtil.prt(e);
								}
								currentappender.close(1, TimeUnit.SECONDS);
							}
						} else {
							LogUtil.prt("MQAppeder failed, next try for "
									+ period + " millisecond");
						}
						timetodie = Long.MAX_VALUE;
					}
					if (status.compareAndSet(0, 1) || status.get() >= 0) {
						sleep();
						if (timetodie < System.currentTimeMillis()) {
							Appender deadappender = appender;
							if (deadappender != null) {
								synchronized (deadappender) {
									if (deadappender.isOpen()) {
										try {
											deadappender.close(1,
													TimeUnit.SECONDS);
										} catch (Exception e) {
											LogUtil.prt(e);
										}
									}
								}
							}
						}
					}
				}
			} finally {
				LogUtil.prt("closing");
				try {
					if (appender.isOpen()) {
						appender.close(1, TimeUnit.SECONDS);
					}
				} catch (Exception e) {
					LogUtil.prt(e);
				} finally {
					LogUtil.prt("closed");
				}
			}
		}
	}

	@Override
	public void activateOptions() {
		if (status.compareAndSet(Integer.MIN_VALUE, 0)) {
			Layout layout = getLayout();
			if (layout == null) {
				formater = new Formater();
			} else {
				formater = new Formater.LayoutFormater(layout);
			}
			appender = getBufferedAppender();
			Thread channelKeeper = new Thread(new ChannelKeeper(),
					"MQChannelKeeper_" + this.hashCode());
			channelKeeper.start();
		}
	}

	private Appender getBufferedAppender() {
		return maxBuffer > 0 ? new Appender.BufferedAppender(maxBuffer)
				: new Appender.RejectedAppender();
	}

	private Channel createChannel() throws Exception {
		Channel channel = null;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
		factory.setHost(host);
		factory.setPort(port);
		Connection connection = null;
		try {
			final Connection connectiontmp = factory.newConnection();
			connection = connectiontmp;
			connectiontmp.addBlockedListener(new BlockedListener() {

				@Override
				public void handleUnblocked() throws IOException {
					timetodie = Long.MAX_VALUE;
				}

				@Override
				public void handleBlocked(String reason) throws IOException {
					// in a period
					timetodie = Math.min(System.currentTimeMillis() + period,
							timetodie);
					wakeup();
				}

			});
			connectiontmp.addShutdownListener(new ShutdownListener() {
				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					wakeup();
				}
			});
			try {
				channel = connectiontmp.createChannel();
				channel.queueDeclare(queueName, true, false, false, null);
			} catch (Exception e) {
				if (channel != null) {
					channel.abort();
				}
				throw e;
			}
		} catch (Exception e) {
			if (connection != null) {
				connection.abort();
			}
			throw e;
		}
		if (!channel.isOpen()) {
			channel.abort();
			channel.getConnection().abort();
			return null;
		}
		return channel;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public int getAsyncThreadNum() {
		return asyncThreadNum;
	}

	public void setAsyncThreadNum(int asyncThreadNum) {
		this.asyncThreadNum = asyncThreadNum;
	}

	public int getMaxBuffer() {
		return maxBuffer;
	}

	public void setMaxBuffer(int maxBuffer) {
		this.maxBuffer = maxBuffer;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		if (period > 0) {
			this.period = period;
		}
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		if (delay > 0) {
			this.delay = delay;
		}
	}

	@Override
	public void close() {
		status.set(-1);
		wakeup();
	}

	/**
	 * disable super layout format
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		try {
			if (appender.isOpen()) {
				appender.publish(getBean(event));
			}
		} catch (Throwable e) {
			LogUtil.prt(e);
			// ignore all error!
			if (status.compareAndSet(1, 0)) {
				wakeup();
			}
		}
	}

	private OnlineWebLog getBean(LoggingEvent event) {
		try {
			if (event != null) {
				OnlineWebLog log = createLog();
				StringBuffer stackMsg = LogUtil.getMessageFromEvent(event);
				if (stackMsg != null) {
					log.setStackMessage(stackMsg.toString());
				}
				StackTraceElement[] traces = new Exception().getStackTrace();
				if (traces != null && traces.length > 10)
					log.setServiceName(traces[9].getClassName() + "."
							+ traces[9].getMethodName());
				log.setLogContent(formater.format(event));// 异常的说明 系统异常时填写
				log.setLogLevel(event.getLevel().toString());
				log.setLogerName(event.getLoggerName());
				return log;
			}
		} catch (Exception e) {
			LogUtil.prt(e);
		}
		return null;
	}

	public static OnlineWebLog createLog() {
		OnlineWebLog log = new OnlineWebLog();
		log.setServerName(OSUtil.getLocalName());
		log.setServerIp(OSUtil.getLocalIP());
		// with 6 more properties clone will be faster obviously, keep clone
		// as an implement
		// OnlineWebLog log = (OnlineWebLog) cacheLog.clone();
		// consider if the initializing of log4j before initializing of
		// LogBean
		log.setAppName(LogHolder.getAppName());// 应用名
		log.setBusinessLine(LogHolder.getBusinessLine());// 产品线
		log.setLogTime(new Date());
		log.setThreadID(String.valueOf(Thread.currentThread().getId()));
		log.setServiceName(LogHolder.getActionName());
		log.setPageUrl(LogHolder.getUrl());
		log.setQueryString(LogHolder.getQueryString());
		log.setClientIP(LogHolder.getClientIp());
		return log;
	}

	private class ConnectedAppender extends Appender {
		protected final Channel channel;
		protected volatile int state = 3;

		ConnectedAppender(Channel channel) {
			if (channel == null) {
				throw new NullPointerException("channel could not be null");
			}
			if (queueName == null) {
				throw new NullPointerException("queueName could not be null");
			}
			this.channel = channel;
		}

		@Override
		protected boolean isOpen() {
			if (state < 0) {
				return false;
			}
			boolean isopen = channel.isOpen();
			if (!isopen && !(state < 0) && status.compareAndSet(1, 0)) {
				wakeup();
			}
			return isopen;
		}

		@Override
		protected synchronized void close(long time, TimeUnit unit) {
			if (state >= 0) {
				state = -1;
				wakeup();
				try {
					channel.abort();
					channel.getConnection().close((int) unit.toMillis(time));
				} catch (Exception e) {
					LogUtil.prt(e);
				}
			}
		}

		@Override
		protected boolean isLocal() {
			return false;
		}

		@Override
		void publish(Serializable obj) {
			if (obj != null) {
				byte[] bs = null;
				try {
					bs = SerializationUtils.serialize(obj);
				} catch (Exception e) {
					LogUtil.prt(e);
				}
				if (bs != null && isOpen()) {
					try {
						channel.basicPublish("", queueName, true, null, bs);
						reset();
					} catch (Exception e) {
						if (state > 0) {
							synchronized (this) {
								if (state > 0) {
									state--;
									if (state == 0) {
										this.close(0, TimeUnit.SECONDS);
									}
								}
							}
						}
						LogUtil.prt(e);
					}
				}
			}
		}

		void reset() {
			if (state < 3) {
				synchronized (this) {
					if (state < 3 && state > 0) {
						state = 3;
					}
				}
			}
		}
	}

	void delay() {
		try {
			if (semaphore.tryAcquire(delay, TimeUnit.MILLISECONDS)) {
				semaphore.drainPermits();
			}
		} catch (InterruptedException e) {
			LogUtil.prt(e);
		}
	}

	void sleep() {
		try {
			if (semaphore.tryAcquire(period, TimeUnit.MILLISECONDS)) {
				semaphore.drainPermits();
			}
		} catch (InterruptedException e) {
			LogUtil.prt(e);
		}
	}

	void wakeup() {
		semaphore.release();
	}
}
