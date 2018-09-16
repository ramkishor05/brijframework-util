package org.brijframework.util.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.brijframework.support.constants.Constants;
import org.brijframework.support.enums.Access;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.FieldUtil;

public class BuilderUtil {
	
	public static Class<?> getCurrentClass(Class<?> cls, String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Class<?> keyClass=cls;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			keyClass=getProperty(keyClass, key);
		}
		return keyClass;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentInstance(Object object, String _keyPath) {
		if(object instanceof Map) {
			return getCurrentFromMapped((Map<String, Object> )object, _keyPath);
		}else {
			return getCurrentFromInstance(object, _keyPath);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCurrentFromInstance(Object object,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			current=getProperty(current, key);
		}
		return (T) current;
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getCurrentFromMapped(Map<String, Object> object,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			current=getProperty(current, key);
		}
		return (T) current;
	}
	
	@SuppressWarnings("unchecked")
	public static  Class<?> getProperty(Class<?> _class, String _field) {
		Field field=FieldUtil.getField(_class, _field, Access.PRIVATE);
		if(field==null) {
			return null;
		}
		return field.getType();
	}
	

	@SuppressWarnings("unchecked")
	public static  <T> T getProperty(Object current, String key) {
		if(current instanceof Object[]) {
			return getPropertyArray((Object[]) current,key);
		}else if(current instanceof Collection) {
			return getPropertyCollection((Collection<?>) current,key);
		}else if(current instanceof Map) {
			return getPropertyMap((Map<String, Object>) current,key);
		}else {
			return getPropertyObject(current, key);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object current, String key,Object value) {
		if(current instanceof Object[]) {
			return setPropertyArray((Object[]) current,key,value);
		}else if(current instanceof Collection) {
			return setPropertyCollection((Collection<?>) current,key,value);
		}else if(current instanceof Map) {
			return setPropertyMap((Map<String, Object>) current,key,value);
		}else {
			return setPropertyObject(current, key,value);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T> T setPropertyArray(Object[] current, String key, Object value) {
		Collection<Object> objects=new ArrayList<>();
		for(Object object:current){
			objects.add(setProperty(object, key,value));
		};
		return (T) objects;
	}

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyCollection(Collection<?> current, String key, Object value) {
		Collection<Object> objects=new ArrayList<>();
		current.forEach(object->{
			objects.add(setProperty(object, key,value));
		});
		return (T) objects;
	}

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyMap(Map<String, Object> current, String key, Object value) {
		Assertion.isTrue(!current.containsKey(key), AssertMessage.unbounded_key_message + " " + key);
		return (T) current.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyMap(Map<String, Object> current,String key ) {
		Assertion.isTrue(!current.containsKey(key), AssertMessage.unbounded_key_message + " " + key);
		return (T) current.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyCollection(Collection<?> collection,String key ) {
		Collection<Object> objects=new ArrayList<>();
		collection.forEach(object->{
			objects.add(getProperty(object, key));
		});
		return (T) objects;
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyArray(Object[] collection,String key ) {
		Collection<Object> objects=new ArrayList<>();
		for(Object object:collection){
			objects.add(getProperty(object, key));
		};
		return (T) objects.toArray();
	}
	
	public static  <T> T getPropertyObject(Object object,String key ) {
		return PropertyAccessorUtil.getProperty(object, key);
	}

	public static <T> T setPropertyObject(Object current, String keyPoint, Object _value) {
		return PropertyAccessorUtil.setProperty(current, keyPoint,_value);
	}
	
	

}
