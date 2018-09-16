package org.brijframework.util.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;

public abstract class ParamUtil {
	
	public static Class<?>[] paramClasses(Object..._objects) {
		if(_objects==null){
			return null;
		}
		Class<?>[] classess=new Class[_objects.length];
		for(int index=0;index<_objects.length;index++){
			classess[index]=_objects[index] instanceof Class<?>  ?(Class<?>)_objects[index]:_objects[index].getClass();
		}
		return classess;
	}
	
	public static boolean isValidParam(Method _method,Object[] _agruments){
		return isEqualParam(_method.getParameterTypes(), _agruments);
	}
	
	public static boolean isEqualParam(Type[] _params,Type..._agruments) {
		if(_agruments==null && _params.length==0){
			return true;
		}
		if(_params==null && _agruments==null){
			return true;
		}
		if(_params.length !=_agruments.length){
			return false;
		}
        for(int index=0;index<_params.length;index++){
        	if(!isEqualParam((Class<?>)_params[index],(Class<?>)_agruments[index])){
        		return false;
        	}
		}
		return true;
	}
	public static boolean isEqualParam(Type[] _params,Object ..._agruments){
		return isEqualParam(_params, paramClasses(_agruments));
	}
	
	public static boolean  isPrimitiveEq(Class<?> params,Class<?> agr) {
		if(params.isAssignableFrom(int.class) && agr.isAssignableFrom(Integer.class)){
			return true;
		}
		if(params.isAssignableFrom(Integer.class) && agr.isAssignableFrom(int.class)){
			return true;
		}
		if(params.isAssignableFrom(long.class) && agr.isAssignableFrom(Long.class)){
			return true;
		}
		if(params.isAssignableFrom(Long.class) && agr.isAssignableFrom(long.class)){
			return true;
		}
		if(params.isAssignableFrom(char.class) && agr.isAssignableFrom(Character.class)){
			return true;
		}
		if(params.isAssignableFrom(Character.class) && agr.isAssignableFrom(char.class)){
			return true;
		}
		if(params.isAssignableFrom(double.class) && agr.isAssignableFrom(Double.class)){
			return true;
		}
		if(params.isAssignableFrom(Double.class) && agr.isAssignableFrom(double.class)){
			return true;
		}
		if(params.isAssignableFrom(float.class) && agr.isAssignableFrom(Float.class)){
			return true;
		}
		if(params.isAssignableFrom(Float.class) && agr.isAssignableFrom(float.class)){
			return true;
		}
		return false;
	}
	
	public static boolean isEqualParam(Class<?> params,Class<?> agr){
		if(Class.class.isAssignableFrom(params) && Class.class.isAssignableFrom(agr)){
    		return true;
    	}
		if(Method.class.isAssignableFrom(params) && Method.class.isAssignableFrom(agr)){
    		return true;
    	}
		if(Field.class.isAssignableFrom(params) && Field.class.isAssignableFrom(agr)){
    		return true;
    	}
		
		if(isPrimitiveEq(params, agr)) {
			return true;
		}
		
		if(params.isAssignableFrom(agr)){
			return true;
		}
		return false;
	}

	public static boolean isValidParam(Constructor<?> _method,Object..._agruments) {
		Assertion.notNull(_method, AssertMessage.method_object_null_message);
		Class<?>[] _parames=_method.getParameterTypes();
		return isEqualParam(_parames, _agruments);
	}

	public static boolean isEqualTypes(Type[] _params, Type[] _agruments) {
		if(_params==null && _agruments==null ) {
			return true;
		}
		
		if(_params==null && _agruments!=null ) {
			return false;
		}
		
		if(_params!=null && _agruments==null ) {
			return false;
		}
		
		if(_params.length!=_agruments.length) {
			return false;
		}
		
		for(int index=0;index<_params.length;index++){
			if(!isEqualParam((Class<?>)_params[index],(Class<?>)_agruments[index])){
				return false;
			}
		}
		return true;
	}
	

