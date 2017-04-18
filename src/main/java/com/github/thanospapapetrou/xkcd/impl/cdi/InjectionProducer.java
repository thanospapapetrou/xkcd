package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.sql.Connection;
import java.sql.SQLException;

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

/**
 * Class defining CDI producer and disposer methods.
 * 
 * @author thanos
 */
@ApplicationScoped
public class InjectionProducer {
	private static final String DATA_SOURCE = "java:/comp/env/jdbc/xkcd";
	private static final String PERSISTENCE_UNIT = "xkcd";

	/**
	 * Dispose a connection by closing it.
	 * 
	 * @param connection
	 *            the connection to dispose
	 * @throws SQLException
	 *             if any errors occur
	 */
	public void dispose(@Disposes final Connection connection) throws SQLException {
		if (!connection.isClosed()) {
			connection.close();
		}
	}

	/**
	 * Dispose an entity manager by closing it.
	 * 
	 * @param manager
	 *            the entity manager to dispose
	 */
	public void dispose(@Disposes final EntityManager manager) {
		if (manager.isOpen()) {
			manager.close();
		}
	}

	/**
	 * Dispose an entity manager factory by closing it.
	 * 
	 * @param factory
	 *            the entity manager factory to dispose
	 */
	public void dispose(@Disposes final EntityManagerFactory factory) {
		if (factory.isOpen()) {
			factory.close();
		}
	}

	/**
	 * Produce a connection.
	 * 
	 * @return a connection
	 * @throws SQLException
	 *             if any errors occur
	 */
	@Produces
	@RequestScoped
	public Connection produceConnection() throws SQLException {
		final DataSource dataSource = CDI.current().select(DataSource.class).get();
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
	public DataSource produceDataSource() throws NamingException {
		return (DataSource) (new InitialContext().lookup(DATA_SOURCE));
	}

	/**
	 * Produce an entity manager.
	 * 
	 * @return an entity manager
	 */
	@Produces
	@RequestScoped
	public EntityManager produceEntityManager() {
		final EntityManagerFactory factory = CDI.current().select(EntityManagerFactory.class).get();
		synchronized (factory) {
			return factory.createEntityManager();
		}
	}

	/**
	 * Produce an entity manager factory.
	 * 
	 * @return an entity manager factory
	 */
	@Produces
	@ApplicationScoped
	public EntityManagerFactory produceEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}
}
