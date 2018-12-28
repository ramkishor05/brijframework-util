package org.brijframework.util.formatter;

import java.lang.reflect.Field;
import java.util.List;

import org.brijframework.support.enums.Access;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.PackUtil;

public class PrintUtil {

	public static String getObjectInfo(Object object) {
		StringBuilder builder=new StringBuilder(object.getClass().getSimpleName());
		builder.append("(");
		List<Field> fields=FieldUtil.getAllField(object.getClass(),Access.PRIVATE_NO_STATIC_FINAL);
		int len=fields.size();
		for (Field field : fields) {
			Object value=PropertyAccessorUtil.getProperty(object, field);
			if(value!=null && PackUtil.isProjectClass(value.getClass())) {
				builder.append(field.getName()+"="+getObjectInfo(value));
			}else {
				builder.append(field.getName()+"="+value);
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
