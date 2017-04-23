package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualifier specifying a parameter to be configured via {@link ConfigurationProducer}. Values of these parameters are resolved against system properties or servlet context initialization parameters.
 * 
 * @author thanos
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Configuration {
	/**
	 * xkcd base URL to use.
	 */
	public static final String BASE_URL = "xkcd.base.url";

	/**
	 * Caching implementation to use.
	 */
	public static final String CACHING = "xkcd.caching";

	/**
	 * Configuration file to use.
	 */
	public static final String CONFIGURATION_FILE = "xkcd.configuration.file";

	/**
	 * The name of the system property or the servlet context initialization parameter to use for configuring the qualified parameter.
	 * 
	 * @return the name of the system property or the servlet context initialization parameter to use for configuring the qualified parameter
	 */
	@Nonbinding
	public String value();
}
