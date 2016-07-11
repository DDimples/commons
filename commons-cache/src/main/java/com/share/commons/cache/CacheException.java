package com.share.commons.cache;

public class CacheException extends RuntimeException {
	private static final long serialVersionUID = 25907520472281072L;

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Throwable e) {
		super(s, e);
	}

	public CacheException(Throwable e) {
		super(e);
	}
	
}
