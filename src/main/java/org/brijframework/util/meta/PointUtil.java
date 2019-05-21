package org.brijframework.util.meta;

import org.brijframework.util.support.Constants;

public class PointUtil {
	
	public static String currentPoint(String _keyPath) {
		StringBuilder builder=new StringBuilder();
		String[] _keyArray=_keyPath.split(Constants.SPLIT_DOT);
		for (int i = 0; i < _keyArray.length-1; i++) {
			builder.append(_keyArray[i]);
			if(i<_keyArray.length-2) {
				builder.append(".");
			}
		}
		return builder.toString();
	}

	public static String keyPoint(String _keyPath) {
		String _key=_keyPath;
		String[] _keyArray=_keyPath.split(Constants.SPLIT_DOT);
		for (int i = 0; i < _keyArray.length; i++) {
			_key=_keyArray[i];
		}
		return _key;
	}
}