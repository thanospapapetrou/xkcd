package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import com.github.thanospapapetrou.xkcd.impl.cache.Cache;

/**
 * Class defining static CDI producer and disposer methods.
 * 
 * @author thanos
 */
public class InjectionProducer {
	private static final String DATA_SOURCE = "java:/comp/env/jdbc/xkcd";
	private static final String NULL_CACHING = "Caching must not be null";
	private static final String NULL_CONNECTION = "Connection must not be null";
	private static final String NULL_DATA_SOURCE = "Data source must not be null";
	private static final String NULL_ENTITY_MANAGER = "Entity manager must not be null";
	private static final String NULL_ENTITY_MANAGER_FACTORY = "Entity manager factory must not be null";
	private static final String PERSISTENCE_UNIT = "xkcd";

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
	public static Cache produceCache(@Configuration(Configuration.CACHING) final Caching caching) {
		Objects.requireNonNull(caching, NULL_CACHING);
		return (caching.getImplementation() == null) ? null : CDI.current().select(caching.getImplementation()).get();
	}

	/**
	 * Produce a connection.
	 * 
	 * @param dataSource
	 *            the data source to use to produce the connection
	 * @return a connection
	 * @throws SQLException
	 *             if any errors occur
	 */
	@Produces
	public static Connection produceConnection(final DataSource dataSource) throws SQLException {
		Objects.requireNonNull(dataSource, NULL_DATA_SOURCE);
		synchronized (dataSource) {
			return dataSource.getConnection();
		}
	}

	/**
	 * Produce a data source.
	 * 
	 * @return a data source
	 * @throws NamingException
	 *             if any errors occur
	 */
	@Produces
	@ApplicationScoped
	public static DataSource produceDataSource() throws NamingException {
		return (DataSource) (new InitialContext().lookup(DATA_SOURCE));
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
	 * @return an entity manager factory
	 */
	@Produces
	@ApplicationScoped
	public static EntityManagerFactory produceEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}
}
