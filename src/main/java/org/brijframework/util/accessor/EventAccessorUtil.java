package org.brijframework.util.accessor;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventAccessorUtil{

	private EventAccessorUtil() {
	}


	public static boolean  containEvent(Object bean,String name) {
		try {
			return findEvent(bean,name)!=null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Method findEvent(Object bean,String name)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass());
		EventSetDescriptor[] events = beaninfo.getEventSetDescriptors();
		for (EventSetDescriptor event : events) {
			Method addMethod = event.getAddListenerMethod();
			if (addMethod != null && addMethod.getName().contentEquals(name)) {
				return addMethod;
			}
		}
		for (EventSetDescriptor event : events) {
			Method removeMethod = event.getRemoveListenerMethod();
			if (removeMethod != null && removeMethod.getName().contentEquals(name)) {
				return removeMethod;
			}
		}
		for (EventSetDescriptor event : events) {
			Method getMethod = event.getGetListenerMethod();
			if (getMethod != null && getMethod.getName().contentEquals(name)) {
				return getMethod;
			}
		}
		return null;
	}

}
