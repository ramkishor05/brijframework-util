package org.brijframework.util.location;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.resouces.FilterUtil;

public class StreamUtil {
	public static final int BUFFER_SIZE = 4096;

	public static String getResource(String resourse) {
		String resurces = "";
		InputStream inStream = getResourceAsStream(resourse);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				resurces += line + "\n";
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		return resurces;
	}

	public static File[] getAllResurces(final String path, String extension) {
		URL url = PathUtil.findAsResource(path);
		File uri = null;
		try {
			uri = new File(url.toURI());
		} catch (URISyntaxException e) {
			return null;
		}
		return FilterUtil.dirFilesWithSubDir(uri.getPath(), extension);
	}

	public static InputStream getConfigStream(final String path) {
		final URL url = PathUtil.locateURLConfig(path);

		if (url == null) {
			return null;
		}

		try {
			return url.openStream();
		} catch (IOException e) {
			return null;
		}
	}

	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
		InputStream stream = null;
		ClassLoader classLoader = PathUtil.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			stream = org.omg.CORBA.Environment.class.getResourceAsStream(resource);
		}
		if (stream == null) {
			stream = org.omg.CORBA.Environment.class.getClassLoader().getResourceAsStream(stripped);
		}
		return stream;
	}

	public static InputStream getUserResourceAsStream(String resource) {
		boolean hasLeadingSlash = resource.startsWith("/");
		String stripped = hasLeadingSlash ? resource.substring(1) : resource;

		InputStream stream = null;

		ClassLoader classLoader = PathUtil.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(resource);
			if (stream == null && hasLeadingSlash) {
				stream = classLoader.getResourceAsStream(stripped);
			}
		}

		if (stream == null) {
			stream = org.omg.CORBA.Environment.class.getClassLoader().getResourceAsStream(resource);
		}
		if (stream == null && hasLeadingSlash) {
			stream = org.omg.CORBA.Environment.class.getClassLoader().getResourceAsStream(stripped);
		}
		return stream;
	}

	public static String loadFromStream(InputStream io) {
		try {
			int i;
			StringBuffer buffer = new StringBuffer();
			while ((i = io.read()) != -1) {
				buffer.append((char) i);
			}
			io.close();
			return buffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] loadInBufferModeFromStream(InputStream inputStream) throws IOException {
		BufferedInputStream bin = new BufferedInputStream(inputStream);
		int pos = 0;
		int i = 0;
		byte[] data = new byte[bin.available()];
		while ((i = bin.read()) != -1) {
			data[pos++] = (byte) i;
		}
		bin.close();
		return data;
	}

	/**
	 * Copy the contents of the given InputStream into a new byte array. Leaves
	 * the stream open when done.
	 * 
	 * @param in
	 *            the stream to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}

	/**
	 * Copy the contents of the given InputStream into a String. Leaves the
	 * stream open when done.
	 * 
	 * @param in
	 *            the InputStream to copy from
	 * @return the String that has been copied to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		Assertion.notNull(in, "No InputStream specified");
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, bytesRead);
		}
		return out.toString();
	}

	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Leaves the stream open when done.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		Assertion.notNull(in, "No input byte array specified");
		Assertion.notNull(out, "No OutputStream specified");
		out.write(in);
	}

	/**
	 * Copy the contents of the given String to the given output OutputStream.
	 * Leaves the stream open when done.
	 * 
	 * @param in
	 *            the String to copy from
	 * @param charset
	 *            the Charset
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(String in, Charset charset, OutputStream out) throws IOException {
		Assertion.notNull(in, "No input String specified");
		Assertion.notNull(charset, "No charset specified");
		Assertion.notNull(out, "No OutputStream specified");
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}

	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Leaves both streams open when done.
	 * 
	 * @param in
	 *            the InputStream to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		Assertion.notNull(in, "No InputStream specified");
		Assertion.notNull(out, "No OutputStream specified");
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}

	/**
	 * Returns a variant of the given {@link InputStream} where calling
	 * {@link InputStream#close() close()} has no effect.
	 * 
	 * @param in
	 *            the InputStream to decorate
	 * @return a version of the InputStream that ignores calls to close
	 */
	public static InputStream nonClosing(InputStream in) {
		Assertion.notNull(in, "No InputStream specified");
		return new NonClosingInputStream(in);
	}

	/**
	 * Returns a variant of the given {@link OutputStream} where calling
	 * {@link OutputStream#close() close()} has no effect.
	 * 
	 * @param out
	 *            the OutputStream to decorate
	 * @return a version of the OutputStream that ignores calls to close
	 */
	public static OutputStream nonClosing(OutputStream out) {
		Assertion.notNull(out, "No OutputStream specified");
		return new NonClosingOutputStream(out);
	}

	private static class NonClosingInputStream extends FilterInputStream {

		public NonClosingInputStream(InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
		}
	}

	private static class NonClosingOutputStream extends FilterOutputStream {

		public NonClosingOutputStream(OutputStream out) {
			super(out);
		}

		@Override
		public void write(byte[] b, int off, int let) throws IOException {
			out.write(b, off, let);
		}

		@Override
		public void close() throws IOException {
		}
	}
}
