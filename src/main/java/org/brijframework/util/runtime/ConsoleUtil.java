package org.brijframework.util.runtime;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.brijframework.util.validator.ValidationUtil;


/*
 * This class will close all the  cmd running on system.
 * It will log in file with PID of all cmd which has been closed.
 * 
 * e.g SUCCESS: The process "cmd.exe" with PID 4192 has been terminated.
 */
public abstract class ConsoleUtil {
	public static PrintStream consoleOutStream;
	public static PrintStream consoleErrStream;
	public static PrintStream outputLogStream;
	static boolean taskClosed=false;
	static Scanner scanner=new Scanner(System.in);
	static Console console=System.console();
	
	@SuppressWarnings("unused")
	public static String getPassword() {
		System.out.println("Please Enter Password: ");
		char input[] =console.readPassword();
		String password=new String(input);
		if((password==null)&&(password.isEmpty())){
			System.err.println("Wong Input");
			return getPassword();
		}
		return password;
	}
	
	@SuppressWarnings("unused")
	public static String getPassword(String regex) {
		System.out.println("Please Enter Password: ");
		char input[] =console.readPassword();
		String password=new String(input);
		if((password==null)&&(password.isEmpty())&&password.matches(regex)){
			System.err.println("Wong Input");
			return getPassword();
		}
		return password;
	}
	public static String getNumber(){
		System.out.println("Please Enter Number : ");
		String input=scanner.next();
		if(!ValidationUtil.isNumber(input)){
			System.err.println("Wong Input  ");
			return getNumber();
		}
		return input;
	}
	
	public String getBinaryNumber(){
		System.out.println("Please Enter Binary Number : ");
		String input=scanner.next();
		if(!ValidationUtil.isBinaryNumber(input)){
			System.err.println("Wong Input  ");
			return getBinaryNumber();
		}
		return input;
	}
	
	
	public  String getOctalNumber(){
		System.out.println("Please Enter Octol Number : ");
		String input=scanner.next();
		if(!ValidationUtil.isOctalNumber(input)){
			System.err.println("Wong Input");
			return getOctalNumber();
		}
		return input;
	}
	
	
	public static String getHexaNumber(){
		System.out.println("Please Enter Hex String: ");
		String input=scanner.next();
		if(!ValidationUtil.isHexaNumber(input)){
			System.err.println("Wong Input  ");
			return getHexaNumber();
		}
		return input;
	}
	
	public static String getString(){
		System.out.println("Please Enter String : ");
		String input=scanner.next();
		if(!ValidationUtil.isAlphabet(input)){
			System.err.println("Wong Input  ");
			return getString();
		}
		return input;
	}
	
	public static String getDoubleNumber(){
		System.out.println("Please Enter Double : ");
		String input=scanner.next();
		if(!ValidationUtil.isDouble(input)){
			System.err.println("Wong Input  ");
			return getDoubleNumber();
		}
		return input;
	}
		
	public static char getChar(){
		System.out.println("Please Enter Only Char : ");
		String input=scanner.next();
		if(!ValidationUtil.isCharecter(input)){
			System.err.println("Wong Input  ");
			return getChar();
		}
		return input.charAt(0);
	}
	
	public static void main(String[] args) {
		
		consoleOutStream = System.out;
		consoleErrStream = System.err;
		log();
		try {
			taskClosed=false;
			listOfProcesses();
			System.out.println("\n\n**************************** Closing cmd.exe   ************************************");
			killProcesses();
			if(taskClosed==false)
				System.out.println("No cmd.exe found to be closed");
			System.out.println("** Finished @ "	+ new Date() + "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	public static  String  outPutPath(){
		String formatedDate="";
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("MM_dd_yyyy");
			formatedDate=	dateFormat.format(new Date());
			String usrDir=System.getProperty("user.dir");
			File log= new File(usrDir+"/log");
			if(!log.exists())
				log.mkdir();

			String logList[] =log.list();	
			for (int i = 0; i < logList.length; i++) {
				long totalTime = 7*24*60*60*1000;
				//			long totalTimeTest = 120*1000;
				File f=new File(log.getAbsolutePath()+"/"+logList[i]);
				//			System.out.println((System.currentTimeMillis())-f.lastModified());
				if(totalTime<=(System.currentTimeMillis()-(f.lastModified()))){
					f.delete();
				}

			}
			File logFile=new File(log,"log_"+formatedDate+".txt");
			return logFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatedDate+".txt";
	}

	public static void log(){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outPutPath(),true);
			outputLogStream = new PrintStream(fileOutputStream, true);
			consoleOutStream.println("Output has been redirected to file "+ outPutPath());
			System.setOut(outputLogStream);
			System.setErr(outputLogStream);
			System.out.println("\r\n***************************************************************");
			System.out.println("Started @ "	+ new Date());
			System.out.println("***************************************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static String killProcesses()
			throws IOException {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		List<String> command = new ArrayList<String>();
		command.add("taskkill");
		command.add("/f");
		command.add("/im");
		command.add("cmd.exe");
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			StringBuilder sw = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sw.append(line.trim());
				sw.append(System.getProperty("lineSeparator"));
				taskClosed=true;
			}
			return sw.toString();
		} finally {
			if (br != null)
				br.close();
			if (isr != null)
				isr.close();
			if (is != null)
				is.close();
		}
	}

	public static String listOfProcesses()
			throws IOException {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		List<String> command = new ArrayList<String>();

		//wmic process where \"name='cmd.exe'\"  get name,processid,sessionid > TestFile.txt
		command.add("wmic");
		command.add("process");
		command.add("where");
		command.add("name='cmd.exe'");
		command.add("get");
		command.add("name,");
		command.add("processid");
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			StringBuilder sw = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sw.append(line.trim());
				sw.append(System.getProperty("lineSeparator"));
			}
			return sw.toString();
		} finally {
			if (br != null)
				br.close();
			if (isr != null)
				isr.close();
			if (is != null)
				is.close();
		}
	}

}
