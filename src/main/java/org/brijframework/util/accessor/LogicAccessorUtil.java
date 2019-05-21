package org.brijframework.util.accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Access;

public class LogicAccessorUtil{

	public static Object callLogic(Object bean,String method, Object... paramObjects) {
		return callLogic(bean,method,Access.PUBLIC, paramObjects);
	}
	
	public static Object callLogic(Object bean,String method, Access access,Object... paramObjects) {
		Method collMethod = MetaAccessorUtil.getLogicMeta(bean.getClass(),method, access,paramObjects);
		return callLogic(bean,collMethod, paramObjects);
	}
	
	public static  Object callLogic(Object bean,Method collMethod, Object... paramObjects) {
		try {
			Assertion.notNull(collMethod, "method "+AssertMessage.Not_found_message);
			collMethod.setAccessible(true);
			return collMethod.invoke(bean, paramObjects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