	/**
	 * Check if the right-hand side type may be assigned to the left-hand side type
	 * following the Java generics rules.
	 * 
	 * @param lhsType
	 *            the target type
	 * @param rhsType
	 *            the value type that should be assigned to the target type
	 * @return true if rhs is assignable to lhs
	 */
	public static boolean isAssignable(Type lhsType, Type rhsType) {
		Assertion.notNull(lhsType, "Left-hand side type must not be null");
		Assertion.notNull(rhsType, "Right-hand side type must not be null");

		// all types are assignable to themselves and to class Object
		if (lhsType.equals(rhsType) || lhsType.equals(Object.class)) {
			return true;
		}

		if (lhsType instanceof Class<?>) {
			Class<?> lhsClass = (Class<?>) lhsType;

			// just comparing two classes
			if (rhsType instanceof Class<?>) {
				return isAssignable(lhsClass, (Class<?>) rhsType);
			}

			if (rhsType instanceof ParameterizedType) {
				Type rhsRaw = ((ParameterizedType) rhsType).getRawType();

				// a parameterized type is always assignable to its raw class type
				if (rhsRaw instanceof Class<?>) {
					return isAssignable(lhsClass, (Class<?>) rhsRaw);
				}
			} else if (lhsClass.isArray() && rhsType instanceof GenericArrayType) {
				Type rhsComponent = ((GenericArrayType) rhsType).getGenericComponentType();

				return isAssignable(lhsClass.getComponentType(), rhsComponent);
			}
		}

		// parameterized types are only assignable to other parameterized types and
		// class types
		if (lhsType instanceof ParameterizedType) {
			if (rhsType instanceof Class<?>) {
				Type lhsRaw = ((ParameterizedType) lhsType).getRawType();

				if (lhsRaw instanceof Class<?>) {
					return isAssignable((Class<?>) lhsRaw, (Class<?>) rhsType);
				}
			} else if (rhsType instanceof ParameterizedType) {
				return isAssignable((ParameterizedType) lhsType, (ParameterizedType) rhsType);
			}
		}

		if (lhsType instanceof GenericArrayType) {
			Type lhsComponent = ((GenericArrayType) lhsType).getGenericComponentType();

			if (rhsType instanceof Class<?>) {
				Class<?> rhsClass = (Class<?>) rhsType;

				if (rhsClass.isArray()) {
					return isAssignable(lhsComponent, rhsClass.getComponentType());
				}
			} else if (rhsType instanceof GenericArrayType) {
				Type rhsComponent = ((GenericArrayType) rhsType).getGenericComponentType();

				return isAssignable(lhsComponent, rhsComponent);
			}
		}

		if (lhsType instanceof WildcardType) {
			return isAssignable((WildcardType) lhsType, rhsType);
		}

		return false;
	}

	private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
		if (lhsType.equals(rhsType)) {
			return true;
		}

		Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
		Type[] rhsTypeArguments = rhsType.getActualTypeArguments();

		if (lhsTypeArguments.length != rhsTypeArguments.length) {
			return false;
		}

		for (int size = lhsTypeArguments.length, i = 0; i < size; ++i) {
			Type lhsArg = lhsTypeArguments[i];
			Type rhsArg = rhsTypeArguments[i];

			if (!lhsArg.equals(rhsArg)
					&& !(lhsArg instanceof WildcardType && isAssignable((WildcardType) lhsArg, rhsArg))) {
				return false;
			}
		}

		return true;
	}

	private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
		Type[] lUpperBounds = lhsType.getUpperBounds();

		// supply the implicit upper bound if none are specified
		if (lUpperBounds.length == 0) {
			lUpperBounds = new Type[] { Object.class };
		}

		Type[] lLowerBounds = lhsType.getLowerBounds();

		// supply the implicit lower bound if none are specified
		if (lLowerBounds.length == 0) {
			lLowerBounds = new Type[] { null };
		}

		if (rhsType instanceof WildcardType) {
			// both the upper and lower bounds of the right-hand side must be
			// completely enclosed in the upper and lower bounds of the left-
			// hand side.
			WildcardType rhsWcType = (WildcardType) rhsType;
			Type[] rUpperBounds = rhsWcType.getUpperBounds();

			if (rUpperBounds.length == 0) {
				rUpperBounds = new Type[] { Object.class };
			}

			Type[] rLowerBounds = rhsWcType.getLowerBounds();

			if (rLowerBounds.length == 0) {
				rLowerBounds = new Type[] { null };
			}

			for (Type lBound : lUpperBounds) {
				for (Type rBound : rUpperBounds) {
					if (!isAssignableBound(lBound, rBound)) {
						return false;
					}
				}

				for (Type rBound : rLowerBounds) {
					if (!isAssignableBound(lBound, rBound)) {
						return false;
					}
				}
			}

			for (Type lBound : lLowerBounds) {
				for (Type rBound : rUpperBounds) {
					if (!isAssignableBound(rBound, lBound)) {
						return false;
					}
				}

				for (Type rBound : rLowerBounds) {
					if (!isAssignableBound(rBound, lBound)) {
						return false;
					}
				}
			}
		} else {
			for (Type lBound : lUpperBounds) {
				if (!isAssignableBound(lBound, rhsType)) {
					return false;
				}
			}

			for (Type lBound : lLowerBounds) {
				if (!isAssignableBound(rhsType, lBound)) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isAssignableBound(Type lhsType, Type rhsType) {
		if (rhsType == null) {
			return true;
		}

		if (lhsType == null) {
			return false;
		}
		return isAssignable(lhsType, rhsType);
	}

}
