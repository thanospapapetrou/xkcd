package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualifier specifying a parameter to be configured via {@link ConfigurationResolver}. Values of these parameters are resolved against system properties, properties defined via a configuration file or servlet context initialization parameters.
 * 
 * @author thanos
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Configuration {
	/**
	 * Enumeration specifying keys of configuration parameters.
	 * 
	 * @author thanos
	 */
	public enum Key {
		/**
		 * xkcd base URL to use.
		 */
		BASE_URL("xkcd.base.url"),

		/**
		 * Caching implementation to use.
		 */
		CACHING("xkcd.caching"),

		/**
		 * Configuration file to use.
		 */
		CONFIGURATION_FILE("xkcd.configuration.file"),

		/**
		 * Default empty value.
		 */
		DEFAULT(""),

		/**
		 * JDBC driver to use.
		 */
		JDBC_DRIVER("xkcd.jdbc.driver"),

		/**
		 * JDBC URL to use.
		 */
		JDBC_URL("xkcd.jdbc.url");

		private final String key;

		private Key(final String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return key;
		}
	}

	/**
	 * The name of the system property or the servlet context initialization parameter to use for configuring the qualified parameter.
	 * 
	 * @return the name of the system property or the servlet context initialization parameter to use for configuring the qualified parameter
	 */
	@Nonbinding
	public Key value() default Key.DEFAULT;
}
