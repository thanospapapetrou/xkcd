package com.github.thanospapapetrou.xkcd.impl;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.github.thanospapapetrou.xkcd.impl.cdi.Caching;
import com.github.thanospapapetrou.xkcd.impl.cdi.Configuration;
import com.github.thanospapapetrou.xkcd.impl.cdi.ConfigurationProducer;

/**
 * Servlet context listener to shutdown Derby on application shutdown. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@WebListener
public class DerbyTerminator implements ServletContextListener {
	private static final String DERBY_SHUT_DOWN = "Derby shut down";
	private static final String ERROR_SHUTTING_DOWN_DERBY = "Error shutting down Derby";
	private static final Logger LOGGER = Logger.getLogger(DerbyTerminator.class.getCanonicalName());
	private static final String SHUTDOWN_JDBC_URL = "jdbc:derby:;shutdown=true";
	private static final String SHUTDOWN_SQL_STATE = "XJ015";

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		final Caching caching = new ConfigurationProducer(event.getServletContext()).produceCaching(Configuration.CACHING);
		if ((caching == Caching.JDBC) || (caching == Caching.JPA)) {
			try {
				DriverManager.getConnection(SHUTDOWN_JDBC_URL).close();
			} catch (final SQLException e) {
				if (e.getSQLState().equals(SHUTDOWN_SQL_STATE)) {
					LOGGER.info(DERBY_SHUT_DOWN);
				} else {
					LOGGER.log(Level.WARNING, ERROR_SHUTTING_DOWN_DERBY, e);
				}
			}
		}
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		// nothing to do on context initialization
	}
}
