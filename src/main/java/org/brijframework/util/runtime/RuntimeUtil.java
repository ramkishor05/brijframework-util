package org.brijframework.util.runtime;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public abstract class RuntimeUtil {

	@SuppressWarnings("static-access")
	public static List<Object> runClasses() {
		List<Object> list = new ArrayList<>();
		Map<Thread, StackTraceElement[]> map = Thread.currentThread().getAllStackTraces();
		Set<Thread> entry = map.keySet();
		for (Thread thread : entry) {
			for (StackTraceElement element : map.get(thread).clone()) {
				list.add(element.getClassName());
			}
		}
		return list;
	}


	@SuppressWarnings("static-access")
	public static List<String> runMethods() {
		List<String> list = new ArrayList<>();
		Map<Thread, StackTraceElement[]> map = Thread.currentThread().getAllStackTraces();
		Set<Thread> entry = map.keySet();
		for (Thread thread : entry) {
			for (StackTraceElement element : map.get(thread).clone()) {
				list.add(element.getMethodName());
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Iterator<?> list(ClassLoader CL)
	        throws NoSuchFieldException, SecurityException,
	        IllegalArgumentException, IllegalAccessException {
	        Class<?> CL_class = CL.getClass();
	        while (CL_class != java.lang.ClassLoader.class) {
	            CL_class = CL_class.getSuperclass();
	        }
	        java.lang.reflect.Field ClassLoader_classes_field = CL_class.getDeclaredField("classes");
	        ClassLoader_classes_field.setAccessible(true);
	        Vector classes = (Vector) ClassLoader_classes_field.get(CL);
	        return classes.iterator();
	    }


	private static void runCommandAndGetOutput() {
		String command = "ping www.codejava.net";

		try {
			Process process = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void runCommandAndWait() {
		String command = "ping www.codejava.net";
		
		try {
			Process process = Runtime.getRuntime().exec(command);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			
			int exitValue = process.waitFor();
			if (exitValue != 0) {
				System.out.println("Abnormal process termination");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void runCommandTypically() {
		String command = "ping www.codejava.net";
		
		try {
			Process process = Runtime.getRuntime().exec(command);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			
			process.destroy();
			if (process.exitValue() != 0) {
				System.out.println("Abnormal process termination");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void runCommandAndSendInput() {
		String command = "cmd /c date";
		
		try {
			Process process = Runtime.getRuntime().exec(command);
			
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(process.getOutputStream()));
			writer.write("09-20-14");
			writer.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void runCommandAndGetErrorOutput() {
		String command = "cmd /c verr";

		try {
			Process process = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();

			System.out.println("ERROR:");
			
			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			while ((line = errorReader.readLine()) != null) {
				System.out.println(line);
			}
			errorReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void runCommandArray() {
		String commandArray[] = {"cmd", "/c", "dir", "C:\\Program Files"};

		try {
			Process process = Runtime.getRuntime().exec(commandArray);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
       runCommandAndSendInput();
	   runCommandAndGetErrorOutput();
	   runCommandAndGetOutput();
       runCommandAndWait();
	   runCommandArray();
       ClassLoader myCL = Thread.currentThread().getContextClassLoader();
       while (myCL != null) {
           System.out.println("ClassLoader: " + myCL);
           for (Iterator<?> iter = list(myCL); iter.hasNext();) {
               System.out.println("\t" + iter.next());
           }
           myCL = myCL.getParent();
       }
      
	}	
	
}
