package com.share.commons.util;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @date 2015-06-03 16:07
 */
public class BlockingObject<V> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6886374646883992028L;
	private volatile V vr = null;
	// default time to wait
	private final long nanos;
	// should count down once
	protected volatile CountDownLatch latch = new CountDownLatch(1);
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BlockingObject.class);

	/**
	 * 
	 * @param time
	 *            time to wait , endless for negative
	 * @param unit
	 */
	public BlockingObject(long time, TimeUnit unit) {
		this(unit.toNanos(time));
	}

	/**
	 * 
	 * @param nanos
	 *            time to wait in nanosecond, endless for negative
	 */
	public BlockingObject(long nanos) {
		this.nanos = nanos;
	}

	/**
	 * endless wait for {@link #getObject()}
	 */
	public BlockingObject() {
		this(-1);
	}

	/**
	 * waiting the default time for the result, or endless.
	 * 
	 * @return
	 */
	public final V getObject() {
		// has been set
		final CountDownLatch latch = this.latch;
		if (latch == null) {
			return vr;
		}
		try {
			if (nanos >= 0) {
				// ignore the result
				latch.await(nanos, TimeUnit.NANOSECONDS);
			} else {
				latch.await();
			}
		} catch (Exception e) {
			LOGGER.error("error in getObject waiting "
					+ (nanos >= 0 ? (nanos + "ns") : "endless"), e);
		}
		// return, success or not
		return vr;
	}

	/**
	 * 
	 * @param time
	 *            time to wait instead of default
	 * @param unit
	 * @return
	 */
	public final V getObject(long time, TimeUnit unit) {
		// has been set
		final CountDownLatch latch = this.latch;
		if (latch == null) {
			return vr;
		}
		try {
			latch.await(time, unit);
		} catch (Exception e) {
			LOGGER.error(
					"error in getObject waiting " + time + " with "
							+ unit.toString(), e);
		}
		// return, success or not
		return vr;
	}

	/**
	 * accept the first Value, ignore others
	 * 
	 * @param v
	 * @return true if it's the first value to set
	 */
	public final boolean setIfAbsent(final V v) {
		final CountDownLatch latch = this.latch;
		if (latch == null) {
			return false;
		}
		if (latch.getCount() > 0) {
			vr = v;
			do {
				latch.countDown();
			} while (latch.getCount() > 0);
			// help GC
			synchronized (latch) {
				if (latch == this.latch) {
					this.latch = null;
					return true;
				}
			}
		}
		// been set before or in other thread
		return false;
	}

	/**
	 * add or update value
	 * 
	 * @param v
	 * @return true if it's the first value to set
	 */
	public final boolean set(final V v) {
		final boolean absent = setIfAbsent(v);
		if (!absent) {
			vr = v;
		}
		return absent;
	}

	/**
	 * accept the first Value, ignore others if force is false
	 * 
	 * @param v
	 * @param force
	 * @return true if it's the first value to set
	 */
	public final boolean set(final V v, final boolean force) {
		return force ? set(v) : setIfAbsent(v);
	}

}
