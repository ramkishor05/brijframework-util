package org.brijframework.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.brijframework.support.enums.Access;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;

public class ConstructUtil {

	public ConstructUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public ConstructUtil(int i) {
		// TODO Auto-generated constructor stub
	}
	
	public ConstructUtil(String st,int i) {
		// TODO Auto-generated constructor stub
	}


	public static List<Constructor<?>> getConstructors(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Constructor<?>> constructors = new ArrayList<>();
		for (Constructor<?> constructor : _class.getDeclaredConstructors()) {
			constructors.add(constructor);
		}
		return constructors;
	}

	public static Constructor<?> getConstructor(Class<?> _class,Access access, Type... types) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		for (Constructor<?> constructor : _class.getDeclaredConstructors()) {
			if(!access.isAccess(constructor.getModifiers())) {
				continue;
			}
			if (ParamUtil.isEqualTypes(types,constructor.getParameterTypes())) {
				return constructor;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getConstructor(ConstructUtil.class,Access.DEFAULT,String.class,int.class));
	}

}
