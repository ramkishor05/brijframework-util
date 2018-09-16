package org.brijframework.util.accessor;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.text.StringUtil;
import org.brijframework.util.validator.ValidationUtil;

public class EventValidateUtil {
	public static final String ADD_PREFIX = "add";
	public static final String REMOVE_PREFIX = "remove";
	public static final String GET_PREFIX = "get";
	public static final String SET_PREFIX = "set";
	public static final String IS_PREFIX = "is";

	public static String setter(String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return SET_PREFIX + StringUtil.capitalFirst(field);
	}

	public static String getter(String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return GET_PREFIX + StringUtil.capitalFirst(field);
	}

	public static String is(String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return IS_PREFIX + StringUtil.capitalFirst(field);
	}

	public static String adder(String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return ADD_PREFIX + StringUtil.capitalFirst(field);
	}

	public static String remover(String field) {
		return REMOVE_PREFIX + StringUtil.capitalFirst(field);
	}

	public static String brijField(String field) {
		if (ValidationUtil.isValidObject(field)) {
			return field;
		}
		return null;
	}

	public static boolean validReader(String method, String field) {
		Assertion.notNull(method, AssertMessage.method_name_null_message);
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return method.contentEquals(getter(field)) || method.contentEquals(is(field));
	}

	public static boolean validWriter(String method, String field) {
		Assertion.notNull(method, AssertMessage.method_name_null_message);
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return method.contentEquals(setter(field)) || method.contentEquals(is(field))|| method.contentEquals(adder(field)) || method.contentEquals(remover(field));
	}

}
