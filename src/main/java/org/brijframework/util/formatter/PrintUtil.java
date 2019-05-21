package org.brijframework.util.formatter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.validator.ValidationUtil;

public class PrintUtil {

	@SuppressWarnings("unchecked")
	public static String getObjectInfo(Object object) {
		StringBuilder builder=new StringBuilder(object.getClass().getSimpleName()+"#"+object.hashCode());
		builder.append("(");
		List<Field> fields=FieldUtil.getAllField(object.getClass(),Access.PRIVATE_NO_STATIC_FINAL);
		int len=fields.size();
		for (Field field : fields) {
			Object value=PropertyAccessorUtil.getProperty(object, field);
			if(value==null) {
				builder.append(field.getName()+"= null");
			}else {
				if(object.getClass().getName().equals(value.getClass().getName())) {
					builder.append(field.getName()+"="+value.getClass().getSimpleName()+"#"+value.hashCode());
				}else if(value instanceof Map){
					builder.append(field.getName()+"="+getMapInfo(object, (Map<String,Object>)value));
				} else if(value instanceof Collection){
					builder.append(field.getName()+"="+getListInfo(object, (Collection<Object>)value));
				}else if(!ValidationUtil.isJDKClass(value)) {
					builder.append(field.getName()+"="+getObjectInfo(object,value));
				}else {
					builder.append(field.getName()+"="+value);
				}
			}
			if(len>1) {
			 len--;
			 builder.append(",");
			}
		}
		builder.append(")");
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	private static String getObjectInfo(Object parent, Object object) {
		StringBuilder builder=new StringBuilder(object.getClass().getSimpleName()+"#"+object.hashCode());
		builder.append("(");
		List<Field> fields=FieldUtil.getAllField(object.getClass(),Access.PRIVATE_NO_STATIC_FINAL);
		int len=fields.size();
		for (Field field : fields) {
			Object value=PropertyAccessorUtil.getProperty(object, field);
			if(value==null) {
				builder.append(field.getName()+"= null");
			}else {
				if(parent.getClass().getName().equals(value.getClass().getName())) {
					builder.append(field.getName()+"="+value.getClass().getSimpleName()+"#"+value.hashCode());
				}else if(value instanceof Map){
					builder.append(field.getName()+"="+getMapInfo(parent, (Map<String,Object>)value));
				} else if(value instanceof Collection){
					builder.append(field.getName()+"="+getListInfo(parent, (Collection<Object>)value));
				}else if(!ValidationUtil.isJDKClass(value)) {
					builder.append(field.getName()+"="+getObjectInfo(object,value));
				}else {
					builder.append(field.getName()+"="+value);
				}
			}
			if(len>1) {
			 len--;
			 builder.append(",");
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static String getMapInfo(Object parent, Map<String,Object> map) {
		StringBuilder builder=new StringBuilder(map.getClass().getSimpleName());
		builder.append("(");
		int len=map.keySet().size();
		for (String field : map.keySet()) {
			Object value=map.get(field);
			if(value==null) {
				builder.append(field+"= null");
			}else {
				if(value instanceof Map){
					builder.append(field+"="+getMapInfo(parent, (Map<String,Object>)value));
				}else if(value instanceof Collection){
					builder.append(field+"="+getListInfo(parent, (Collection<Object>)value));
				}else if(!ValidationUtil.isJDKClass(value)) {
					builder.append(field+"="+getObjectInfo(parent, value));
				} else {
					builder.append(field+"="+value);
				}
			}
			if(len>1) {
			 len--;
			 builder.append(",");
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static String getListInfo(Object parent, Collection<Object> list) {
		StringBuilder builder=new StringBuilder(list.getClass().getSimpleName());
		builder.append("(");
		int len=list.size();
		for (Object value : list) {
			if(value==null) {
				builder.append(value);
			}else {
				if(parent.getClass().getName().equals(value.getClass().getName())) {
					builder.append(value.getClass().getSimpleName()+"#"+value.hashCode());
				}else 
				if(value instanceof Map){
					builder.append(getMapInfo(parent, (Map<String,Object>)value));
				}else if(value instanceof Collection){
					builder.append(getListInfo(parent, (Collection<Object>)value));
				}else if(!ValidationUtil.isJDKClass(value)) {
					builder.append(getObjectInfo(parent, value));
				} else {
					builder.append(value);
				}
			}
			if(len>1) {
			 len--;
			 builder.append(",");
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
}
