package org.brijframework.util.location;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ZipUtil {
	public static byte[] getBytesAftrZippingFolder(File inFolder) {
		if (inFolder == null || !inFolder.exists()) {
			System.out.println("Please specify the correct FOLDER path for zip");
			return null;
		}

		String rootFolderName = inFolder.getName();

		int BUFFER = 2048;
		BufferedInputStream in = null;
		ZipOutputStream out = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			out = new ZipOutputStream(new BufferedOutputStream(baos));
			byte[] data = new byte[BUFFER];
			Stack<File> folderStack = new Stack<>();
			String startFolder = inFolder.getAbsolutePath();

			if (inFolder.isDirectory()) {
				ZipEntry folderZipEntry = new ZipEntry(rootFolderName + "/");
				folderZipEntry.setExtra("Directory".getBytes());
				out.putNextEntry(folderZipEntry);
			}

			try {
				do {
					String parenFolderPath = "";
					try {
						parenFolderPath = inFolder.getAbsolutePath().substring(startFolder.length() + 1);
					} catch (Exception e) {

					}
					//String files[] = inFolder.list();
					String[] files = null;
					if (inFolder.isDirectory()) {
						files = inFolder.list();
					} else {
						files = new String[1];
						files[0] = inFolder.getName();
						inFolder = new File(startFolder.substring(0, (startFolder.length() - files[0].length())));
						parenFolderPath = null;
						rootFolderName = null;
					}

					for (int i = 0; i < files.length; i++) {
						if (new File(inFolder, files[i]).isDirectory()) {
							folderStack.push(new File(inFolder, files[i]));
							ZipEntry folderZipEntry = null;
							if (parenFolderPath != null && parenFolderPath.trim().length() > 0) {
								folderZipEntry = new ZipEntry(rootFolderName + "/" + parenFolderPath + "/" + files[i] + "/");
							} else {
								folderZipEntry = new ZipEntry(rootFolderName + "/" + files[i] + "/");
							}
							folderZipEntry.setExtra("Directory".getBytes());
							out.putNextEntry(folderZipEntry);
							continue;
						}
						in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "/" + files[i]), BUFFER);
						ZipEntry fileZipEntry = null;
						if (rootFolderName == null) {
							fileZipEntry = new ZipEntry(files[i]);
						} else {
							if (parenFolderPath != null && parenFolderPath.trim().length() > 0) {
								fileZipEntry = new ZipEntry(rootFolderName + "/" + parenFolderPath + "/" + files[i]);
								fileZipEntry.setExtra((rootFolderName + "/" + parenFolderPath).getBytes());
							} else {
								{
									fileZipEntry = new ZipEntry(rootFolderName + "/" + files[i]);
									fileZipEntry.setExtra((rootFolderName).getBytes());
								}
							}
						}
						out.putNextEntry(fileZipEntry);
						int count;
						while ((count = in.read(data, 0, BUFFER)) != -1) {
							out.write(data, 0, count);
						}
						out.closeEntry();
					}
				} while ((inFolder = (File) folderStack.pop()) != null);
			} catch (EmptyStackException e) {
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception ignored) {
			}
			try {
				out.close();
			} catch (Exception ignored) {
			}
		}
		return baos.toByteArray();
	}

	public static void createZipFileFromByteArrayToLocalDisk(byte[] zippedByteData, File outputDir, String zipFileName) {

		if (zippedByteData == null) {
			zippedByteData = new byte[0];
		}
		try {
			if (outputDir == null) {
				outputDir = getCurrentDir();//new File(System.getProperty("user.dir"));
			} else {
				try {
					// if(!outputDir.getAbsolutePath().contains(":/") && !outputDir.getAbsolutePath().contains(":\\") ){
					if (outputDir.getAbsolutePath().indexOf(":/") == -1 && outputDir.getAbsolutePath().indexOf(":\\") == -1) {
						//outputDir = new File(new File(System.getProperty("user.dir")),outputDir.getAbsolutePath());
						outputDir = new File(getCurrentDir(), outputDir.getAbsolutePath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			outputDir = getCurrentDir();//new File(System.getProperty("user.dir"));
		}
		try {
			if (!outputDir.exists() || !outputDir.isDirectory()) {
				outputDir.mkdirs();
			}
			//if(outputDir.)
		} catch (Exception e) {
			System.out.println("Writing Bytes Into Current Directory");
			outputDir = getCurrentDir();//new File(System.getProperty("user.dir"));
		}
		try {
			BufferedOutputStream outPutDest = null;
			FileOutputStream fos = new FileOutputStream(new File(outputDir.getAbsolutePath(), zipFileName));
			outPutDest = new BufferedOutputStream(fos);

			outPutDest.write(zippedByteData);
			outPutDest.flush();
			outPutDest.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//----------------------------------------unzipping-------------------------//
	public static void unZipFolder(String zipFile, String outZipFileName) {
		File inFolder = new File(zipFile);
		if (!inFolder.exists()) {
			inFolder = new File(new File(System.getProperty("user.dir")), zipFile);
		}
		File outFile = new File(outZipFileName);
		byte[] data = getByteArrayFromZipFile(inFolder);
		createFileStrFromByteArrayToLocalDisk(data, outFile);
		// unZipFolder(inFolder,outFile);
	}

	public static byte[] getByteArrayFromZipFile(File zipFileName) {

		int BUFFER = 2048;
		BufferedInputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] data = new byte[BUFFER];
			try {
				in = new BufferedInputStream(new FileInputStream(zipFileName), BUFFER);
				int count;
				while ((count = in.read(data, 0, BUFFER)) != -1) {
					baos.write(data, 0, count);
				}
			} catch (EmptyStackException e) {
			}
			baos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception ignored) {
			}
			try {
				baos.close();
			} catch (Exception ignored) {
			}
		}
		return baos.toByteArray();

	}

	public static void createFileStrFromByteArrayToLocalDisk(byte[] zippedByteData, File outputDir) {
		if (zippedByteData == null) {
			zippedByteData = new byte[0];
		}
		try {
			if (outputDir == null) {
				outputDir = new File(System.getProperty("user.dir"));
			} else {
				try {
					//if (!outputDir.getAbsolutePath().contains(":/")	&& !outputDir.getAbsolutePath().contains(":\\")) {
					if (outputDir.getAbsolutePath().indexOf(":/") == -1 && outputDir.getAbsolutePath().indexOf(":\\") == -1) {
						outputDir = new File(new File(System.getProperty("user.dir")), outputDir.getAbsolutePath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			outputDir = new File(System.getProperty("user.dir"));
		}
		try {
			if (!outputDir.exists() || !outputDir.isDirectory()) {
				outputDir.mkdirs();
			}
		} catch (Exception e) {
			outputDir = new File(System.getProperty("user.dir"));
		}
		try {
			int BUFFER = 2048;
			BufferedOutputStream outPutDest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(zippedByteData)));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String extraInfo = "file";
				if (entry.getExtra() != null) {
					extraInfo = new String(entry.getExtra());
				}
				if (extraInfo.equals("Directory")) {
					File dir = new File(outputDir.getAbsolutePath(), entry.getName());//.substring(0,	index));
					dir.mkdirs();
					//System.out.println(outputDir.getAbsolutePath()+"=Dir="+dir.getAbsolutePath()+"---"+dir.exists()+"---"+entry.getName());
				}
				/*if (entry.getName().endsWith("\\.") || entry.getName().endsWith("/.")) {
					int index = entry.getName().indexOf("\\.");
					if (index == -1) {
						index = entry.getName().indexOf("/.");
					}
					File dir = new File(outputDir, entry.getName().substring(0,	index));
					dir.mkdir();
				} */else {
					try {
						int count;
						byte data[] = new byte[BUFFER];
						FileOutputStream fos = null;

						int indx = entry.getName().lastIndexOf('/');
						if (indx == -1) {
							indx = entry.getName().lastIndexOf('\\');
						}
						String fileName = entry.getName().substring(indx + 1);

						if (entry.getExtra() == null) {
							if (!fileName.equals("UnzipFileFolder.exe.zip")) {
								if (entry.isDirectory()) {
									File dir = new File(outputDir.getAbsolutePath(), entry.getName());//.substring(0,	index));
									dir.mkdirs();
								} else {
									fos = new FileOutputStream(new File(outputDir.getAbsolutePath(), entry.getName()));//fileName));// entry.getName()));
								}
							}
						} else {
							if (entry.isDirectory()) {
								File dir = new File(outputDir.getAbsolutePath(), entry.getName());//.substring(0,	index));
								dir.mkdirs();
							} else {
								fos = new FileOutputStream(new File(outputDir.getAbsolutePath(), entry.getName()));//new String(entry.getExtra())),fileName));// entry.getName()));
							}
						}

						outPutDest = new BufferedOutputStream(fos, BUFFER);
						while ((count = zis.read(data, 0, BUFFER)) != -1) {
							outPutDest.write(data, 0, count);
						}
						outPutDest.flush();
						outPutDest.close();
					} catch (Exception e) {
					}
				}
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<File> getFiles(File file) {
		List<File> files = new ArrayList<>();
		try {

			InputStream input = new FileInputStream(file);
			ZipInputStream zip = new ZipInputStream(input);
			ZipEntry entry = zip.getNextEntry();
			while (true) {
				if (entry == null) {
					break;
				}
				System.out.println(entry);
				files.add(file);
			}
			zip.close();
			input.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * @param args
	 */
	
	private static File currentDir = null;
	public static File getCurrentDir() {
		if(currentDir == null){
			try {
				Process p = Runtime.getRuntime().exec("cmd /c cd");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String tempS = null;
		           while ((tempS = stdInput.readLine()) != null) {
		               tempS = tempS.replace('\\', '/');
		               currentDir = new File(tempS);
		               //System.out.println(tempS);
		           }
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			if(!currentDir.exists())
				currentDir = new File(System.getProperty("user.dir"));
		}
		return currentDir;
	}


	public static void setCurrentDir(File currentDir) {
		ZipUtil.currentDir = currentDir;
	}
	
	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Compresses a list of files to a destination zip file
	 * 
	 * @param listFiles
	 *            A collection of files and directories
	 * @param destZipFile
	 *            The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(List<File> listFiles, String destZipFile) throws FileNotFoundException,
			IOException {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));

		for (File file : listFiles) {
			if (file.isDirectory()) {
				this.zipDirectory(file, file.getName(), zos);
			} else {
				this.zipFile(file, zos);
			}
		}

		zos.flush();
		zos.close();
	}

	/**
	 * Compresses files represented in an array of paths
	 * 
	 * @param files
	 *            a String array containing file paths
	 * @param destZipFile
	 *            The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
		List<File> listFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			listFiles.add(new File(files[i]));
		}

		this.zip(listFiles, destZipFile);
	}

	/**
	 * Adds a directory to the current zip output stream
	 * 
	 * @param folder
	 *            the directory to be added
	 * @param parentFolder
	 *            the path of parent directory
	 * @param zos
	 *            the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipDirectory(File folder, String parentFolder,
			ZipOutputStream zos) throws FileNotFoundException, IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				this.zipDirectory(file, parentFolder + "/" + file.getName(), zos);
				continue;
			}

			zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

			@SuppressWarnings("resource")
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			@SuppressWarnings("unused")
			long bytesRead = 0;
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;

			while ((read = bis.read(bytesIn)) != -1) {
				zos.write(bytesIn, 0, read);
				bytesRead += read;
			}

			zos.closeEntry();

		}
	}

	/**
	 * Adds a file to the current zip output stream
	 * 
	 * @param file
	 *            the file to be added
	 * @param zos
	 *            the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void zipFile(File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {
		zos.putNextEntry(new ZipEntry(file.getName()));

		@SuppressWarnings("resource")
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

		long bytesRead = 0;
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;

		while ((read = bis.read(bytesIn)) != -1) {
			zos.write(bytesIn, 0, read);
			bytesRead += read;
		}

		zos.closeEntry();
	}

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified
	 * by destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static String fileUnCompress(String formReadFileName)
			throws Exception {
		FileInputStream fin = new FileInputStream(formReadFileName);
		InflaterInputStream in = new InflaterInputStream(fin);
		int i;
		String data = "";
		while ((i = in.read()) != -1) {
			data += (char) i;
		}
		fin.close();
		in.close();
		return data;
	}

	public static boolean fileCompress(String formReadFileName, String toWriteFileName) throws IOException {
		FileInputStream fin = new FileInputStream(formReadFileName);
		FileOutputStream fout = new FileOutputStream(toWriteFileName);
		DeflaterOutputStream out = new DeflaterOutputStream(fout);
		int i;
		while ((i = fin.read()) != -1) {
			out.write((byte) i);
			out.flush();
		}
		fin.close();
		out.close();
		return true;
	}

	public static String loadCompressFile(File formReadFileName,File toWriteFileName) throws Exception {
		FileInputStream fin = new FileInputStream(formReadFileName);
		InflaterInputStream in = new InflaterInputStream(fin);
		FileOutputStream fout = new FileOutputStream(toWriteFileName);
		int i;
		String data="";
		while ((i = in.read()) != -1) {
			fout.write((byte) i);
			fout.flush();
			data=""+(char)i;
		}
		fin.close();
		fout.close();
		in.close();
		return data;
	}

	public static String loadCompressFile(String formReadFileName) throws Exception {
		FileInputStream fin = new FileInputStream(formReadFileName);
		InflaterInputStream in = new InflaterInputStream(fin);
		int i;
		String data = "";
		while ((i = in.read()) != -1) {
			data += (char) i;
		}
		fin.close();
		in.close();
		return data;
	}

	public static ArrayList<?> loadFilesFromJarOrDirectly(String dirPath, URL dirUrl) {
		ArrayList<Object> inputStreamArray = new ArrayList<>();
		try {
			URLConnection urlConnection = dirUrl.openConnection();
			if (urlConnection instanceof JarURLConnection) { // if the file is inside jar
				JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
				JarFile jarFile = jarURLConnection.getJarFile();
				Enumeration<JarEntry> enm = jarFile.entries();
				String alternateDirPath = dirPath;
				if (dirPath.length() > 1 && dirPath.startsWith("/")) {
					alternateDirPath = dirPath.substring(1);
				}
				while (enm.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) enm.nextElement();

					if (!jarEntry.isDirectory() && (jarEntry.getName().startsWith(dirPath) || jarEntry.getName().startsWith(alternateDirPath))) {
						System.out.println(jarFile.getName());
						inputStreamArray.add(jarFile.getInputStream(jarEntry));
					}
				}
			} else { // if the file can be accessed directly
				URI uri = null;
				try {
					uri = new URI(dirUrl.toString());
				} catch (URISyntaxException e) {
					return inputStreamArray;
				}
				File dirOrFile = new File(uri);
				if (dirOrFile.isDirectory()) {
					File[] files = dirOrFile.listFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							FileInputStream fileInputStream = new FileInputStream(files[i]);
							inputStreamArray.add(fileInputStream);
						}
					}
				} else {
					FileInputStream fileInputStream = new FileInputStream(dirOrFile);
					inputStreamArray.add(fileInputStream);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could Not load directory from directory " + dirPath, e);
		}
		return inputStreamArray;
	}

	public static void main(String[] args) throws IOException {
		byte[] zippedByteData=Base64.decodeBase64(new String(Files.readAllBytes(new File("D:/fileBytes.txt").toPath())));
		System.out.println(zippedByteData.length);
		createFileStrFromByteArrayToLocalDisk(zippedByteData, null);
	}
}
