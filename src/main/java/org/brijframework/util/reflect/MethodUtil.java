package org.brijframework.util.reflect;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Access;
import org.brijframework.util.text.StringUtil;

public abstract class MethodUtil {

	private static boolean isValidParam(Method _method, Class<?>... _classes) {
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		if (_classes == null) {
			if (_method.getParameterTypes().length == 0) {
				return true;
			}
		}
		if (_classes.length == 0 && _method.getParameterTypes().length == 0) {
			return true;
		} else if (ParamUtil.isValidParam(_method, _classes)) {
			return true;
		}
		return false;
	}

	public static List<Method> fillMethods(Method[] methods, Access _accessLevel, Class<?>... _classes) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		List<Method> returnList = new LinkedList<>();
		for (Method method : methods) {
			if (_accessLevel.isAccess(method.getModifiers())) {
				if (isValidParam(method, _classes)) {
					returnList.add(method);
				}
			}
		}
		return returnList;
	}

	private static Collection<Method> fillMethods(Method[] methods, Access _accessLevel) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		List<Method> returnList = new LinkedList<>();
		for (Method method : methods) {
			if (_accessLevel.isAccess(method.getModifiers())) {
				returnList.add(method);
			}
		}
		return returnList;
	}

	private static Collection<Method> fillMethods(Method[] methods, Class<?>... _classes) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		List<Method> returnList = new LinkedList<>();
		for (Method method : methods) {
			if (isValidParam(method, _classes)) {
				returnList.add(method);
			}
		}
		return returnList;
	}

	private static Collection<Method> findMethod(Method[] methods, String _method, Access _accessLevel) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		List<Method> returnList = new LinkedList<>();
		for (Method method : methods) {
			if (method.getName().equals(_method)) {
				if (_accessLevel.isAccess(method.getModifiers())) {
					returnList.add(method);
				}
			}
		}
		return returnList;
	}

	private static List<Method> findMethod(Method[] methods, Access _accessLevel, String _method,
			Class<?>... _classes) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		List<Method> returnList = new LinkedList<>();
		for (Method method : methods) {
			if (method.getName().equals(_method)) {
				if (_accessLevel.isAccess(method.getModifiers())) {
					if (isValidParam(method, _classes)) {
						returnList.add(method);
					}
				}
			}
		}
		return returnList;
	}

	private static Method findMethod(Method[] methods, String _method, Access _accessLevel, Class<?>... _classes) {
		Assertion.notNull(methods, AssertMessage.arg_null_message + " methods");
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		for (Method method : methods) {
			if (method.getName().equals(_method)) {
				if (_accessLevel.isAccess(method.getModifiers())) {
					if (isValidParam(method, _classes)) {
						return method;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get public method of current class
	 * 
	 * @param _class,_params
	 * @return Method
	 */
	public static Method getMethod(Class<?> _class, String _method, Class<?>... _classes) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		try {
			return _class.getMethod(_method, _classes);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	/**
	 * Get all public methods current class
	 * 
	 * @param _class,_params
	 * @return list
	 */
	public static Collection<Method> getAllMethod(Class<?> _class, Class<?>... _params) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		try {
			return fillMethods(_class.getMethods(), _params);
		} catch (SecurityException e) {
		}
		return null;
	}

	/**
	 * Get method current class
	 * 
	 * @param _class,_method,_params
	 * @return Method
	 */
	public static Method getMethod(Class<?> _class, String _method, Object... _param) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		return getMethod(_class, _method, Access.PUBLIC, _param);
	}

	/**
	 * Get field current class with following condition : <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class,_method,_accessLevel,_param
	 * @return Field
	 * @category tested
	 */
	public static Method getMethod(Class<?> _class, String _method, Access _accessLevel, Class<?>... _classes) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		Method returnMethod = findMethod(_class.getDeclaredMethods(), _method, _accessLevel, _classes);
		if (returnMethod != null) {
			return returnMethod;
		}
		if (_class.getSuperclass() != null) {
			for (Class<?> supClass : ClassUtil.getAllSuperClass(_class)) {
				returnMethod = findMethod(supClass.getDeclaredMethods(), _method, _accessLevel, _classes);
				if (returnMethod != null) {
					return returnMethod;
				}
			}
		}
		return null;
	}

	/**
	 * Get field current class with following condition : <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class,_method,_accessLevel,_param
	 * @return Field
	 * @category tested
	 */
	public static Method getMethod(Class<?> _class, String _method, Access _accessLevel, Object... _param) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		return getMethod(_class, _method, _accessLevel, ParamUtil.paramClasses(_param));
	}

	/**
	 * Get all public methods current class
	 * 
	 * @param _class
	 * @return List<Method>
	 * 
	 */
	public static Collection<Method> getAllMethod(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		return getAllMethod(_class, Access.PUBLIC);
	}

	/**
	 * Get methods of current class with following condition : <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class,_method,_accessLevel,_classes
	 * @return List<Method>
	 * @category tested
	 */
	public static Collection<Method> getAllMethod(Class<?> _class, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Collection<Method> returnMethods = new LinkedList<>();
		returnMethods.addAll(fillMethods(_class.getDeclaredMethods(), _accessLevel));
		if (_class.getSuperclass() != null) {
			for (Class<?> supClass : ClassUtil.getAllSuperClass(_class)) {
				returnMethods.addAll(fillMethods(supClass.getDeclaredMethods(), _accessLevel));
			}
		}
		return returnMethods;
	}

	/**
	 * Get all overloaded method of current class with following condition :
	 * <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class,_method,_accessLevel,_classes
	 * @return List<Method>
	 * @category tested
	 */
	public static List<Method> getAllOverloadMethod(Class<?> _class, String _method, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		List<Method> returnMethods = new LinkedList<>();
		returnMethods.addAll(findMethod(_class.getDeclaredMethods(), _method, _accessLevel));
		if (_class.getSuperclass() != null) {
			for (Class<?> _super : ClassUtil.getAllSuperClass(_class)) {
				returnMethods.addAll(findMethod(_super.getDeclaredMethods(), _method, _accessLevel));
			}
		}
		return returnMethods;
	}

	public static Method getMethod(Class<?> _class, String _method, int params, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		for (Method method : getAllOverloadMethod(_class, _method, _accessLevel)) {
			if (method.getParameterCount() == params) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Get all overloaded method of current class with following condition :
	 * <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class,_method,_accessLevel,_classes
	 * @return List<Method>
	 * @category tested
	 */
	public static List<Method> getAllOverrideMethod(Class<?> _class, String _method, Access _accessLevel,
			Object... _param) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Assertion.notNull(_method, AssertMessage.method_name_null_message);
		List<Method> returnMethods = new LinkedList<>();
		returnMethods
				.addAll(findMethod(_class.getDeclaredMethods(), _accessLevel, _method, ParamUtil.paramClasses(_param)));
		for (Class<?> _super : ClassUtil.getAllSuperClass(_class)) {
			returnMethods.addAll(
					findMethod(_super.getDeclaredMethods(), _accessLevel, _method, ParamUtil.paramClasses(_param)));
		}
		return returnMethods;
	}

	public static Boolean hasSetter(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Method[] methods = _class.getMethods();
		for (Method method : methods) {
			if (isSetter(method)) {
				System.out.println("setter: " + method);
			}
		}
		return false;
	}

	public static Boolean hasGetter(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_name_null_message);
		Method[] methods = _class.getMethods();
		for (Method method : methods) {
			if (isGetter(method)) {
				System.out.println("getter: " + method);
			}
		}
		return false;
	}
	
	public static String  getFieldName(Method _method) {
		if(isGetter(_method)) {
			return StringUtil.lowerFirst(StringUtil.getOfter(_method.getName(),"get"));
		}
		if(isSetter(_method)) {
			return StringUtil.lowerFirst(StringUtil.getOfter(_method.getName(),"set"));
		}
		return _method.getName();
	}
	

	public static boolean isGetter(Method _method) {
		Assertion.notNull(_method, AssertMessage.method_object_null_message);
		if (!_method.getName().startsWith("get"))
			return false;
		if (_method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(_method.getReturnType()))
			return false;
		return true;
	}

	public static boolean isSetter(Method _method) {
		Assertion.notNull(_method, AssertMessage.method_object_null_message);
		if (!_method.getName().startsWith("set"))
			return false;
		if (_method.getParameterTypes().length != 1)
			return false;
		return true;
	}

	public static Boolean isMethodExists(Object _object, String _methodName) {
		Assertion.notNull(_object, AssertMessage.object_null_message);
		Assertion.notNull(_methodName, AssertMessage.method_name_null_message);
		Collection<Method> methods = getAllMethod(_object.getClass());
		for (Method method : methods) {
			if (method.getName().equals(_methodName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean matchInterface(Class<?> cls, Class<?> cld) {
		Assertion.notNull(cls, AssertMessage.class_object_null_message + " left");
		Assertion.notNull(cld, AssertMessage.class_object_null_message + " right");
		for (Class<?> C : cld.getInterfaces()) {
			if (C == cls) {
				return true;
			}
		}
		return false;
	}

	public static boolean matchSuperClass(Class<?> cls, Class<?> cld) {
		Assertion.notNull(cls, AssertMessage.class_object_null_message + " left");
		Assertion.notNull(cld, AssertMessage.class_object_null_message + " right");
		for (Class<?> C : ClassUtil.getAllSuperClass(cld)) {
			if (C == cls) {
				return true;
			} else {
				if (matchInterface(cls, C)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Method findMethod(Class<?> _class, String _method, Access _accessLevel, Class<?>... paramClasses) {
		List<Method> methods = getAllOverloadMethod(_class, _method, _accessLevel);
		if (methods.size() > 0) {
			return methods.get(0);
		}
		return null;
	}

}
