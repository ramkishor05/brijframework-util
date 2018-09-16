package org.brijframework.util.reflect;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.logger.LogTracker;
import org.brijframework.logger.constant.LogAccess;
import org.brijframework.support.enums.Access;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.validator.ValidationUtil;

public abstract class LogicUnit {

	private static final String FIELD_NOT_FOUND = "FIELD_NOT_FOUND";
	private static final String OBJECT_NULL_FOUND = "OBJECT_NULL_FOUND";
	private static final String KEY_NULL_FOUND = "KEY_NULL_FOUND";

	
	private static final String LogicUnit_setField = "LogicUnit_setField";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T collectionMethod(Collection _collection, String _property, Object... _objects) {
		Assertion.notNull(_collection, AssertMessage.arg_null_message+" collection");
		Assertion.notNull(_property, AssertMessage.arg_null_message+" property");
		Collection<Object> returncollection = InstanceUtil.getInstance(_collection.getClass());
		for (Object object : _collection) {
			Class<?>[] classes = ParamUtil.paramClasses(_objects);
			Method method = MethodUtil.getMethod(object.getClass(), _property, classes);
			if (method != null) {
				try {
					returncollection.add(method.invoke(object, _objects));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LogTracker.trace("LogicUtil_collectionMethod", LogAccess.DEVELOPER, e.getMessage(), e);
				}
			}
		}
		return (T) returncollection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T collectionField(Collection _collection, String _property) {
		Assertion.notNull(_collection, AssertMessage.arg_null_message+" collection");
		Assertion.notNull(_property, AssertMessage.arg_null_message+" property");
		Collection<Object> returncollection = InstanceUtil.getInstance(_collection.getClass());
		for (Object object : _collection) {
			Field field = FieldUtil.getField(object.getClass(), _property);
			if (field != null) {
				try {
					returncollection.add(field.get(object));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return (T) returncollection;
	}

	public static <T> T callMethod(Object object, String _method, Object... objects) {
		Method method = MethodUtil.getMethod(object.getClass(), _method);
		return callMethod(object, method, objects);
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T callMethod(Object object, Method method,Object... objects) {
		Assertion.notNull(method,"Method should not be null");
		try {
			return (T) method.invoke(object, objects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T callMethod(Class<?> _class, String _method, Object... objects) {
		Method method = MethodUtil.getMethod(_class, _method);
		return callMethod(method, objects);
	}

	@SuppressWarnings("unchecked")
	public static <T> T callMethod(Method method, Object... objects) {
		Assertion.notNull(method,"Method should not be null");
		try {
			return (T) method.invoke(null, objects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getField(Object object, String property) {
		Field field = FieldUtil.getField(object.getClass(), property);
		return getField(object, field);
	}

	@SuppressWarnings("unchecked")
	public static <T> T setAllField(Object object, Map<String, Object> map) {
		for (String key : map.keySet()) {
			setField(object, key, map.get(key));
		}
		return (T) object;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getField(Object object, Field field) {
		Assertion.notNull(field,"Field should not be null");
		try {
			field.setAccessible(true);
			return (T) field.get(object);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getAllField(Object _object, String... _keys) {
		Map<String, Object> returnValues = new HashMap<>();
		List<String> keyList = _keys.length > 0 ? Arrays.asList(_keys) : FieldUtil.getFieldList(_object.getClass());
		for (String key : keyList) {
			returnValues.put(key, getField(_object, key));
		}
		return returnValues;
	}

	public static <T> T setField(Object _object, String property, Object _param) {
		if (_object == null) {
			LogTracker.info(LogicUnit_setField, LogAccess.DEVELOPER, OBJECT_NULL_FOUND );
			return null;
		}
		if (!ValidationUtil.isValidObject(property)) {
			LogTracker.info(LogicUnit_setField, LogAccess.DEVELOPER, KEY_NULL_FOUND );
			return null;
		}
		Field _field = FieldUtil.getField(_object.getClass(), property);
		if (_field == null) {
			LogTracker.info(LogicUnit_setField, LogAccess.DEVELOPER, FIELD_NOT_FOUND + "-> " + property);
			return null;
		}
		return setField(_object, _field, _param);
	}

	public static <T> T setField(Object object, String property, Object param, Access accessLevel) {
		Field field = FieldUtil.getField(object.getClass(), property, accessLevel);
		return setField(object, field, param);
	}

	@SuppressWarnings("unchecked")
	public static <T> T setField(Object _object, Field _field, Object _param) {
		if ( !ValidationUtil.isValidObject(_object,_field)) {
			LogTracker.info(LogicUnit_setField, LogAccess.DEVELOPER, FIELD_NOT_FOUND + "-> " + _field);
			return null;
		}
		try {
			Class<?> paramType = null;
			Class<?> returnType = _field.getType();
			if (ValidationUtil.isEqualClass(Collection.class, _field.getType())) {
				ParameterizedType listType = (ParameterizedType) _field.getGenericType();
				paramType = (Class<?>) listType.getActualTypeArguments()[0];
				returnType = getField(_object, _field).getClass();
			}
			Object atual = CastingUtil.castObject(_param, returnType, paramType);
			_field.set(_object, atual);
			return (T) atual;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
