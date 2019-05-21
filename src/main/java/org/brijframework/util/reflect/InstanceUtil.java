package org.brijframework.util.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;

/**
 * Instance utility for create object and initialize values of object
 * 
 * @author Ram Kishor
 *
 */
public abstract class InstanceUtil {
	

	/**
	 * Validate arguments of constructor of class
	 * 
	 * @param _constructor
	 * @param _classes
	 * @return
	 */
	private static boolean isValidParam(Constructor<?> _constructor, Object... _params) {
		if (_params == null) {
			if (_constructor.getParameterTypes().length == 0) {
				return true;
			}
		}
		if (_params.length == 0 && _constructor.getParameterTypes().length == 0) {
			return true;
		} 
        if (ParamUtil.isValidParam(_constructor, _params)) {
			return true;
		}
		return false;
	}

	/**
	 * Get constructor with arguments
	 * 
	 * @param _class
	 * @param params
	 * @return constructor
	 */
	public static Constructor<?> constructor(Class<?> _class, Object... params) {
		for (Constructor<?> constructor : _class.getDeclaredConstructors()) {
			if (isValidParam(constructor, params)) {
				return constructor;
			}
		}
		return null;
	}

	/**
	 * Get instance of class with arguments
	 * 
	 * @param _className
	 * @param params
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String _className, Object... params) {
		Assertion.notNull(_className, AssertMessage.class_name_null_message);
		return (T) getInstance(ClassUtil.getClass(_className), params);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Constructor<?> constructor, Object... params) {
		Assertion.notNull(constructor, AssertMessage.class_name_null_message);
		try {
			return (T) constructor.newInstance(params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * Get instance of class with arguments
	 * 
	 * @param _class
	 * @param params
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> _class, Object... params) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		if(_class.isInterface()||_class.getModifiers()==Modifier.ABSTRACT) {
			return null;
		}
		try {
			if (params!=null && params.length > 0){
				Constructor<?> constructor = constructor(_class,params);
				Assertion.notNull(constructor, "constructor"+AssertMessage.Not_found_message);
				return (T) constructor.newInstance(params);
			}
			else{
				return _class.newInstance();
			}
		} catch (InstantiationException | IllegalArgumentException | InvocationTargetException
				| IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get instance of class with arguments and init values map
	 * 
	 * @param _class
	 * @param _map
	 * @param _params
	 * 
	 * @return object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getInstance(Class<?> _class, Map _map) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Assertion.notNull(_map, AssertMessage.arg_object_null_message);
		Object object = getInstance(_class);
		PropertyAccessorUtil.setProperties(object, _map);
		return (T) object;
	}
	
	/**
	 * Get instance of class with arguments and init values map
	 * 
	 * @param _class
	 * @param _map
	 * @param _params
	 * 
	 * @return object
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T getInstance(Class<?> _class, Map<String,Object> _map,Object...params) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Assertion.notNull(_map, AssertMessage.arg_object_null_message);
		Object object = getInstance(_class,params);
		PropertyAccessorUtil.setProperties(object, _map);
		return (T) object;
	}

	/**
	 * Get private or singleton instance of class with arguments
	 * 
	 * @param _class
	 * @param params
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSingletonInstance(Class<?> _class, Object... params) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		try {
			Constructor<?> constructor = constructor(_class, params);
			Assertion.notNull(constructor, "constructor"+AssertMessage.Not_found_message+" for "+_class);
			constructor.setAccessible(true);;
			return (T) constructor.newInstance(params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSingletonInstance(String _class, Object... params) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		return (T) getSingletonInstance(ClassUtil.getClass(_class), params);
	}

	/**
	 * Get private or singleton instance of class with arguments and init values
	 * map
	 * 
	 * @param _class
	 * @param _map
	 * @param _params
	 * 
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSingletonInstance(Class<?> _class, Map<String, Object> _map, Object... _params) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_map, AssertMessage.arg_object_null_message);
		Object object = getSingletonInstance(_class, _params);
		PropertyAccessorUtil.setProperties(object, _map);
		return (T) object;
	}
	
	public static Map<String, Object> getAllWithOutRelField(Object object) {
		return null;
	}
	

}
