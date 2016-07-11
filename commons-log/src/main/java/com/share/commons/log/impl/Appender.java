package com.share.commons.log.impl;

import java.io.Serializable;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class Appender {

	abstract boolean isLocal();

	abstract boolean isOpen();

	abstract void publish(Serializable obj);

	abstract void close(long time, TimeUnit unit);

	protected Serializable getCache() {
		return null;
	}

	/**
	 * flush buffered cache of the other appender
	 * 
	 * @param appender
	 * @return
	 */
	long flush(final Appender appender) {
		long bufferCount = 0;
		Serializable obj = null;
		while ((obj = appender.getCache()) != null) {
			this.publish(obj);
			bufferCount++;
		}
		return bufferCount;
	}

	public static class RejectedAppender extends Appender {

		@Override
		protected boolean isLocal() {
			return true;
		}

		@Override
		protected boolean isOpen() {
			return false;
		}

		@Override
		void close(long time, TimeUnit unit) {
		}

		@Override
		void publish(Serializable obj) {
			// do nothing
		}

	}

	public static class BufferedAppender extends Appender {
		@Override
		boolean isLocal() {
			return true;
		}

		public BufferedAppender(int maxCount) {
			capt.set(maxCount);
		}

		private BlockingQueue<Serializable> queue = new LinkedBlockingQueue<Serializable>(
				65535);
		private final AtomicInteger capt = new AtomicInteger(0);

		@Override
		protected boolean isOpen() {
			return queue != null;
		}

		@Override
		protected void close(long time, TimeUnit unit) {
			final BlockingQueue<Serializable> queue = this.queue;
			if (queue != null) {
				this.queue = null;
				queue.clear();
			}
		}

		@Override
		protected Serializable getCache() {
			Serializable bs = null;
			try {
				bs = queue.poll(1, TimeUnit.SECONDS);
			} catch (Exception e) {
				// ignore
			} finally {
				if (bs != null) {
					capt.incrementAndGet();
				}
			}
			return bs;
		}

		@Override
		void publish(Serializable obj) {
			if (obj != null) {
				final BlockingQueue<Serializable> queue = this.queue;
				if (queue != null) {
					if (queue.offer(obj)) {
						if (capt.get() > 0 || queue.poll() == null) {
							capt.decrementAndGet();
						}
					} else {
						// should not happen
						if (queue.poll() != null && !queue.offer(obj)) {
							capt.incrementAndGet();
						}
					}
				}
			}
		}

	}

	public static class AsyncAppender extends Appender {
		private final ExecutorService executor;
		private final Appender appender;

		public AsyncAppender(final int threadnum, final int maxbuffer,
				final Appender appender) {
			if (threadnum <= 0 || maxbuffer <= 0) {
				throw new IllegalArgumentException(
						"threadnum and maxbuffer should be positive");
			}
			if (appender == null) {
				throw new NullPointerException("appender could not be null");
			}
			if (AsyncAppender.class.isInstance(appender)) {
				throw new IllegalArgumentException(
						"appender could not be AsyncAppender");
			}
			this.executor = new ThreadPoolExecutor(threadnum, threadnum, 0L,
					TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
							maxbuffer));
			this.appender = appender;
		}

		@Override
		protected synchronized void close(long time, TimeUnit unit) {
			long end = System.currentTimeMillis() + unit.toMillis(time);
			if (!executor.isShutdown()) {
				executor.shutdown();
			}
			try {
				executor.awaitTermination(time, unit);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long remaining = end - System.currentTimeMillis();
			if (remaining > 0) {
				appender.close(remaining, TimeUnit.MILLISECONDS);
			}
		}

		@Override
		protected boolean isOpen() {
			return appender.isOpen() && (!executor.isShutdown());
		}

		@Override
		boolean isLocal() {
			return appender.isLocal();
		}

		@Override
		void publish(final Serializable obj) {
			if (obj != null) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						appender.publish(obj);
					}
				});
			}
		}
	}
}
