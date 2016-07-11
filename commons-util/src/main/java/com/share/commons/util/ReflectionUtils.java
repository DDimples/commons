package com.share.commons.util;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类.
 * 
 * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数.
 * 
 */
public class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
	private static final String IS_INITIALS = "is";
	private static final String GET_INITIALS = "get";
	@SuppressWarnings("unused")
	private static final String SET_INITIALS = "set";

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetterMethod(Object obj, String propertyName) {
		String getterMethodName = "get" + StringUtil.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtil.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	public static Object getProperty(final Object obj, final String fieldName) {
		try {
			return BeanUtils.getProperty(obj, fieldName);
		} catch (IllegalAccessException e) {
			logger.error("取对象属性异常", e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("取对象属性异常", e.getMessage());
		} catch (NoSuchMethodException e) {
			logger.error("取对象属性异常", e.getMessage());
		}
		return "";
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		//Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	/**
	 * 判断指定属性是否存在
	 */
	public static boolean getExistField(final Object obj, final String fieldName){
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field : fields){
			if( fieldName != null && fieldName.equals(field.getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况.
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (NoSuchMethodException e) {//NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	
	public static Object getMethodReturnTypeInstance(Class<?> clazz, String methodName){
		Object rst = null;
		if(clazz != null && !StringUtil.isBlank(methodName)){
			try {
				Method[] methods = clazz.getMethods();
				if(methods != null){
					for(Method method : methods){
						if(method.getName().equals(methodName)){
							rst = method.getReturnType().newInstance();
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rst;
	}
	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static Map<String, Method> getGetters(Class<?> clazz) {
		if (!Modifier.isPublic(clazz.getModifiers())) {
			throw new IllegalArgumentException("class not public " + clazz);
		}
		Map<String, Method> methods = new TreeMap<String, Method>();
		for (Method m : clazz.getMethods()) {
			if (!isGetter(m)) {
				continue;
			}
			if (m.getDeclaringClass().equals(Object.class)) {
				// hack: removing getClass()
				continue;
			}
			String propertyName = "";
			if (m.getName().startsWith(GET_INITIALS)) {
				propertyName = m.getName().substring(GET_INITIALS.length());

			} else if (m.getName().startsWith(IS_INITIALS)) {
				propertyName = m.getName().substring(IS_INITIALS.length());
			}
			// ok, this is a hack, cause we can have a problem
			// with classes with a get() method
			// (the propertyname would be an empty string)
			if (propertyName.length() != 0) {
				if (propertyName.length() == 1 || Character.isLowerCase(propertyName.charAt(1))) {
					propertyName = Introspector.decapitalize(propertyName);
				}
				methods.put(propertyName, m);
			}
		}
		return methods;
	}

	public static boolean isGetter(Method m) {
		if (m.getParameterTypes().length != 0 || !Modifier.isPublic(m.getModifiers())
				|| m.getReturnType().equals(Void.TYPE)) {
			return false;
		}
		if (Modifier.isStatic(m.getModifiers()) || !Modifier.isPublic(m.getModifiers())
				|| Modifier.isAbstract(m.getModifiers())) {
			return false;
		}
		if (m.getName().startsWith(GET_INITIALS) && m.getName().length() > GET_INITIALS.length()) {
			return true;
		}
		if (m.getName().startsWith(IS_INITIALS) && m.getName().length() > IS_INITIALS.length()
				&& (m.getReturnType().equals(boolean.class) || m.getReturnType().equals(Boolean.class))) {
			return true;
		}
		return false;
	}
}
