package org.brijframework.util.reflect;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Access;
import org.brijframework.util.validator.ValidationUtil;

public abstract class FieldUtil {


	public static final String field_getName = "getName";
	

	/**
	 * Find field of current class
	 * 
	 * @param _fields
	 * @param _field
	 * @param _accessLevel
	 * @return Field
	 */
	private static Field findField(Field[] _fields, String _field, Access _accessLevel) {
		Assertion.notNull(_fields, AssertMessage.arg_null_message+" fields");
		Assertion.notNull(_field, AssertMessage.arg_null_message+" field");
		Assertion.notNull(_accessLevel, AssertMessage.arg_null_message+" accessLevel");
		for (Field field : _fields) {
			if (field.getName().equals(_field)) {
				if (_accessLevel.isAccess(field.getModifiers())) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * Fill all field spicified access leval class
	 * 
	 * @param _fields
	 * @param _field
	 * @param _accessLevel
	 * @return list of field
	 */
	private static List<Field> fillFields(Field[] _fields, Access _accessLevel) {
		Assertion.notNull(_fields, AssertMessage.arg_null_message+" fields");
		Assertion.notNull(_accessLevel, AssertMessage.arg_null_message+" accessLevel");
		List<Field> list = new ArrayList<>();
		for (Field field : _fields) {
			if (_accessLevel.isAccess(field.getModifiers())) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * Get all public fields current class
	 * 
	 * @param _class
	 * @return list of field
	 */
	public static List<Field> getAllField(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		return getAllField(_class, Access.PUBLIC);
	}

	/**
	 * get public field current class
	 * 
	 * @param _class
	 * @param _field
	 * @return Field
	 */
	public static Field getField(Class<?> _class, String _field) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Assertion.notNull(_field, AssertMessage.field_name_null_message);
		return getField(_class,_field,Access.PUBLIC);
	}

	/**
	 * get all names of fields current class
	 * 
	 * @param _class
	 * @return List of String
	 */
	public static List<String> getFieldList(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Collection<Field> collection = getAllField(_class);
		return LogicUnit.collectionMethod(collection, field_getName);
	}

	/**
	 * Get all names field current class with following condition : <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class
	 * @param _accessLevel
	 * @return List of String
	 */
	public static List<String> getFieldList(Class<?> _class, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Collection<Field> collection = getAllField(_class, _accessLevel);
		return LogicUnit.collectionMethod(collection, field_getName);
	}

	/**
	 * Get field current class with following condition : <br>
	 * PRIVATE can access protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class
	 * @param _field
	 * @param _accessLevel
	 * @return Field
	 */
	public static Field getField(Class<?> _class, String _field, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		Assertion.notNull(_field, AssertMessage.field_name_null_message);
		Field field = findField(_class.getDeclaredFields(), _field, _accessLevel);
		if (field != null) {
			return field;
		}
		if (_class.getSuperclass() != null) {
			for (Class<?> supClass : ClassUtil.getAllSuperClass(_class)) {
				field = findField(supClass.getDeclaredFields(), _field, _accessLevel);
				if (field != null) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * get all fields current class with following condition : <br>
	 * PRIVATE can access all protected , public , private , default field <br>
	 * PROTECTED can access only protected , public <br>
	 * PUBLIC can access only public <br>
	 * DEFAULT can access only protected , public ,default
	 * 
	 * @param _class
	 * @param _accessLevel
	 * @return List of Field
	 */
	public static List<Field> getAllField(Class<?> _class, Access _accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> list = new ArrayList<>();
		list.addAll(fillFields(_class.getDeclaredFields(), _accessLevel));
		if (_class.getSuperclass() != null) {
			for (Class<?> _super : ClassUtil.getAllSuperClass(_class)) {
				list.addAll(fillFields(_super.getDeclaredFields(), _accessLevel));
			}
		}
		return list;
	}
	
	public static List<Field> getAllManyRelField(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		return getAllManyRelField(_class,Access.PUBLIC);
	}

	
	public static List<Field> getAllManyRelField(Class<?> _class,Access accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> fields=new LinkedList<>();
		for(Field field:getAllField(_class,accessLevel)){
			if(Collection.class.isAssignableFrom(field.getType())){
			  fields.add(field);
			}
		}
		return fields;
	}

	
	public static List<Field> getAllOneRelField(Class<?> _class, Access accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> fields=new LinkedList<>();
		for(Field field:getAllField(_class,accessLevel)){
			if(!Collection.class.isAssignableFrom(field.getType())&& !ValidationUtil.isPrimative(field.getType())){
			  fields.add(field);
			}
		}
		return fields;
	}
	
	public static List<Field> getAllPrimativeField(Class<?> _class) {
		Assertion.notNull(_class,AssertMessage. class_object_null_message);
		return getAllPrimativeField(_class, Access.PUBLIC);
	}
	
	public static List<Field> getAllPrimativeField(Class<?> _class, Access accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> fields=new LinkedList<>();
		for(Field field:getAllField(_class,accessLevel)){
			if(ValidationUtil.isPrimative(field.getType())){
			  fields.add(field);
			}
		}
		return fields;
	}
	
	public static List<Field> getAllRelField(Class<?> _class){
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		return getAllRelField(_class,Access.PUBLIC);
	}

	public static List<Field> getAllRelField(Class<?> _class, Access accessLevel) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Field> fields=new LinkedList<>();
		for(Field field:getAllField(_class,accessLevel)){
			if(!ValidationUtil.isPrimative(field.getType())){
			  fields.add(field);
			}
		}
		return fields;
	}

	public static boolean isRelField(Field _field) {
		Assertion.notNull(_field, AssertMessage.field_object_null_message);
		return !ValidationUtil.isPrimative(_field.getType());
	}
	
	
}
