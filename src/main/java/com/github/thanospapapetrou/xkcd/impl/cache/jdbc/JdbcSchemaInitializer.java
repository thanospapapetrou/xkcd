package com.github.thanospapapetrou.xkcd.impl.cache.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cdi.Caching;
import com.github.thanospapapetrou.xkcd.impl.cdi.Configuration;
import com.github.thanospapapetrou.xkcd.impl.cdi.ConfigurationResolver;

/**
 * Servlet context listener to initialize JDBC schema on application startup. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@WebListener
public class JdbcSchemaInitializer implements ServletContextListener {
	private static final String COUNT_COMICS = "SELECT COUNT(*) FROM SYS.SYSSCHEMAS S, SYS.SYSTABLES T WHERE (S.SCHEMANAME = 'APP') AND (S.SCHEMAID = T.SCHEMAID) AND (T.TABLENAME = 'COMICS')";
	private static final String CREATE_COMICS = "CREATE TABLE COMICS (ID INT NOT NULL PRIMARY KEY, \"DATE\" DATE NOT NULL, TITLE VARCHAR (%1$d) NOT NULL, SAFE_TITLE VARCHAR (%2$d) NOT NULL, IMAGE VARCHAR (%3$d) NOT NULL, ALTERNATE VARCHAR (%4$d), TRANSCRIPT VARCHAR (%5$d), LINK VARCHAR (%6$d), NEWS VARCHAR (%7$d))";
	private static final String ERROR_INITIALIZING_SCHEMA = "Error initializing schema";
	private static final Logger LOGGER = Logger.getLogger(JdbcSchemaInitializer.class.getCanonicalName());
	private static final String NULL_EVENT = "Event must not be null";
	private static final String SCHEMA_ALREADY_INITIALIZED = "Schema already initialized";
	private static final String SCHEMA_INITIALIZED = "Schema initialized";

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		// nothing to do on context destruction
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		Objects.requireNonNull(event, NULL_EVENT);
		if (ConfigurationResolver.resolveCaching(event.getServletContext(), Configuration.Key.CACHING) == Caching.JDBC) {
			try {
				try (final Connection connection = CDI.current().select(Connection.class).get()) {
					try (final PreparedStatement countComics = connection.prepareStatement(COUNT_COMICS)) {
						try (final ResultSet comics = countComics.executeQuery()) {
							comics.next();
							if (comics.getInt(1) == 0) {
								try (final PreparedStatement createComics = connection.prepareStatement(String.format(CREATE_COMICS, Comic.TITLE_LENGTH, Comic.SAFE_TITLE_LENGTH, Comic.IMAGE_LENGTH, Comic.ALTERNATE_LENGTH, Comic.TRANSCRIPT_LENGTH, Comic.LINK_LENGTH, Comic.NEWS_LENGTH))) {
									createComics.executeUpdate();
									LOGGER.info(SCHEMA_INITIALIZED);
								}
							} else {
								LOGGER.info(SCHEMA_ALREADY_INITIALIZED);
							}
						}
					}
				}
			} catch (final SQLException e) {
				LOGGER.log(Level.WARNING, ERROR_INITIALIZING_SCHEMA, e);
			}
		}
	}
}
