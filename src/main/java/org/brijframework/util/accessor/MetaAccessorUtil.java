package org.brijframework.util.accessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ParamUtil;
import org.brijframework.util.support.ReflectionAccess;
import org.brijframework.util.validator.ValidationUtil;

public class MetaAccessorUtil {

	public static AccessibleObject getPropertyMeta(Class<?> meta, String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return findGetterMeta(meta, field, ReflectionAccess.PUBLIC);
	}

	public static AccessibleObject findGetterMeta(Class<?> meta, String field, ReflectionAccess level) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		try {
			Method getter = getterPropertyDescriptor(meta, field);
			return getter != null ? getter :findFieldMeta(meta, field, level);
		} catch (IllegalArgumentException | IntrospectionException e) {
			return null;
		}
	}

	public static AccessibleObject setPropertyMeta(Class<?> meta, String field, Object value) {
		return setPropertyMeta(meta, field, ReflectionAccess.PUBLIC, value);
	}

	public  static AccessibleObject findSetterMeta(Class<?> meta, String field, ReflectionAccess level) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		try {
			Method setter = setterPropertyDescriptor(meta, field);
			return setter != null ?  setter :  findFieldMeta(meta, field, level);
		} catch (IllegalArgumentException | IntrospectionException e) {
			return null;
		}
	}
	
	public  static AccessibleObject setPropertyMeta(Class<?> meta, String field, ReflectionAccess level, Object value) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		try {
			Method setter = setterPropertyDescriptor(meta, field, value);
			return setter != null ?  setter :  findFieldMeta(meta, field, level);
		} catch (IllegalArgumentException | IntrospectionException e) {
			return null;
		}
	}

	public static List<AccessibleObject> getPropertiesMeta(Class<?> meta) {
		return getPropertiesMeta(meta, ReflectionAccess.PUBLIC);
	}

	/**
	 * this get all properties of class
	 * 
	 * @return
	 */
	public static List<AccessibleObject> getPropertiesMeta(Class<?> meta, ReflectionAccess level) {
		List<AccessibleObject> properties = new ArrayList<>();
		BeanInfo beaninfo = null;
		try {
			beaninfo = Introspector.getBeanInfo(meta);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		for (PropertyDescriptor pd : beaninfo.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null && !new Object().getClass().equals(reader.getDeclaringClass())) {
				properties.add(reader);
			}
			Method writer = pd.getWriteMethod();
			if (writer != null && !new Object().getClass().equals(writer.getDeclaringClass())) {
				properties.add( writer);
			}
		}
		for (Field field : FieldUtil.getAllField(meta, level)) {
			properties.add(field);
		}
		return  properties;
	}

	public static Method getLogicMeta(Class<?> meta, String _method, Object... params) {
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		return getLogicMeta(meta, _method, ReflectionAccess.PUBLIC, params);
	}

	public static Method getLogicMeta(Class<?> meta, String _method, ReflectionAccess level, Object... paramObjects) {
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		try {
			Method method = (Method) getMethodDescriptor(meta, _method, paramObjects);
			return method != null ? method : (Method) getMethodMeta(meta, level, _method, paramObjects);
		} catch (IntrospectionException e) {
			return null;
		}
	}
	
	public static Method getterPropertyMeta(Class<?> _meta, String _name){
		try {
			return getterPropertyDescriptor(_meta, _name);
		} catch (IntrospectionException e) {
			return null;
		}
	}

	public static Method getterPropertyDescriptor(Class<?> _meta, String _name) throws IntrospectionException {
		BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		PropertyDescriptor pds[] = beaninfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			Method getterMethod = pd.getReadMethod();
			if (getterMethod != null && EventValidateUtil.validReader(getterMethod.getName(), _name)) {
				return getterMethod;
			}
		}
		return getMethodMeta(_meta, ReflectionAccess.PUBLIC, _name, 0);
	}

	public static Method setterPropertyMeta(Class<?> _meta, String _name, Object value){
		try {
			return setterPropertyDescriptor(_meta, _name, value);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public static Method setterPropertyMeta(Class<?> _meta, String _name) {
		try {
			return setterPropertyDescriptor(_meta, _name);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public static Method setterPropertyDescriptor(Class<?> _meta, String _name) throws IntrospectionException {
		BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		PropertyDescriptor pds[] = beaninfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			Method setterMethod = pd.getWriteMethod();
			if (setterMethod != null && EventValidateUtil.validWriter(setterMethod.getName(), _name)) {
				return setterMethod;
			}
		}
		return getMethodMeta(_meta, ReflectionAccess.PUBLIC, _name, 1);
	}
	
    public static Map<String, Method> setterPropertyDescriptorList(Class<?> _meta) {
    	Map<String, Method> propertyDescriptorList=new HashMap<String, Method>();
    	try {
    	BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		PropertyDescriptor pds[] = beaninfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			Method setterMethod = pd.getWriteMethod();
			if (setterMethod != null) {
				propertyDescriptorList.put(pd.getName(), setterMethod);
			}
		}
    	}catch (Exception e) {
		}
		return propertyDescriptorList;
    }
    
    public static Map<String, Method> getterPropertyDescriptorList(Class<?> _meta) {
    	Map<String, Method> propertyDescriptorList=new HashMap<String, Method>();
    	try {
    	BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		PropertyDescriptor pds[] = beaninfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			Method getterMethod = pd.getReadMethod();
			if (getterMethod != null) {
				propertyDescriptorList.put(pd.getName(), getterMethod);
			}
		}
    	}catch (Exception e) {
		}
		return propertyDescriptorList;
    }
    
    public static Method setterPropertyDescriptor(Class<?> _meta, String _name, Object value) throws IntrospectionException {
		BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		PropertyDescriptor pds[] = beaninfo.getPropertyDescriptors();
		List<Method> methods = new ArrayList<>();
		for (PropertyDescriptor pd : pds) {
			Method setterMethod = pd.getWriteMethod();
			if (setterMethod != null && EventValidateUtil.validWriter(setterMethod.getName(), _name)) {
				methods.add(setterMethod);
			}
		}
		for (Method method : methods) {
			if (value == null) {
				return method;
			}
			if (method.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
				return method;
			}
			if (ValidationUtil.isNumber(value.toString())
					&& method.getParameterTypes()[0].isAssignableFrom(Number.class)) {
				return method;
			}
		}
		return methods.size() > 0 ? methods.get(0) : getMethodMeta(_meta, ReflectionAccess.PUBLIC, _name, 1);
	}

	public static Field findFieldMeta(Class<?> _meta, String _name, ReflectionAccess access) {
		try {
			return FieldUtil.getField(_meta, _name, access);
		} catch (SecurityException e) {
			return null;
		}
	}

	public static Method getMethodDescriptor(Class<?> _meta, String method, Object... paramClasses)
			throws IntrospectionException {
		BeanInfo beaninfo = Introspector.getBeanInfo(_meta);
		MethodDescriptor[] methods = beaninfo.getMethodDescriptors();
		for (MethodDescriptor descriptor : methods) {
			Method collMethod = descriptor.getMethod();
			if (collMethod != null && collMethod.getName().equals(method)
					&& ParamUtil.isEqualParam(collMethod.getParameterTypes(), paramClasses)) {
				return collMethod;
			}
		}
		return null;
	}

	public static Method getMethodMeta(Class<?> _meta, ReflectionAccess level, String _name, Object... params) {
		try {
			return MethodUtil.getMethod(_meta, _name, level, params);
		} catch (SecurityException e) {
			return null;
		}
	}

	public static Method getMethodMeta(Class<?> _meta, ReflectionAccess level, String _name, int params) {
		try {
			return MethodUtil.getMethod(_meta, _name, params, level);
		} catch (SecurityException e) {
			return null;
		}
	}

	public static Set<String> getNamePropertiesMeta(Class<?> meta) {
		Assertion.notNull(meta, "meta clas not be null");
		return getNamePropertiesMeta(meta, ReflectionAccess.PUBLIC);
	}

	public static Set<String> getNamePropertiesMeta(Class<?> meta, ReflectionAccess level) {
		Set<String> properties = new LinkedHashSet<>();
		for (Field field : FieldUtil.getAllField(meta,ReflectionAccess.PRIVATE)) {
			if (level.isAccess(field.getModifiers())) {
				properties.add(field.getName());
			}
		}
		return properties;
	}

	public static Set<String> getNameLogicsMeta(Class<?> meta) {
		return getNamePropertiesMeta(meta, ReflectionAccess.PUBLIC);
	}

	public Set<String> getNameLogicsMeta(Class<?> meta, ReflectionAccess level) {
		Set<String> properties = new LinkedHashSet<>();
		for (Method field : MethodUtil.getAllMethod(meta, level)) {
			properties.add(field.getName());
		}
		return properties;
	}

	public static Set<Method> getLogicsMeta(Class<?> meta) {
		return getLogicsMeta(meta, ReflectionAccess.PUBLIC);
	}

	public static Set<Method> getLogicsMeta(Class<?> meta, ReflectionAccess level) {
		Set<Method> properties = new LinkedHashSet<>();
		for (Method field : MethodUtil.getAllMethod(meta, level)) {
			properties.add(field);
		}
		return properties;
	}

	

}
