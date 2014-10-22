package de.fraunhofer.iao.muvi.managerweb.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Custom {@link PropertyPlaceholderConfigurer} which allows system properties
 * (set with java -Dproperty=value -jar MonitoringLauncher.jar) to override the properties from
 * monitoring.properties.
 * 
 * @author Bertram Frueh
 */
public class IaoPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	public IaoPropertyPlaceholderConfigurer() {

		// Allow definition of properties via command line arguments "-D..." by default.
		setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);

		// Don't lookup environment variables.
		setSearchSystemEnvironment(false);
	}
}
