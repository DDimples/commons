/**   
 * @Title: CloneBean.java
 * @date 2014年12月11日 下午10:36:25 
 * @version V1.0   
 */
package com.share.commons.util.beans;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.share.commons.util.Wrapper;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

public class CloneableBean implements ICloneableBean {
	private static ConcurrentMap<Class<?>, Wrapper<BeanCopier>> beanCopierMap = new ConcurrentHashMap<Class<?>, Wrapper<BeanCopier>>();

	@Override
	public Object clone() {
		try {
			Class<?> clzz = this.getClass();
			BeanCopier beanCopier = _createCopier(clzz);
			if (beanCopier != null) {
				Object clone = clzz.newInstance();
				beanCopier.copy(this, clone, new Converter() {
					@SuppressWarnings("rawtypes")
					public Object convert(Object arg0, Class arg1, Object arg2) {
						return _clone(arg0, arg1);
					}
				});
				return clone;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	private static Object _clone(Object object, Class<?> clzz) {
		if (object == null) {
			return object;
		}
		if (clzz.isPrimitive()) {
			return object;
		}
		if (ICloneableBean.class.isAssignableFrom(clzz)) {
			return ((ICloneableBean) object).clone();
		} else {
			if (clzz.isArray()) {
				int length = Array.getLength(object);
				Class<?> type = clzz.getComponentType();
				Object clone = Array.newInstance(type, length);
				if (type.isPrimitive()) {
					System.arraycopy(object, 0, clone, 0, length);
				} else {
					for (int i = 0; i < length; i++) {
						Array.set(clone, i, _clone(Array.get(object, i), type));
					}
				}
				return clone;
			}
			return Cloneable.class.isAssignableFrom(clzz) ? clone(object)
					: object;
		}
	}

	private static Object clone(Object object) {
		Class<?> clzz = object.getClass();
		Wrapper<Method> wrapper = cloneMethodMap.get(clzz);
		if (wrapper == null) {
			Method method = null;
			try {
				method = object.getClass().getMethod("clone");
			} catch (Exception e) {
				// no public method clone()
			}
			wrapper = new Wrapper<Method>(method);
			Wrapper<Method> wrapper2 = cloneMethodMap
					.putIfAbsent(clzz, wrapper);
			if (wrapper2 != null) {
				wrapper = wrapper2;
			}
		}
		// (wrapper != null) shall be always true
		if (wrapper != null) {
			Method m = wrapper.getT();
			if (m != null) {
				try {
					return m.invoke(object);
				} catch (Exception e) {
					// cannot clone, remove method
					wrapper.setT(null);
				}
			}
		}
		// cannot clone success
		return object;
	}

	private static final ConcurrentMap<Class<?>, Wrapper<Method>> cloneMethodMap = new ConcurrentHashMap<Class<?>, Wrapper<Method>>();

	private static BeanCopier _createCopier(Class<?> clz) {
		Wrapper<BeanCopier> wrapper = beanCopierMap.get(clz);
		if (wrapper == null) {
			BeanCopier copier = null;
			try {
				copier = BeanCopier.create(clz, clz, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			wrapper = new Wrapper<BeanCopier>(copier);
			Wrapper<BeanCopier> existWrapper = beanCopierMap.putIfAbsent(clz,
					wrapper);
			if (existWrapper != null) {
				wrapper = existWrapper;
			}
		}
		return wrapper.getT();
	}

}