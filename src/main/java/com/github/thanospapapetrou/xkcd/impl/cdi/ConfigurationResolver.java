package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Class defining CDI producer methods for parameters annotated with the {@link Configuration} qualifier. Values for these parameters are resolved against system properties or servlet context initialization parameters. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@ApplicationScoped
@WebListener
public class ConfigurationResolver implements ServletContextListener {
	private static final String ERROR_LOADING_CONFIGURATION_FILE = "Error loading configuration file %1$s";
	private static final String LOADED_CONFIGURATION_FILE = "Loaded configuration file %1$s";
	private static final Logger LOGGER = Logger.getLogger(ConfigurationResolver.class.getCanonicalName());
	private static final String NULL_EVENT = "Event must not be null";
	private static final String NULL_INJECTION_POINT = "Injection point must not be null";
	private static final String NULL_KEY = "Key must not be null";
	private static final String NULL_SERVLET_CONTEXT = "Servlet context must not be null";
	private static final String SET_TO = "%1$s set to %2$s";

	/**
	 * Resolve a {@link Caching} configuration parameter value.
	 * 
	 * @param servletContext
	 *            the servlet context to use for resolving configuration parameter value
	 * @param injectionPoint
	 *            the injection point where the configuration parameter value is to be injected
	 * @return the configuration parameter value
	 */
	@Configuration
	@Produces
	public static Caching resolveCaching(final ServletContext servletContext, final InjectionPoint injectionPoint) {
		Objects.requireNonNull(injectionPoint, NULL_INJECTION_POINT);
		return resolveCaching(servletContext, injectionPoint.getAnnotated().getAnnotation(Configuration.class).value());
	}

	/**
	 * Resolve a {@link Caching} configuration parameter value.
	 * 
	 * @param servletContext
	 *            the servlet context to use for resolving configuration parameter value
	 * @param key
	 *            the key of the configuration parameter
	 * @return the configuration parameter value
	 */
	public static Caching resolveCaching(final ServletContext servletContext, final Configuration.Key key) {
		final String value = resolveString(servletContext, key);
		return (value == null) ? null : Caching.valueOf(value);
	}

	/**
	 * Resolve a {@link URL} configuration parameter value.
	 * 
	 * @param servletContext
	 *            the servlet context to use for resolving configuration parameter value
	 * @param injectionPoint
	 *            the injection point where the configuration parameter value is to be injected
	 * @return the configuration parameter value
	 * @throws MalformedURLException
	 *             if any errors occur while converting the value to URL
	 */
	@Configuration
	@Produces
	public static URL resolveUrl(final ServletContext servletContext, final InjectionPoint injectionPoint) throws MalformedURLException {
		Objects.requireNonNull(injectionPoint, NULL_INJECTION_POINT);
		final String value = resolveString(servletContext, injectionPoint.getAnnotated().getAnnotation(Configuration.class).value());
		return (value == null) ? null : new URL(value);
	}

	private static String resolveString(final ServletContext servletContext, final Configuration.Key key) {
		Objects.requireNonNull(servletContext, NULL_SERVLET_CONTEXT);
		Objects.requireNonNull(key, NULL_KEY);
		final Object value = servletContext.getAttribute(key.toString());
		return (value == null) ? null : value.toString();
	}

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		// nothing to do on context destruction
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		Objects.requireNonNull(event, NULL_EVENT);
		final Properties configuration = new Properties();
		final String configurationFile = System.getProperty(Configuration.Key.CONFIGURATION_FILE.toString());
		if (configurationFile != null) {
			try (final FileInputStream input = new FileInputStream(new File(configurationFile))) {
				configuration.load(input);
				LOGGER.config(String.format(LOADED_CONFIGURATION_FILE, configurationFile));
			} catch (final IOException e) {
				LOGGER.log(Level.WARNING, String.format(ERROR_LOADING_CONFIGURATION_FILE, configurationFile), e);
			}
		}
		final ServletContext servletContext = event.getServletContext();
		for (final String name : Collections.list(servletContext.getInitParameterNames())) {
			final String value = System.getProperty(name, configuration.getProperty(name, servletContext.getInitParameter(name)));
			servletContext.setAttribute(name, value);
			LOGGER.config(String.format(SET_TO, name, value));
		}
	}
}
