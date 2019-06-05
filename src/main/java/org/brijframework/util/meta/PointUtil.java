package org.brijframework.util.meta;

import org.brijframework.util.support.Constants;

public class PointUtil {
	
	public static String superPoint(String _keyPath) {
		StringBuilder builder=new StringBuilder();
		String[] _keyArray=_keyPath.split(Constants.SPLIT_DOT);
		for (int i = 0; i < _keyArray.length-2; i++) {
			builder.append(_keyArray[i]);
			if(i<_keyArray.length-3) {
				builder.append(".");
			}
		}
		return builder.toString();
	}

	
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
	
	public static String keyArray(String _keyPath) {
		String _key=_keyPath;
		if(_key.contains(Constants.OPEN_BRAKET)) {
			_key=_key.split("\\"+Constants.OPEN_BRAKET)[0];
		}
		return _key;
	}
	
	public static Integer indexArray(String _keyPath) {
		return Integer.valueOf(_keyPath.substring(_keyPath.indexOf(Constants.OPEN_BRAKET)+1, _keyPath.indexOf(Constants.CLOSE_BRAKET)));
	}
}