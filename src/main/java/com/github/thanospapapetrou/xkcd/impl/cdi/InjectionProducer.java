package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.github.thanospapapetrou.xkcd.impl.cache.Cache;

/**
 * Class defining static CDI producer and disposer methods.
 * 
 * @author thanos
 */
public final class InjectionProducer {
	private static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";
	private static final String JAVAX_PERSISTENCE_JDBC_URL = "javax.persistence.jdbc.url";
	private static final String NULL_CACHING = "Caching must not be null";
	private static final String NULL_CONNECTION = "Connection must not be null";
	private static final String NULL_ENTITY_MANAGER = "Entity manager must not be null";
	private static final String NULL_ENTITY_MANAGER_FACTORY = "Entity manager factory must not be null";
	private static final String NULL_JDBC_DRIVER = "JDBC driver must not be null";
	private static final String NULL_JDBC_URL = "JDBC URL must not be null";
	private static final String PERSISTENCE_UNIT = "xkcd";

	private InjectionProducer() {
	}

	/**
	 * Dispose a connection by closing it.
	 * 
	 * @param connection
	 *            the connection to dispose
	 * @throws SQLException
	 *             if any errors occur
	 */
	public static void dispose(@Disposes final Connection connection) throws SQLException {
		Objects.requireNonNull(connection, NULL_CONNECTION);
		if (!connection.isClosed()) {
			connection.close();
		}
	}

	/**
	 * Dispose an entity manager by closing it.
	 * 
	 * @param entityManager
	 *            the entity manager to dispose
	 */
	public static void dispose(@Disposes final EntityManager entityManager) {
		Objects.requireNonNull(entityManager, NULL_ENTITY_MANAGER);
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}

	/**
	 * Dispose an entity manager factory by closing it.
	 * 
	 * @param entityManagerFactory
	 *            the entity manager factory to dispose
	 */
	public static void dispose(@Disposes final EntityManagerFactory entityManagerFactory) {
		Objects.requireNonNull(entityManagerFactory, NULL_ENTITY_MANAGER_FACTORY);
		if (entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}

	/**
	 * Produce a cache.
	 * 
	 * @param caching
	 *            the caching mode to use for producing the cache
	 * @return a cache
	 */
	@Produces
	@ImplementationSelector
	public static Cache produceCache(@Configuration(Configuration.Key.CACHING) final Caching caching) {
		Objects.requireNonNull(caching, NULL_CACHING);
		return (caching.getImplementation() == null) ? null : CDI.current().select(caching.getImplementation()).get();
	}

	/**
	 * Produce a connection.
	 * 
	 * @param jdbcDriver
	 *            the JDBC driver to use to produce the connection
	 * @param jdbcUrl
	 *            the JDBC URL to use to produce the connection
	 * @return a connection
	 * @throws ClassNotFoundException
	 *             if any errors occur while loading the JDBC driver
	 * @throws InstantiationException
	 *             if any errors occur while loading the JDBC driver
	 * @throws IllegalAccessException
	 *             if any errors occur while loading the JDBC driver
	 * @throws SQLException
	 *             if any errors occur while producing the connection
	 */
	@Produces
	public static Connection produceConnection(@Configuration(Configuration.Key.JDBC_DRIVER) final String jdbcDriver, @Configuration(Configuration.Key.JDBC_URL) final String jdbcUrl) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
		Objects.requireNonNull(jdbcDriver, NULL_JDBC_DRIVER);
		Objects.requireNonNull(jdbcUrl, NULL_JDBC_URL);
		Class.forName(jdbcDriver).newInstance();
		return DriverManager.getConnection(jdbcUrl);
	}

	/**
	 * Produce an entity manager.
	 * 
	 * @param entityManagerFactory
	 *            the entity manager factory to use to produce the entity manager
	 * @return an entity manager
	 */
	@Produces
	@RequestScoped
	public static EntityManager produceEntityManager(final EntityManagerFactory entityManagerFactory) {
		Objects.requireNonNull(entityManagerFactory, NULL_ENTITY_MANAGER_FACTORY);
		synchronized (entityManagerFactory) {
			return entityManagerFactory.createEntityManager();
		}
	}

	/**
	 * Produce an entity manager factory.
	 * 
	 * @param jdbcDriver
	 *            the JDBC driver to use
	 * @param jdbcUrl
	 *            the JDBC URL to use
	 * @return an entity manager factory
	 */
	@Produces
	@ApplicationScoped
	public static EntityManagerFactory produceEntityManagerFactory(@Configuration(Configuration.Key.JDBC_DRIVER) final String jdbcDriver, @Configuration(Configuration.Key.JDBC_URL) final String jdbcUrl) {
		Objects.requireNonNull(jdbcDriver, NULL_JDBC_DRIVER);
		Objects.requireNonNull(jdbcUrl, NULL_JDBC_URL);
		final Map<String, String> properties = new HashMap<String, String>();
		properties.put(JAVAX_PERSISTENCE_JDBC_DRIVER, jdbcDriver);
		properties.put(JAVAX_PERSISTENCE_JDBC_URL, jdbcUrl);
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
	}
}
