package de.fraunhofer.iao.muvi.managerweb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads the configuration from monitoring.properties.
 * 
 * @author Bertram Frueh
 */
public class Config {

    private static final Log log = LogFactory.getLog(Config.class);

    public static final String SOFTWARE_VERSION = "1.0";
    public static final String SOFTWARE_DATE = "2014-02-04";

    private static final Properties PROPERTIES = loadProperties();

    private Config() {

    }

    /**
     * Returns the value of the property with the given key or {@code null}, if no such property can
     * be found.
     */
    public static String getProperty(String key) {

        return PROPERTIES.getProperty(key);
    }

    /**
     * Returns the value of the property with the given key or {@code null}, if no such property can
     * be found.
     */
    public static Long getPropertyAsLong(String key) {

        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            return null;
        }
        return Long.parseLong(value);
    }

    /**
     * Returns the value of the property with the given key. Throws an
     * {@link IllegalArgumentException} if no such property can be found.
     */
    public static int getPropertyAsInt(String key) {

        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Unable to find property by key '" + key + "'");
        }
        return Integer.parseInt(value);
    }

    /**
     * Returns the value of the property with the given key. Returns the given default value if no
     * such property can be found or if the value is empty.
     */
    public static int getPropertyAsInt(String key, int defaultValue) {

        String value = PROPERTIES.getProperty(key);
        if (Utils.isEmpty(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    private static Properties loadProperties() {

        Properties properties = new Properties();

        // monitoring.properties
        String monitoringConfig = System.getProperty("monitoringConfig");
        if (System.getProperty("monitoringConfig") == null) {
            String message = "System property 'monitoringConfig' is empty. Valid values are e.g. "
                    + "'classpath:monitoring.properties' or 'file:C:/monitoring.properties'. "
                    + "Taking default value.";
            log.warn(message);
            monitoringConfig = "classpath:monitoring.properties";
        }

        log.info(String.format("Reading monitoring config from '%s'", monitoringConfig));
        if (monitoringConfig.startsWith("classpath:")) {
            properties.putAll(loadFromClasspathResource(monitoringConfig));
        } else {
            properties.putAll(loadFromFile(monitoringConfig));
        }

        // local-system.properties
        String localConfig = System.getProperty("localConfig");
        if (System.getProperty("localConfig") == null) {
            String message = "System property 'localConfig' is empty. Valid values are e.g. "
                    + "'classpath:local-system.properties' or 'file:C:/local-system.properties'. "
                    + "Taking default value.";
            log.warn(message);
            localConfig = "classpath:local-system.properties";
        }

        log.info(String.format("Reading local system config from '%s'", localConfig));
        if (localConfig.startsWith("classpath:")) {
            properties.putAll(loadFromClasspathResource(localConfig));
        } else {
            properties.putAll(loadFromFile(localConfig));
        }

        // Override the properties from the files with the system properties (if present).
        properties.putAll(System.getProperties());

        // Log all properties except passwords.
        log.info("System configuration:");
        for (Entry<Object, Object> entry : properties.entrySet()) {
            if (!entry.getKey().toString().contains("password")) {
                log.info(entry);
            }
        }

        return properties;
    }

    private static Properties loadFromClasspathResource(String location) {

        Properties properties = new Properties();
        InputStream input = Config.class.getClassLoader().getResourceAsStream(
                location.replaceAll("classpath:", ""));
        if (input == null) {
            String message = String.format("Unable to read class path resource '%s'", location);
            log.error(message);
            throw new IllegalStateException(message);
        }

        try {
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            String message = String.format("Unable to read class path resource '%s': %s", location,
                    ex.getMessage());
            log.error(message);
            throw new IllegalStateException(message, ex);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    private static Properties loadFromFile(String location) {

        FileInputStream input = null;
        try {
            input = FileUtils.openInputStream(new File(location.replaceAll("file:", "")));
        } catch (IOException ex) {
            String message = String.format("Unable to read file '%s': %s", location,
                    ex.getMessage());
            log.error(message);
            throw new IllegalStateException(message, ex);
        }

        Properties properties = new Properties();
        try {
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            String message = String.format("Unable to load properties from file '%s': %s",
                    location, ex.getMessage());
            log.error(message);
            throw new IllegalStateException(message, ex);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
