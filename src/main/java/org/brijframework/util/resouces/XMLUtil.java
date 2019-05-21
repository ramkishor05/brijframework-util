package org.brijframework.util.resouces;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.brijframework.util.validator.ValidationUtil;
import org.json.JSONException;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class XMLUtil {

	

	/**
	 * Convert {@link DOMSource} object to byte array
	 * 
	 * @since Brijframework 1.0
	 * @param _doc
	 *            {@link Document}
	 * @return byte array
	 */
	public static byte[] documentToBytes(Document _doc) {
		return documentToText(_doc).getBytes();
	}

	/**
	 * Convert byte array to DOM
	 * 
	 * @since Brijframework 1.0
	 * @param _bytes
	 * @return {@link Document}
	 */

	public static Document bytesToDocument(byte[] _bytes) {
		return xmlTextToDocument(new String(_bytes));
	}

	/**
	 * XML String Convert  to XML Document
	 * 
	 * @since Brijframework 1.0
	 * @param _string
	 * @return {@link Document}
	 */
	public static Document xmlTextToDocument(String _string) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(_string)));
		} catch (Exception e) {
			return null;
		}
	}

	// XML --> Hashtable XML --> Bytes -->

	/**
	 * Convert DOM object to String
	 * 
	 * @since Brijframework 1.0
	 * @param doc
	 *            {@link Document}
	 * @return {@link String}
	 */
	public static String documentToText(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			return null;
	    }
	}
	
	
	public static Map<String,Object> rootMapFromXML(String xmlString){
		if(ValidationUtil.isEmptyObject(xmlString)){
			return null;
		}
		try {
			return JSONUtil.getMapFromJSONObject(XML.toJSONObject(xmlString));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static String getCleanXMLFor(String str) {
		if (str == null) {
			return "";
		}
		if (str.indexOf('&') == -1 && str.indexOf('<') == -1) {
			return str;
		} else {
			if (str.equals("&nbsp;")) {
				return str;
			} else {
				StringBuffer strBuf = new StringBuffer();
				char[] charArray = str.toCharArray();
				for (int i = 0; i < charArray.length; i++) {
					if (charArray[i] == '&') {
						strBuf.append("&amp;");
					} else if (charArray[i] == '<') {
						strBuf.append("&lt;");
					} else if (charArray[i] == '>') {
						strBuf.append("&gt;");
					} else {
						strBuf.append(charArray[i]);
					}
				}
				return strBuf.toString();
			}
		}
	}
	
	public static Document getXmlDocFromString(String string) throws Exception {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		StringReader stringReader = null;
		try {
			stringReader = new StringReader(string);
			InputSource inputSource = new InputSource(stringReader);
			return builder.parse(inputSource);
		} finally {
			try {
				stringReader.close();
			} catch (Exception ignored) {
			}
		}
	}
}
