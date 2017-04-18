package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.github.thanospapapetrou.xkcd.impl.cache.Cache;

/**
 * Class defining CDI producer methods for parameters annotated with the {@link Configuration} qualifier. Values for these parameters are resolved against system properties or servlet context initialization parameters. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@ApplicationScoped
public class ConfigurationProducer {
	private static final String NULL_INJECTION_POINT = "Injection point must not be null";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_SERVLET_CONTEXT = "Servlet context must not be null";

	private final ServletContext servletContext;

	/**
	 * Construct a new configuration producer.
	 * 
	 * @param servletContext
	 *            the servlet context in which the current web application is running
	 */
	@Inject
	public ConfigurationProducer(final ServletContext servletContext) {
		this.servletContext = Objects.requireNonNull(servletContext, NULL_SERVLET_CONTEXT);
	}

	/**
	 * Produce a cache.
	 * 
	 * @param injectionPoint
	 *            the injection point where the cache is to be injected
	 * @return a cache to be injected in the given injection point
	 */
	@Configuration("")
	@Produces
	public Cache produceCache(final InjectionPoint injectionPoint) {
		final Class<? extends Cache> implementation = Caching.valueOf(produceString(injectionPoint)).getImplementation();
		return (implementation == null) ? null : CDI.current().select(implementation).get();
	}

	/**
	 * Produce a caching.
	 * 
	 * @param name
	 *            the name of the configuration parameter
	 * @return a cache to be injected in the given injection point
	 */
	public Caching produceCaching(final String name) {
		return Caching.valueOf(produceString(name));
	}

	/**
	 * Produce a URL.
	 * 
	 * @param injectionPoint
	 *            the injection point where the URL is to be injected
	 * @return a URL to be injected in the given injection point
	 * @throws MalformedURLException
	 *             if the resolved value is not a valid URL
	 */
	@Configuration("")
	@Produces
	public URL produceUrl(final InjectionPoint injectionPoint) throws MalformedURLException {
		return new URL(produceString(injectionPoint));
	}

	private String produceString(final InjectionPoint injectionPoint) {
		Objects.requireNonNull(injectionPoint, NULL_INJECTION_POINT);
		return produceString(injectionPoint.getAnnotated().getAnnotation(Configuration.class).value());
	}

	private String produceString(final String name) {
		Objects.requireNonNull(name, NULL_NAME);
		synchronized (servletContext) {
			return System.getProperty(name, servletContext.getInitParameter(name));
		}
	}
}
