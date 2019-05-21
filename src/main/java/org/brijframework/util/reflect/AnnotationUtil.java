package org.brijframework.util.reflect;


import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AnnotationUtil {
	/**
	 * Get annotation object from class
	 * 
	 * @param _class
	 * @param _annotation
	 * @return annotation
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Annotation getAnnotation(Class<?> _class, Class _annotation) {
		Annotation annotation = null;
		if (_class != null) {
			annotation = _class.getAnnotation(_annotation);
		}
		return annotation;
	}

	
	/**
	 * Get annotation object from field
	 * 
	 * @param _field
	 * @param _annotation
	 * @return annotation
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Annotation getAnnotation(AccessibleObject _field, Class _annotation) {
		Annotation annotation = null;
		if (_field != null) {
			annotation = _field.getAnnotation(_annotation);
		}
		return annotation;
	}

	/**
	 * Check annotation is exist or not in class
	 * 
	 * @param _class
	 * @param _annotation
	 * @return boolean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isExistAnnotation(Class<?> _class, Class _annotation) {
		Annotation annotation = null;
		if (_class != null) {
			annotation = _class.getAnnotation(_annotation);
		}
		return annotation != null;
	}

	/**
	 * Check annotation is exist or not in field
	 * 
	 * @param _field
	 * @param _annotation
	 * @return boolean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isExistAnnotation(Field _field, Class _annotation) {
		Annotation annotation = null;
		if (_field != null) {
			annotation = _field.getAnnotation(_annotation);
		}
		return annotation != null;
	}

	/**
	 * Check annotation is exist or not in method
	 * 
	 * @param _method
	 * @param _annotation
	 * @return boolean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isExistAnnotation(Method _method, Class _annotation) {
		Annotation annotation = null;
		if (_method != null) {
			annotation = _method.getAnnotation(_annotation);
		}
		return annotation != null;
	}

	/**
	 * Check and get annotation is contain field
	 * 
	 * @param _class
	 * @param _annotation
	 * @return Field
	 */
	@SuppressWarnings("rawtypes")
	public static Field getAnnotationField(Class<?> _class, Class _annotation) {
		List<Field> fields = FieldUtil.getAllField(_class);
		for (Field field : fields) {
			if (getAnnotation(field, _annotation) != null) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Check and get all annotation contain in field
	 * 
	 * @param _class
	 * @param _annotation
	 * @return Field
	 */
	public static List<Field> getAnnotationFields(Class<?> _class, Class<?> _annotaion) {
		List<Field> fields = FieldUtil.getAllField(_class);
		List<Field> fieldReturn = new ArrayList<>();
		for (Field field : fields) {
			if (getAnnotation(field, _annotaion) != null) {
				fieldReturn.add(field);
			}
		}
		return fieldReturn;
	}

	/**
	 * Get all annotation from class
	 * 
	 * @param _class
	 * @return Annotation[]
	 */
	public static Annotation[] getAnnotations(Class<?> _class) {
		return _class.getAnnotations();
	}

	/**
	 * Get all annotation from method
	 * 
	 * @param _class
	 * @return Annotation[]
	 */
	public static Annotation[] getAnnotations(Method _method) {
		return _method.getAnnotations();
	}

	/**
	 * Get all annotation from field
	 * 
	 * @param _class
	 * @return Annotation[]
	 */
	public static Annotation[] getAnnotations(Field _Field) {
		return _Field.getAnnotations();
	}

	/**
	 * Get all data from class
	 * 
	 * @param _class
	 * @param _annotation
	 * @return map
	 */
	public static Map<String, Object> getAnnotationData(Class<?> _class, Class<?> _annotation) {
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		Annotation annotation = getAnnotation(_class, _annotation);
		if(annotation==null){
			return _returnMap;
		}
		Method[] methods = annotation.annotationType().getMethods();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}
	
	public static Map<String, Object> getAnnotationData(Constructor<?> _class, Class<?> _annotation) {
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		Annotation annotation = getAnnotation(_class, _annotation);
		if(annotation==null){
			return _returnMap;
		}
		Method[] methods = annotation.annotationType().getMethods();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}

	public static Map<String, Object> getAnnotationData(Parameter _class, Class<? extends Annotation> _annotation) {
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		Annotation annotation = getAnnotation(_class, _annotation);
		if(annotation==null){
			return _returnMap;
		}
		Method[] methods = annotation.annotationType().getMethods();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}

	public static Annotation getAnnotation(Parameter _class, Class<? extends Annotation> _annotation) {
		if (_class == null) {
			return null;
		}
		return _class.getAnnotation( _annotation);
	}


	/**
	 * Get all data from field
	 * 
	 * @param _field
	 * @param _annotation
	 * @return map
	 */
	public static Map<String, Object> getAnnotationDataForField(Field _field, Class<?> _annotation) {
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		Annotation annotation = getAnnotation(_field, _annotation);
		if(annotation==null){
			return _returnMap;
		}
		Method[] methods = annotation.annotationType().getMethods();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}

	/**
	 * Get all data from method
	 * 
	 * @param _method
	 * @param _annotation
	 * @return map
	 */
	public static Map<String, Object> getAnnotationDataForMethod(Method _method, Class<?> _annotation) {
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		Annotation annotation = getAnnotation(_method, _annotation);
		if(annotation==null){
			return _returnMap;
		}
		Method[] methods = annotation.annotationType().getMethods();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}
	

	/**
	 * Get all data from annotation
	 * 
	 * @param _annotation
	 * @return map
	 */
	public static Map<String, Object> getAnnotationData(Annotation _annotation) {
		Method[] methods = _annotation.annotationType().getMethods();
		Map<String, Object> _returnMap = new HashMap<String, Object>();
		for (Method method : methods) {
			try {
				if (method.getDeclaringClass().equals(_annotation.annotationType())) {
					_returnMap.put(method.getName(), method.invoke(_annotation));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return _returnMap;
	}

	/**
	 * Get all data from any object
	 * 
	 * @param _object
	 * @param _annotation
	 * @return
	 */
	public static Map<String, Object> getAnnotionData(Object _object, Class<?> _annotation) {
		if (_object instanceof Method) {
			return getAnnotationDataForMethod((Method) _object, _annotation);
		}
		if (_object instanceof Field) {
			return getAnnotationDataForField((Field) _object, _annotation);
		}
		if (_object instanceof Class) {
			return getAnnotationData((Class<?>) _object, _annotation);
		}
		return null;
	}


	public static Annotation getAnnotation(Class<?> _class, Class<?> _annotation,String target, String key, String value) {
		Annotation annotation=getAnnotation(_class, _annotation);
		if(annotation==null) {
			return null;
		}
		for(Method method:annotation.annotationType().getMethods()) {
			if(!target.equals(method.getName())) {
				continue;
			}
			Annotation[] annotations=LogicUnit.callMethod(annotation, method);
			for(Annotation ann:annotations) {
				for(Method meth:ann.annotationType().getMethods()) {
					if(!meth.getName().equals(key)) {
						continue;
					}
					Object val=LogicUnit.callMethod(ann, method);
					if(val!=null && val.equals(value)) {
						return ann;
					}
				}
			}
		}
		return null;
	}


	public static Map<String, Object> getAnnotationData(AccessibleObject target, Class<?> _annotation) {
		if(target instanceof Method) {
			return getAnnotationDataForMethod((Method)target,_annotation);
		}
		if(target instanceof Field) {
			return getAnnotationDataForField((Field)target,_annotation);
		}
		return null;
	}
}
