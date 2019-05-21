package org.brijframework.util.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

	public ClassUtil() {
	}

	public static boolean isClass(String className) {
	    try  {
	        Class.forName(className);
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

	public static Object collectionParamType(Field _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		if (isCollection(_field.getType())) {
			ParameterizedType listType = (ParameterizedType) _field.getGenericType();
			if (listType.getActualTypeArguments().length < 1) {
				return List.class.toGenericString();
			}
			return listType.getActualTypeArguments()[0];
		}
		return null;
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
