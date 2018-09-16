package org.brijframework.util.resouces;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.brijframework.util.casting.DateUtil;
import org.brijframework.util.formatter.FormatUtil;
import org.brijframework.util.location.DirUtil;
import org.brijframework.util.location.StreamUtil;
import org.brijframework.util.objects.ObjectUtil;

public class FileUtil {
	
	/**
	 * 
	 * @param FileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOExceptionuse
	 *             use to load Properties File and Return Properties
	 *             {@link Object}
	 */

	public static Properties loadPropetiesFile(String FileName) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(FileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static Properties loadPropertiesFile(String FileName, Properties properties){
		try {
			properties.load(new FileInputStream(FileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void writeObject(File file, Object object) throws IOException {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} finally {
			try {
				objectOutputStream.close();
			} catch (Exception ignored) {
			}
			try {
				fileOutputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Converts File to Object. Returns null if exception occurs
	 * 
	 * @param file
	 *            {@link File} object which need to be change to {@link Object}
	 * @return object converted {@link File} object
	 */
	public static Object readObject(File file) {
		FileInputStream fis = null;
		ObjectInputStream oos = null;
		Object object = null;
		try {
			fis = new FileInputStream(file);
			oos = new ObjectInputStream(fis);
			object = oos.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (Exception ignore) {
			}
			try {
				fis.close();
			} catch (Exception ignore) {
			}
		}
		return object;
	}

	/**
	 * Converts bytes array to Object. Returns null if exception occurs
	 * 
	 * @param bytes
	 *            {@link Byte} array
	 * @return object converted by byte array.
	 */
	public static Object readObject(byte[] bytes) {
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
			ObjectInputStream p = new ObjectInputStream(bufferedInputStream);
			Object hash = p.readObject();
			p.close();
			bufferedInputStream.close();
			return hash;
		} catch (Exception e) {
			return null;
		}
	}

	public static void saveToFile(String filePath, Object obj) throws IOException {
		DirUtil.createFile(filePath);
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		outputStream.writeObject(obj);
		outputStream.close();
	}

	@SuppressWarnings("resource")
	public static Object loadFromFile(String filePath) throws IOException, ClassNotFoundException {
		DirUtil.createFile(filePath);
		if (loadByteFile(new File(filePath)).length == 0) {
			return null;
		}
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream inputStream = new ObjectInputStream(fis);
		return inputStream.readObject();
	}

	public static void saveToStream(Object obj) throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(System.out);
		outputStream.writeObject(obj);
		outputStream.close();
	}

	public static Object loadFromStream(Object obj) throws IOException, ClassNotFoundException {
		ObjectInputStream inputStream = new ObjectInputStream(System.in);
		return inputStream.readObject();
	}

	public static void saveToNetwork(Object obj) throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(System.out);
		outputStream.writeObject(obj);
		outputStream.close();
	}

	public static Object loadFromNetwork(Object obj) throws IOException, ClassNotFoundException {
		ObjectInputStream inputStream = new ObjectInputStream(System.in);
		return inputStream.readObject();
	}

	public static byte[] getObjectBytes(Object object) throws IOException {
		ByteArrayOutputStream byteStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			byte[] byteArray = byteStream.toByteArray();
			return byteArray;
		} finally {
			try {
				objectOutputStream.close();
			} catch (Exception ignored) {
			}
			try {
				byteStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static Collection<?> getLinesForFile(File file) {
		return ObjectUtil.getLinesForString(new String(loadFile(file)));
	}
	
	public static String writeCSVFile(String filePath, String FileName,StringBuffer data)
			throws IOException {
		if (data.toString() == null) {
			return "Data cant be blank";
		}
		if (FileName == null) {
			return "file Name can not be empty or null";
		}
		File file = new File(FileName);
		if (file.length() > 1) {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			fileOutputStream.write((data.toString()).getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();

		} else {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write((data.toString()).getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		return "1";
	}

	public static  String writeCSVFile(String FileName,String data)
			throws IOException {
		if (data.toString() == null) {
			return "Data cant be blank";
		}
		if (FileName == null) {
			return "file Name can not be empty or null";
		}
		FileOutputStream fileOutputStream = new FileOutputStream(FileName);
		fileOutputStream.write((data.toString()).getBytes());
		fileOutputStream.flush();
		fileOutputStream.close();
		return "1";
	}
   
	public static void writeDOCFile(String filePath,String fileName){
    	
    }
    public static void writeJSONFile(String filePath,String fileName){
    	
    }
    public static void writeXMLFile(String filePath,String fileName){
    	
    }
    public static String writePropertiesFile(String filePath,String fileName,String data)throws IOException {
		String createFileName = null ;
		if (fileName.endsWith(".properties")) {
			File file = new File(fileName);
			if (file.exists()) {
				createFileName = "File Allready exists";
			} else {
				file.createNewFile();
				createFileName = fileName;
			}
		}
		return createFileName;
	}
	
    public static void writeClassFile(String filePath,String fileName){
    	
    }
    public static void writeJARFile(String filePath,String fileName){
    	
    }
    public static void writeZIPFile(String filePath,String fileName) {
    	
    }
   
    public static byte[] writeImageFile(String dirPath,String type ,String detail) {
    	byte[] imgByte=detail.getBytes();
    	try {
			BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imgByte));
			File imageFIle=DirUtil.getVerifiedDir(dirPath);
			ImageIO.write(imag, type, imageFIle);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return imgByte;
    }
    public static void writeVedioFile(String filePath,String fileName) {
    	
    }
    public static void writeAedioFile(String filePath,String fileName) {
    	
    }
    
    public static boolean writeByteFile(File fileName, byte[] data) {
		try {
			FileOutputStream fi = new FileOutputStream(fileName);
			fi.write(data);
			fi.flush();
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	} 
	
	public static boolean writeByteFile(String fileName, String data,boolean appand) throws IOException {
		FileOutputStream fout = new FileOutputStream(fileName, appand);
		byte b[] = data.getBytes();// converting string into byte array
		fout.write(b);
		fout.close();
		return true;
	}
	
	public static boolean writeByteFileInBufferMode(String fileName, String data)throws Exception {
		FileOutputStream fout = new FileOutputStream(fileName);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		byte b[] = data.getBytes();
		bout.write(b);
		bout.flush();
		bout.close();
		fout.close();
		return true;
	}

	public static boolean writeByteFileInBufferMode(String fileName,String data, boolean appand) throws Exception {
		FileOutputStream fout = new FileOutputStream(fileName, appand);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		byte b[] = data.getBytes();
		bout.write(b);
		bout.flush();
		bout.close();
		fout.close();
		return true;
	}

	public static boolean writeCharFile(String fileName, String data)
			throws IOException {
		FileWriter fw = new FileWriter(fileName);
		fw.write(data);
		fw.close();
		return true;
	}

	public static boolean writeCharFile(String fileName, String data,
			boolean appand) throws IOException {
		FileWriter fw = new FileWriter(fileName, appand);
		fw.write(data);
		fw.close();
		return true;
	}

	public static boolean writeCharInMultipleFle(String fileName[],
		String data, boolean appand) throws Exception {
		CharArrayWriter out = new CharArrayWriter();
		out.write(data);
		for (String file : fileName) {
			FileWriter fw = new FileWriter(file, appand);
			out.writeTo(fw);
			fw.close();
		}
		return true;
	}
	
	public static void LineNumberReader(String filePath) {
		try {
			LineNumberReader lineReader = new LineNumberReader(new FileReader(filePath));
			String lineText = null;

			while ((lineText = lineReader.readLine()) != null) {
				System.out.println(lineReader.getLineNumber() + ": " + lineText);
			}
			lineReader.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public static byte[] loadFile(File file) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			FileInputStream fi = new FileInputStream(file);
			int i;
			while ((i = fi.read()) != -1) {
				byteStream.write(i);
			}
			fi.close();
			byteStream.flush();
			bytes = byteStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}	
	public static String loadFile(String dir,String fileName) throws IOException{
		File file=DirUtil.getFileExists(dir, fileName);
		return new String(loadByteFileInBufferMode(file));
	}
	
	public static String loadFile(String path){
		File file=DirUtil.getPath(path);
		return new String(loadFile(file));
	}
    /**
	 * 
	 * @param data
	 * @param FileName
	 * @return
	 * @throws IOException
	 *             this method will write data into csv file.
	 */

	public static String loadCSVFile(String FileName) throws IOException {
		FileReader fr = new FileReader(FileName);
		BufferedReader bin = new BufferedReader(fr);
		String data = "";
		String i;
		while ((i = bin.readLine()) != null)
			data += i+"\n";
		fr.close();
		return data;
	}
    public static byte[] loadByteFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] value = new byte[fis.available()];
		fis.read(value);
		fis.close();
		return value;
	}
	
	public static byte[] loadByteFile(String dir,String fileName) throws IOException{
		File file=DirUtil.getFileExists(dir, fileName);
		return loadByteFileInBufferMode(file);
	}
	
    public static byte[] loadByteFileInBufferMode(File file)throws IOException {
		FileInputStream fin=new FileInputStream(file);
		return StreamUtil.loadInBufferModeFromStream(fin);
	}
	
	public static byte[] loadByteArrayFile(File file) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			FileInputStream fi = new FileInputStream(file);
			int i;
			while ((i = fi.read()) != -1) {
				byteStream.write(i);
			}
			fi.close();
			byteStream.flush();
			bytes = byteStream.toByteArray();
			byteStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	public static String loadFile(InputStream stream) {
		String resurces="";
		 BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
         String line = "";
         try {
 			while ((line = reader.readLine()) != null) {
 				resurces+=line+"\n";
 			}
 		} catch (IOException e) {
 			System.err.println(e);
 		}
		return resurces;
	}
	
	public static String loadCharFile(File file)throws Exception {
		FileReader fileReader = new FileReader(file);
		String data = "";
		int i;
		while ((i = fileReader.read()) != -1)
			data += ((char) i);
		fileReader.close();
		return data;
	}
	
	public static String loadMultipleFileInSequence(File... files)throws IOException {
		int len = 0;
		Vector<FileInputStream> v = new Vector<FileInputStream>();
		for (File file : files)
			v.add(new FileInputStream(file));
		Enumeration<FileInputStream> e = v.elements();
		while (e.hasMoreElements())
			len += e.nextElement().available();
		Enumeration<FileInputStream> e1 = v.elements();
		SequenceInputStream bin = new SequenceInputStream(e1);
		int pos = 0;
		int i = 0;
		char[] data = new char[len];
		while ((i = bin.read()) != -1) {
			data[pos++] = (char) i;
		}
		bin.close();
		while (e.hasMoreElements())
			e.nextElement().close();
		return new String(data);
	}
	
	public static void deleteAllFile(File file, int offSet) {
		Calendar previousDate = DateUtil.currentDate(offSet);
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				File delfile = new File(file, list[i]);
				Calendar fileMod = Calendar.getInstance();
				fileMod.setTime(new Date(delfile.lastModified()));
				if (fileMod.before(previousDate)) {
					delfile.delete();
				}
			} catch (Exception e) {
				System.out.println("Unable to delete Log Files  \r\n" + list[i]);
			}
		}
	}

	public static void deleteAllFile(File file) {
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				File delfile = new File(file, list[i]);
				delfile.delete();
			} catch (Exception e) {
				System.out.println("Unable to delete Log Files  \r\n" + list[i]);
			}
		}
	}
	

	public static void deleteOldFiles(File directory, int offsetHours) {
		if (!directory.exists()) {
			return;
		}
		Calendar timeOffsetHoursBack = Calendar.getInstance();
		timeOffsetHoursBack.add(Calendar.HOUR, -1 * offsetHours);
		long timeOfOldestFileToBeKept = timeOffsetHoursBack.getTimeInMillis();
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				if (files[i].isFile() && files[i].lastModified() < timeOfOldestFileToBeKept) {
					if (!files[i].delete()) {
						System.out.println("Could not delete " + files[i].getAbsolutePath() + ", the file could be in read-only mode or some other process could be using it");
					}
				}
			} catch (Exception e) {
				System.out.println("Unable to delete file " + files[i].getName() + " of directory " + directory.getName());
			}
		}
	}
	

	public static Object readObjectNewWay(File file) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);
			return objectInputStream.readObject();
		} finally {
			try {
				objectInputStream.close();
			} catch (Exception ignored) {
			}
			try {
				fileInputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static Object readObjectInLinuxWay(File file) throws IOException, ClassNotFoundException {
		BufferedReader bufferedReader = null;
		ObjectInputStream objectInputStream = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			int ch = -1;
			StringBuffer sBuffer = new StringBuffer();
			while ((ch = bufferedReader.read()) != -1) {
				sBuffer.append((char) ch);
			}
			byte[] dataByteArray = FormatUtil.decodeUnzipBuffer(sBuffer.toString());
			objectInputStream = new ObjectInputStream(new ByteArrayInputStream(dataByteArray));
			return objectInputStream.readObject();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
			try {
				objectInputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static Object getObjectFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return objectInputStream.readObject();
		} finally {
			try {
				objectInputStream.close();
			} catch (Exception ignored) {
			}
			try {
				byteArrayInputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static byte[] objectToBytes(Object object) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream p = new ObjectOutputStream(byteStream);
			p.writeObject(object);
			p.flush();
			byte[] array = byteStream.toByteArray();
			p.close();
			byteStream.close();
			return array;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeObjectNewWay(File file, Object object) throws IOException {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} finally {
			try {
				objectOutputStream.close();
			} catch (Exception ignored) {
			}
			try {
				fileOutputStream.close();
			} catch (Exception ignored) {
			}
		}
	}



	public static void writeFile(File file, byte[] data) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(File fileName, String data) {
		try {
			FileOutputStream fi = new FileOutputStream(fileName);
			fi.write(data.getBytes());
			fi.flush();
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFileNewWay(File file, byte[] data) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static void writeFileNewWay(File file, String data) throws IOException {
		writeFileNewWay(file, data.getBytes());
	}

	public static String readFile(File file) throws IOException {
		StringBuffer strBuf = new StringBuffer();
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				strBuf.append(line + "\r\n");
			}
			return strBuf.toString();
		} finally {
			try {
				fileReader.close();
			} catch (Exception ignored) {
			}
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
		}
	}
	public static StringBuffer getStringBuffer(InputStream inputStream) throws IOException {
		StringBuffer strBuf = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				strBuf.append(line + "\r\n");
			}
			return strBuf;
		} finally {
			try {
				reader.close();
			} catch (Exception ignored) {
			}
			try {
				inputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] getStringLines(String str) throws IOException {
		StringReader stringReader = null;
		BufferedReader bufferedReader = null;
		ArrayList linesArray = new ArrayList();
		try {
			stringReader = new StringReader(str);
			bufferedReader = new BufferedReader(stringReader);
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				linesArray.add(line);
			}
			String[] lines = new String[linesArray.size()];
			Iterator itr = linesArray.iterator();
			for (int i = 0; itr.hasNext(); i++) {
				lines[i] = (String) itr.next();
			}
			return lines;
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
			try {
				stringReader.close();
			} catch (Exception ignored) {
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] getLines(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		ArrayList linesArray = new ArrayList();
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				linesArray.add(line);
			}
			String[] lines = new String[linesArray.size()];
			Iterator itr = linesArray.iterator();
			for (int i = 0; itr.hasNext(); i++) {
				lines[i] = (String) itr.next();
			}
			return lines;
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
			try {
				inputStreamReader.close();
			} catch (Exception ignored) {
			}
			try {
				inputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] getFileLines(File file) throws IOException {
		ArrayList linesArray = new ArrayList();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				linesArray.add(line);
			}
		} finally {
			try {
				fileReader.close();
			} catch (Exception ignored) {
			}
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
		}
		String[] lines = new String[linesArray.size()];
		Iterator itr = linesArray.iterator();
		for (int i = 0; itr.hasNext(); i++) {
			lines[i] = (String) itr.next();
		}
		return lines;
	}
	public static byte[] getFileBytes(File file) throws IOException {
		byte[] byteArray = null;
		FileInputStream fileInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[15000];
			for (int numberOfBytesRead = fileInputStream.read(bytes); numberOfBytesRead != -1; numberOfBytesRead = fileInputStream.read(bytes)) {
				byteArrayOutputStream.write(bytes, 0, numberOfBytesRead);
			}
			byteArrayOutputStream.flush();
			byteArray = byteArrayOutputStream.toByteArray();
		} finally {
			try {
				fileInputStream.close();
			} catch (Exception ignored) {
			}
			try {
				byteArrayOutputStream.close();
			} catch (Exception ignored) {
			}
		}
		return byteArray;
	}

	public static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[15000];
			for (int numberOfBytesRead = inputStream.read(bytes); numberOfBytesRead != -1; numberOfBytesRead = inputStream.read(bytes)) {
				byteArrayOutputStream.write(bytes, 0, numberOfBytesRead);
			}
			byteArrayOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (Exception ignored) {
			}
		}
	}


	public static Properties getProperties(File file, boolean isMandatory) {
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			if (!file.exists()) {
				System.out.println("Could Not find properties file at " + file.getAbsolutePath());
				if (isMandatory) {
					throw new RuntimeException("Could Not find properties file at " + file.getAbsolutePath());
				} else {
					return null;
				}
			}
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
			return properties;
		} catch (IOException e) {
			System.out.println("Exception in loading properties file");
			e.printStackTrace();
			if (isMandatory) {
				throw new RuntimeException("Exception in loading properties file ", e);
			} else {
				return null;
			}
		} finally {
			try {
				fileInputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

}
