package de.fraunhofer.iao.muvi.managerweb.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;

public class Utils {
	
	public static final int DEFAULT_WAIT_TIME = 30 * 1000;

	private static final Log log = LogFactory.getLog(Utils.class);

	/**
	 * Opens a new socket connection to test whether the given port is already
	 * in use.
	 */
	public static boolean isPortInUse(String host, int port) {

		try (Socket socket = new Socket(host, port)) {
			// The connection was established. Somebody is already using the
			// port.
			return true;
		} catch (IOException e) {
			// Nobody accepted our connection attempt. The port is free.
			return false;
		}
	}
	
	public static <T extends Object> T getObjectFromXml(String xml,
			Class<T> type) {
		return getObjectFromXml(xml, type, null);
	}

	/**
	 * Returns the Java object of the given XML string. Used e.g. for SNMP
	 * messages. Returns {@code null}, if the XML string cannot be deserialized
	 * into a Java object of the given type.
	 * @throws SAXException 
	 */
	public static <T extends Object> T getObjectFromXml(String xml,
			Class<T> type, File schemaFile) {
		
		T object = null;
		
		String errorMessage = "";
		
		MuViValidationEventHandler handler = new MuViValidationEventHandler(errorMessage);
		
		try {
			JAXBContext context = JAXBContext.newInstance(type);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			if (schemaFile != null) {
				SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = schemaFactory.newSchema(schemaFile);
				unmarshaller.setSchema(schema);
			}
			unmarshaller.setEventHandler(handler);
	
			JAXBElement<T> obj = unmarshaller.unmarshal(new StreamSource(
					new StringReader(xml)), type);
			object = obj.getValue();
		} catch (JAXBException | IllegalArgumentException | SAXException ex) {
						
			log.error(String.format(
					"Unable to deserialize XML '%s' into '%s': %s", xml, type,
					handler.getMessage()));
			throw new IllegalArgumentException(String.format(
					"Unable to deserialize XML '%s' into '%s': %s", xml, type,
					handler.getMessage()), ex);
		}
		return object;
	}
	
	public static String getXml(Object object) {
		return getXml(object, null, false);
	}

	/**
	 * Returns the XML representation of the given object. Used e.g. for
	 * transferring this object to the client's browser via HTTP.
	 * @throws SAXException 
	 */
	public static String getXml(Object object, File schemaFile, boolean showSchemaLocation) {

		String xml = null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING,
					StandardCharsets.UTF_8.name());
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			if (schemaFile != null) {
				SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = schemaFactory.newSchema(schemaFile); 
				marshaller.setSchema(schema);
				if (showSchemaLocation) {
					marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaFile.toURI().toString().replaceAll(".xsd", "") + " " + schemaFile.toURI().toString());
				}
			}
			StringWriter writer = new StringWriter();
			marshaller.marshal(object, writer);
			xml = writer.toString();
		} catch (JAXBException | SAXException ex) {
			log.error(String.format(
					"Unable to serialize object [%s] as XML: %s", object,
					ex.getMessage()), ex);
		}
		return xml;
	}
	
	/**
	 * Returns true, if the given string is {@code null} or empty. White space
	 * characters are ignored.
	 */
	public static boolean isEmpty(String value) {

		return value == null || value.trim().length() == 0;
	}

	/**
	 * Returns true, if the given string is neither {@code null} nor empty.
	 * White space characters are ignored.
	 */
	public static boolean isNotEmpty(String value) {

		return value != null && value.trim().length() > 0;
	}

	/**
	 * Returns true, if the given list is {@code null} or empty.
	 */
	public static boolean isEmpty(Collection<?> collection) {

		return collection == null || collection.size() < 1;
	}

	/**
	 * Returns true, if the given list is neither {@code null} nor empty.
	 */
	public static boolean isNotEmpty(Collection<?> collection) {

		return collection != null && collection.size() > 0;
	}

	public static boolean validateIP(final String ip) {

		String patternForIP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		Pattern pattern = Pattern.compile(patternForIP);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	
	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return url;
		}
	}
	
	public static String decodeUrl(String url) {
		try {
			return URLDecoder.decode(url, StandardCharsets.UTF_8.displayName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return url;
		}
	}
	
	public static String escapeXml(String xml) {

		return StringEscapeUtils.escapeXml(xml);

	}

	public static <K, V> boolean isNotEmpty(Map<K, V> map) {
		if (map != null) {
			return isNotEmpty(map.keySet());
		}
		return false;
	}
	
	/**
	 * Wait for 30 seconds.
	 */
	public static void waitDefault() {
		wait(DEFAULT_WAIT_TIME);
	}
	
	/**
	 * Wait for the given time (in milliseconds)
	 * @param time Time to wait for in milliseconds.
	 */
	public static void wait(int time) {
		try {
			log.debug("Wait " + time + "ms...");
			Thread.sleep(time);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Replaces http://MUVIMANAGER/ in a URL with the given replacement.
	 * @param url
	 * @param replacement
	 * @return
	 */
	public static URL replaceManagerCode(URL url, String replacement) {
		try {
		
			String imgUrlString = url.toString();
			
			if (imgUrlString.contains("http://MUVIMANAGER/")) {
				imgUrlString = imgUrlString.replace("http://MUVIMANAGER/", replacement);
			}

			return new URL(imgUrlString);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			return url;
		}
	}
	
	public static List<ScreenID> convertToScreenIDList(String listAsString) {
		List<ScreenID> list = new ArrayList<>();
		if (Utils.isNotEmpty(listAsString)) {
			String[] ids = listAsString.split(",");
			for (String id : ids) {
				if (Utils.isNotEmpty(id)) {
					try {
						list.add(new ScreenID(Integer.parseInt(id)));
					} catch (Exception e) {
						log.error("Invalid screen ID " + id);
					}
				}
			}
		}
		return list;
	}
	
	public static String convertToString(List<ScreenID> screenIDList) {
		String listAsString = "";
		if (Utils.isNotEmpty(screenIDList)) {
			for (ScreenID screenID : screenIDList) {
				listAsString += screenID + ",";
			}
			listAsString = listAsString.substring(0, listAsString.length() - 1);
		}
		return listAsString;
	}
	
	public static URL getURLFromImageFile(File imageFile) {
		URL url = null;
		try {
			url = new URL("http://137.251.35.100:3636/manager/apps/readImage.jsp?img=file:///" + imageFile.getAbsolutePath());
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
		}
		return url;
	}

}
