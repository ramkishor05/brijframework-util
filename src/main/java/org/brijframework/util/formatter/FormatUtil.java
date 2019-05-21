package org.brijframework.util.formatter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.text.MaskFormatter;

import org.brijframework.util.text.StringUtil;

public abstract class FormatUtil {
	
	static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';
    
    
    public static String getEncodedZipData(Object o) {
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteArray);
			out.writeObject(o);
			byteArray.flush();
			return zipAndEncodeBuffer(byteArray.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

    public static String encodeBuffer(byte[] bytes) {
        StringBuffer aBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int j = ((int)bytes[i])-((int)Byte.MIN_VALUE);
            String x = Integer.toHexString(j);
            if (x.length() == 1)
                x = "0" + x;
            aBuffer.append(x);
        }
        return aBuffer.toString();
    }

    public static byte[] decodeBuffer(String aString) {
        byte[] aByteArray = new byte[aString.length()/2];
        for (int i = 0; i < aString.length()/2; i++) {
            aByteArray[i] =  (byte)(16 * stringToInt(aString.charAt(2*i)));
            aByteArray[i] += (byte)stringToInt(aString.charAt(2*i+1));
            aByteArray[i] += Byte.MIN_VALUE;
        }
        return aByteArray;
    }

    public static String encodeBuffer(File aFile) {
        byte[] aByteArray;
        try {
            if (aFile.exists()) {
                RandomAccessFile raf = new RandomAccessFile(aFile, "r");
                aByteArray = new byte[(int)raf.length()];
                raf.read(aByteArray);
                raf.close();
                return encodeBuffer(aByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeBuffer(String aString, File aFile) {
        try {
            RandomAccessFile raf = new RandomAccessFile(aFile, "rw");
            byte[] retVal = decodeBuffer(aString);
            raf.write(retVal);
            raf.close();
            return retVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int stringToInt(char c) {
        switch (c) {
            case '0' : return 0;
            case '1' : return 1;
            case '2' : return 2;
            case '3' : return 3;
            case '4' : return 4;
            case '5' : return 5;
            case '6' : return 6;
            case '7' : return 7;
            case '8' : return 8;
            case '9' : return 9;
            case 'a' : return 10;
            case 'b' : return 11;
            case 'c' : return 12;
            case 'd' : return 13;
            case 'e' : return 14;
            case 'f' : return 15;
            default : return 15;
        }
    }
	
	public static String zipAndEncodeBuffer(byte bytes[]){
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		ZipEntry fileEntry = new ZipEntry("NAME");
		ZipOutputStream zos = new ZipOutputStream(writer);
		try {
			zos.putNextEntry(fileEntry);
			zos.write(bytes);
			zos.flush();
			zos.close();
		}catch(Exception e){
			throw new RuntimeException("Unable to zip Data");
		}
		return encodeBuffer(writer.toByteArray());
	}
	
	public static byte[] decodeUnzipBuffer(String data){
		try {
			ZipInputStream rd =new ZipInputStream( new ByteArrayInputStream(decodeBuffer(data)));
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int i;
			/*ZipEntry zipEntry = */
			rd.getNextEntry();
			while((i = rd.read())!= -1){
				bout.write(i);
			}
			rd.close();
			return 	bout.toByteArray();
		}catch(Exception e){
			throw new RuntimeException("Unable to zip Data");
		}

	}
	
	public static byte[] hashToBytes(Map<?,?> hash){
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream p = new ObjectOutputStream(byteStream);
			p.writeObject(hash);
			p.flush();
			byte[] array = byteStream.toByteArray();
			p.close();
			byteStream.close();
			return array; 
		}catch(Exception e){
			return null;
		}
 	}
	
	public static Object getDecodedObject(String encodedData) {
		try {
			byte[] decodedBytes =decodeUnzipBuffer(encodedData);
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes));
			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static Object getDecodedObject(String encodedData, boolean throwException) throws IOException, ClassNotFoundException {
		byte[] decodedBytes =decodeUnzipBuffer(encodedData);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decodedBytes));
		return in.readObject();
	}
	
	public static Map<?,?> readHashTable(File file){
		try {
			ObjectInputStream p = new ObjectInputStream(new FileInputStream(file));
			Map<?,?> hash = (Map<?,?>)p.readObject();
			p.close();
			return hash;
		}catch(Exception e){
			return null;
		}
 	}



	public  static final Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }

        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }


    public static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        final int trimemdLen = argArray.length - 1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }

   

    public static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {

        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    public static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenMap);
            }
        }
    }

    public static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            sbuf.append("[FAILED toString()]");
        }

    }

    public static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            // allow repeats in siblings
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    public static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    public static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1)
                sbuf.append(", ");
        }
        sbuf.append(']');
    }

    
    public static String formatStrToUSPhoneFormat(Object phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		String phone = String.valueOf(phoneNumber);
		if (phone.length() < 6) {
			return phone;
		}
		return phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
	}

	public static String customFormatter(String val, String formate) {
		MaskFormatter maskFormatter;
		try {
			maskFormatter = new MaskFormatter(formate);
			maskFormatter.setValueContainsLiteralCharacters(false);
			return maskFormatter.valueToString(val);
		} catch (ParseException e) {
		}
		return formate;
	}

	/**
	 * This method parse messageString like "{0} {1} lives in {2}" and creates
	 * and returns {@link MessageFormat} object
	 * 
	 * @param _messageString
	 *            string corresponding to which {@link MessageFormat} object
	 *            needed
	 * @return {@link MessageFormat} object by parsing messageString
	 */
	public MessageFormat getMessageFormat(String _messageString) {
		MessageFormat messageFormat = null;
		try {
			messageFormat = StringUtil.isEmpty(_messageString) ? null : new MessageFormat(_messageString);
		} catch (IllegalArgumentException e) {
		}
		return messageFormat;
	}

	public String formattedMessage(String msg, String iid, Object... params) {
		if (msg == null) {
			return iid;
		}
		MessageFormat messageFormat = this.getMessageFormat(msg);
		return messageFormat == null ? iid : messageFormat.format(params);
	}

	public String formatMessage(String messageString, Object[] params) {
		MessageFormat messageFormat = this.getMessageFormat(messageString);
		if (messageFormat == null) {
		   return null;
		}
		return messageFormat.format(params);
	}

	/**
	 * This method will parse any string with object key. For eg. "Hi
	 * {{firstName}} {{lastName}}. How are you ?" Any key within {{ }} will get
	 * search inside supplied FNHashMap and replace with value in string
	 * 
	 */
	public static String ngMessageFormat(Matcher matcher, String messageString, Map<String, Object> replacements) {
		StringBuffer buffer = new StringBuffer();
		if (matcher == null) {
			Pattern pattern = Pattern.compile("\\{\\{(.+?)\\}\\}"); // Create a pattern for {{anyKey}}
			matcher = pattern.matcher(messageString);
		}
		while (matcher.find()) {
			Object replacement = replacements.get(matcher.group(1));
			if (replacement != null) {
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}
