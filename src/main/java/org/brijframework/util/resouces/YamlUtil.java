package org.brijframework.util.resouces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

public class YamlUtil {
	public static HashMap<String, Object> getHashMap(File file ){
	    try {
			return getHashMap(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, Object> getHashMap(InputStream in){
	    return getObjectMap(  HashMap.class ,in);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectMap(Class<?> cls,InputStream in){
		Yaml yaml = new Yaml();  
	    return (T) yaml.loadAs( in, cls);
	}
}
