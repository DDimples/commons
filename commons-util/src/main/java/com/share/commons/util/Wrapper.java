package com.share.commons.util;

/**
 * simple wrapper
 *
 *
 */
public  class Wrapper<T> {
	private T t;

	public Wrapper(T t) {
		this.t = t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public T getT() {
		return t;
	}
}
