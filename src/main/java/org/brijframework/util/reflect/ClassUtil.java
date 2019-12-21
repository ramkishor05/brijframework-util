package org.brijframework.util.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.validator.ValidationUtil;

public abstract class ClassUtil {
	
	static public ParameterizedType getParameterizedType(Class<?> target) {
	    Type[] types = getGenericType(target);
	    if (types.length > 0 && types[0] instanceof ParameterizedType) {
	      return (ParameterizedType) types[0];
	    }
	    return null;
	  }

	  static public Type[] getParameterizedTypes(Class<?> target) {
	    Type[] types = getGenericType(target);
	    if (types.length > 0 && types[0] instanceof ParameterizedType) {
	      return ((ParameterizedType) types[0]).getActualTypeArguments();
	    }
	    return null;
	  }

	  static public Type[] getGenericType(Class<?> target) {
	    if (target == null)
	      return new Type[0];
	    Type[] types = target.getGenericInterfaces();
	    if (types.length > 0) {
	      return types;
	    }
	    Type type = target.getGenericSuperclass();
	    if (type != null) {
	      if (type instanceof ParameterizedType) {
	        return new Type[] { type };
	      }
	    }
	    return new Type[0];
	  }

	public ClassUtil() {
	}

	public static boolean isClass(String _className) {
		if(_className==null) {
			return false;
		}
		if(char.class.getName().equals(_className)) {
			return true;
		}
		if(byte.class.getName().equals(_className)) {
			return true;
		}
		if(short.class.getName().equals(_className)) {
			return true;
		}
		if(int.class.getName().equals(_className)) {
			return true;
		}
		if(long.class.getName().equals(_className)) {
			return true;
		}
		if(double.class.getName().equals(_className)) {
			return true;
		}
		if(float.class.getName().equals(_className)) {
			return true;
		}
	    try  {
	        Class.forName(_className);
	        return true;
	    }  catch (ClassNotFoundException e) {
	        return false;
	    }
	}
	
	/**
	 * get class from class name
	 * 
	 * @param _className
	 * @return class
	 */
	public static Class<?> getClass(String _className) {
		Assertion.notNull(_className, AssertMessage.class_name_null_message);
		if(char.class.getName().equals(_className)) {
			return char.class;
		}
		if(byte.class.getName().equals(_className)) {
			return byte.class;
		}
		if(short.class.getName().equals(_className)) {
			return short.class;
		}
		if(int.class.getName().equals(_className)) {
			return int.class;
		}
		if(long.class.getName().equals(_className)) {
			return long.class;
		}
		if(double.class.getName().equals(_className)) {
			return double.class;
		}
		if(float.class.getName().equals(_className)) {
			return float.class;
		}
		
		try {
			return Class.forName(_className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * get supper class of current class
	 * 
	 * @param _className
	 * @return class
	 */
	public Class<?> getSuperClass(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Class<?> superClass = null;
		while (!_class.getSimpleName().contentEquals(Object.class.getSimpleName())) {
			superClass = _class;
		}
		return superClass;
	}

	/**
	 * get supper interface of current class
	 * 
	 * @param _className
	 * @return class
	 */
	public Class<?> getSuperInterface(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Class<?> superClass = null;
		while (!_class.getSimpleName().contentEquals(Object.class.getSimpleName())) {
			if (_class.isInterface()) {
				superClass = _class;
			} else {
				break;
			}
		}
		return superClass;
	}

	/**
	 * get all interface of current class
	 * 
	 * @param _className
	 * @return list
	 */
	public static List<Class<?>> getAllSuperInterface(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		ArrayList<Class<?>> interfaces = new ArrayList<>();
		for (Class<?> c : ClassUtil.getAllSuperClass(_class)) {
			for (Class<?> i : c.getInterfaces()) {
				interfaces.add(i);
			}
		}
		return interfaces;
	}

	/**
	 * get all supper class of current class
	 * 
	 * @param _class
	 * @return list
	 */

	public static List<Class<?>> getAllSuperClass(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		LinkedList<Class<?>> _classes = new LinkedList<>();
		Class<?> _supper = _class.getSuperclass();
		while (!_supper.getSimpleName().contentEquals(Object.class.getSimpleName())) {
			_classes.add(_supper);
			_supper = _supper.getSuperclass();
		}
		return _classes;
	}

	/**
	 * check interface is exist or not in current class
	 * 
	 * @param _clazz
	 * @return list
	 */
	public static boolean isContainInterface(Class<?> _class, Class<?> _interface) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message + " left");
		Assertion.notNull(_class, AssertMessage.class_object_null_message + " rigth");
		return ClassUtil.getAllSuperInterface(_class).contains(_interface);
	}

	public static Class<?> collectionParamType(Field _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		if (isCollection(_field.getType())) {
			ParameterizedType listType = (ParameterizedType) _field.getGenericType();
			return (Class<?>)listType.getActualTypeArguments()[0];
		}
		return null;
	}
	
	public static Class<?> collectionParamType(Method _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		if (isCollection(_field.getReturnType())) {
			ParameterizedType listType = (ParameterizedType) _field.getGenericReturnType();
			return (Class<?>)listType.getActualTypeArguments()[0];
		}
		return null;
	}
	
	public static Class<?> collectionParamType(AccessibleObject _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		if(_field instanceof Method) {
			return collectionParamType((Method)_field);
		}else {
			return collectionParamType((Field)_field);
		}
	}

	public static Class<?> getCollectionType(Collection<?> _collection) {
		Assertion.notNull(_collection, AssertMessage.arg_null_message);
		if (_collection.size() > 0) {
			_collection.toArray()[0].getClass();
		}
		return null;
	}

	public static boolean isArrayField(Field _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		if (Array.class.isAssignableFrom(_field.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isCollection(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		if (List.class.isAssignableFrom(_class) || Set.class.isAssignableFrom(_class)
				|| Queue.class.isAssignableFrom(_class) || Map.class.isAssignableFrom(_class)) {
			return true;
		}
		return false;
	}

	public static List<Field> getAllRelField(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> fields = new ArrayList<>();
		for (Field field : FieldUtil.getAllField(_class)) {
			if (!ValidationUtil.isPrimative(field.getType())) {
				fields.add(field);
			}
		}
		return fields;
	}

	public static void main(String[] args) {
		/*
		 * Field fieldID=ClassUtil.getField(TestDemo.class,"ID", AccessLevel.PUBLIC);
		 * System.out.println("FIND "+fieldID.getName());
		 */
		/*
		 * for (Field field : ClassUtil.getAllField(TestDemo.class,
		 * AccessLevel.DEFAULT)) { System.out.println("------------------------");
		 * System.out.println(field.getName() + "=" + field.getModifiers()); }
		 */

	}

}
