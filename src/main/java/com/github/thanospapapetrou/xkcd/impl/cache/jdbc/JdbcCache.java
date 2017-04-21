package com.github.thanospapapetrou.xkcd.impl.cache.jdbc;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cache.Cache;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.AlternateConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.ImageConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.LinkConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.NewsConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.SafeTitleConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.TitleConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.TranscriptConverter;

/**
 * Class implementing a cache using JDBC. Are instances of this class thread-safe?
 * 
 * @author thanos
 */
@RequestScoped
public class JdbcCache implements Cache {
	private static final int ALTERNATE = 6;
	private static final int DATE = 2;
	private static final String ERROR_LOADING_COMIC = "Error loading comic %1$d";
	private static final String ERROR_LOADING_LATEST_COMIC = "Error loading latest comic";
	private static final String ERROR_SAVING_COMIC = "Error saving comic %1$d";
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT+00:00");
	private static final int ID = 1;
	private static final int IMAGE = 5;
	private static final int LINK = 8;
	private static final String LOAD = "SELECT C.ID, C.\"DATE\", C.TITLE, C.SAFE_TITLE, C.IMAGE, C.ALTERNATE, C.TRANSCRIPT, C.LINK, C.NEWS FROM COMICS C WHERE (C.ID = ?)";
	private static final String LOAD_LATEST = "SELECT C.ID, C.\"DATE\", C.TITLE, C.SAFE_TITLE, C.IMAGE, C.ALTERNATE, C.TRANSCRIPT, C.LINK, C.NEWS FROM COMICS C ORDER BY C.ID DESC FETCH FIRST 1 ROW ONLY";
	private static final int NEWS = 9;
	private static final String NULL_COMIC = "Comic must not be null";
	private static final String NULL_CONNECTION = "Connection must not be null";
	private static final int SAFE_TITLE = 4;
	private static final int TITLE = 3;
	private static final String SAVE = "INSERT INTO COMICS (ID, \"DATE\", TITLE, SAFE_TITLE, IMAGE, ALTERNATE, TRANSCRIPT, LINK, NEWS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final int TRANSCRIPT = 7;

	private final Connection connection;

	/**
	 * Construct a new JDBC cache.
	 * 
	 * @param connection
	 *            the connection to use
	 */
	@Inject
	public JdbcCache(final Connection connection) {
		this.connection = Objects.requireNonNull(connection, NULL_CONNECTION);
	}

	JdbcCache() {
		// this constructor exists just to keep CDI happy
		connection = null;
	}

	@Override
	public Comic load(final int id) throws XkcdException {
		try (final PreparedStatement load = connection.prepareStatement(LOAD)) {
			load.setInt(1, id);
			try (final ResultSet comic = load.executeQuery()) {
				return comic.next() ? new Comic(comic.getInt(ID), comic.getDate(DATE), comic.getString(TITLE), comic.getString(SAFE_TITLE), new URL(comic.getString(IMAGE)), comic.getString(ALTERNATE), comic.getString(TRANSCRIPT), (comic.getString(LINK) == null) ? null : new URL(comic.getString(LINK)), comic.getString(NEWS)) : null;
			}
		} catch (final MalformedURLException | SQLException e) {
			throw new XkcdException(String.format(ERROR_LOADING_COMIC, id), e);
		}
	}

	@Override
	public Comic loadLatest() throws XkcdException {
		try (final PreparedStatement loadLatest = connection.prepareStatement(LOAD_LATEST)) {
			try (final ResultSet comic = loadLatest.executeQuery()) {
				return comic.next() ? new Comic(comic.getInt(ID), comic.getDate(DATE), comic.getString(TITLE), comic.getString(SAFE_TITLE), new URL(comic.getString(IMAGE)), comic.getString(ALTERNATE), comic.getString(TRANSCRIPT), (comic.getString(LINK) == null) ? null : new URL(comic.getString(LINK)), comic.getString(NEWS)) : null;
			}
		} catch (final MalformedURLException | SQLException e) {
			throw new XkcdException(ERROR_LOADING_LATEST_COMIC, e);
		}
	}

	@Override
	public void save(final Comic comic) throws XkcdException {
		Objects.requireNonNull(comic, NULL_COMIC);
		try (final PreparedStatement save = connection.prepareStatement(SAVE)) {
			save.setInt(ID, comic.getId());
			save.setDate(DATE, new Date(comic.getDate().getTime()), new GregorianCalendar(GMT, Locale.ROOT));
			save.setString(TITLE, new TitleConverter().convertToDatabaseColumn(comic.getTitle()));
			save.setString(SAFE_TITLE, new SafeTitleConverter().convertToDatabaseColumn(comic.getSafeTitle()));
			save.setString(IMAGE, new ImageConverter().convertToDatabaseColumn(comic.getImage()));
			save.setString(ALTERNATE, new AlternateConverter().convertToDatabaseColumn(comic.getAlternate()));
			save.setString(TRANSCRIPT, new TranscriptConverter().convertToDatabaseColumn(comic.getTranscript()));
			save.setString(LINK, new LinkConverter().convertToDatabaseColumn(comic.getLink()));
			save.setString(NEWS, new NewsConverter().convertToDatabaseColumn(comic.getNews()));
			save.executeUpdate();
		} catch (final SQLException e) {
			throw new XkcdException(String.format(ERROR_SAVING_COMIC, comic.getId()), e);
		}
	}
}
