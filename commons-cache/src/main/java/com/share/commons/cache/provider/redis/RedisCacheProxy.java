package com.share.commons.cache.provider.redis;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class RedisCacheProxy implements MethodInterceptor {

	RedisCache cache;

	public RedisCacheProxy(RedisCache cache) {
		this.cache = cache;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		try {
			 Object oj=null ;
			 oj = proxy.invoke(cache, args);
			return oj;
		} catch (Throwable e) {
			//e.printStackTrace();

		} finally {

		}
		return null;
	}

	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.cache.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
}
