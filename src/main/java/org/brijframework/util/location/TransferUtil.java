package org.brijframework.util.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
/**
 * This utility class implements a method for copying a whole resorces
 * to a new location in the file system.
 * @author ram
 *
 */
public class TransferUtil {
	public static void moveFile(File moveFrom, File moveTo, boolean isDelete) {
        try {
            FileInputStream rd = new FileInputStream(moveFrom);
            FileOutputStream fo = new FileOutputStream(moveTo);
            int j;
            byte bytes[];
            while (true) {
                bytes = new byte[rd.available()];
                rd.read(bytes);
                fo.write(bytes);
                j = rd.read();
                if (j == -1) {
                    break;
                }
                fo.write(j);
            }
            rd.close();
            fo.close();
            if (isDelete) {
                moveFrom.delete();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	public static void moveFile(File moveFrom, File moveTo) {
		try {
			FileInputStream rd = new FileInputStream(moveFrom);
			FileOutputStream fo = new FileOutputStream(moveTo);
			int j;
			byte bytes[];
			while (true) {
				bytes = new byte[rd.available()];
				rd.read(bytes);
				fo.write(bytes);
				j = rd.read();
				if (j == -1) {
					break;
				}
				fo.write(j);
			}
			rd.close();
			fo.close();
			moveFrom.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Copy a file from a location to another
	 * @param sourceFile a File object represents the source file
	 * @param destFile a File object represents the destination file
	 * @throws IOException thrown if IO error occurred.
	 */
	@SuppressWarnings("resource")
	public void copyFileChannel(File sourceFile, File destFile)throws IOException {
		System.out.println("COPY FILE: " + sourceFile.getAbsolutePath()+ " TO: " + destFile.getAbsolutePath());
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel sourceChannel = null;
		FileChannel destChannel = null;

		try {
			sourceChannel = new FileInputStream(sourceFile).getChannel();
			destChannel = new FileOutputStream(destFile).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
		} finally {
			if (sourceChannel != null) {
				sourceChannel.close();
			}
			if (destChannel != null) {
				destChannel.close();
			}
		}
	}
	/**
	 * Copy a whole directory to another location.
	 * 
	 * @param sourceDir
	 *            a File object represents the source directory
	 * @param destDir
	 *            a File object represents the destination directory
	 * @throws IOException
	 *             thrown if IO error occurred.
	 */
	public void copyDirectory(File sourceDir, File destDir)
			throws IOException {
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		if (!sourceDir.exists()) {
			throw new IllegalArgumentException("sourceDir does not exist");
		}
		if (sourceDir.isFile() || destDir.isFile()) {
			throw new IllegalArgumentException(
					"Either sourceDir or destDir is not a directory");
		}

		File[] items = sourceDir.listFiles();
		if (items != null && items.length > 0) {
			for (File anItem : items) {
				if (anItem.isDirectory()) {

					File newDir = new File(destDir, anItem.getName());
					System.out.println("CREATED DIR: "
							+ newDir.getAbsolutePath());
					newDir.mkdir();

					copyDirectory(anItem, newDir);
				} else {
					File destFile = new File(destDir, anItem.getName());
					copyFile(anItem, destFile);
				}
			}
		}
	}
	
	


	/**this method split main file into small packet with given chunk size
	 * 
	 * @param mainFileName
	 * @param chunkSize
	 * @throws Exception 
	 * 
	 */

	public static void splitFileWithChunkSize(String mainFileName, int chunkSize)
			throws Exception {
		int offSetPosition = 0, length = 0;
		boolean flag = true;
		FileOutputStream fileOutputStream=null;
		FileInputStream fileInputStream = new FileInputStream(mainFileName);
		byte data[] = new byte[fileInputStream.available()];
		fileInputStream.read(data);
		while (flag) {
			fileOutputStream = new FileOutputStream("chunk" + offSetPosition+ ".txt");
			if ((offSetPosition + chunkSize) <= data.length) {
				length = chunkSize;
				fileOutputStream.write(data, offSetPosition, length);
				offSetPosition += chunkSize;
				System.out.println(offSetPosition);
				fileOutputStream.flush();
			} else {
				fileOutputStream.write(data, offSetPosition,
						(data.length - offSetPosition));
				fileOutputStream.flush();
				flag = false;
				break;

			}
		}
		fileOutputStream.close();
		fileInputStream.close();
	}
	

	public static boolean copyFile(File fromFile, File toFile) throws IOException {
		return moveFileNewWay(fromFile, toFile, false);
	}

	public static boolean moveFileNewWay(File fromFile, File toFile, boolean isDelete) throws IOException {
		if (!fromFile.exists()) {
			return false;
		}
		if (toFile.exists()) {
			boolean isOldDestFileDeletedSuccessfully = toFile.delete();
			if (!isOldDestFileDeletedSuccessfully) {
				return false;
			}
		}
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream(fromFile);
			fileOutputStream = new FileOutputStream(toFile);
			byte[] bytes = new byte[15000];
			for (int numberOfBytesRead = fileInputStream.read(bytes); numberOfBytesRead != -1; numberOfBytesRead = fileInputStream.read(bytes)) {
				fileOutputStream.write(bytes, 0, numberOfBytesRead);
			}
			fileOutputStream.flush();
			fileInputStream.close();
			if (isDelete) {
				return fromFile.delete();
			} else {
				return true;
			}
		} finally {
			try {
				fileInputStream.close();
			} catch (Exception ignored) {
			}
			try {
				fileOutputStream.close();
			} catch (Exception ignored) {
			}
		}
	}

	
}
