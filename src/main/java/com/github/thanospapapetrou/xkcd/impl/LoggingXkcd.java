package com.github.thanospapapetrou.xkcd.impl;

import java.util.Objects;
import java.util.logging.Logger;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import com.github.thanospapapetrou.xkcd.api.Xkcd;
import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Decorator extending an xkcd implementation with logging. Instances of this class are thread safe, provided they are constructed with a thread-safe xkcd.
 * 
 * @author thanos
 */
@Decorator
public class LoggingXkcd implements Xkcd {
	private static final String COMIC_NOT_FOUND = "Comic %1$d not found";
	private static final String COMIC_RETRIEVED = "Comic %1$d retrieved";
	private static final String CURRENT_COMIC_RETRIEVED = "Current comic (%1$d) retrieved";
	private static final String NULL_LOGGER = "Logger must not be null";
	private static final String NULL_XKCD = "xkcd must not be null";

	private final Xkcd xkcd;
	private final Logger logger;

	/**
	 * Construct a new logging xkcd.
	 * 
	 * @param xkcd
	 *            the xkcd to use for comic retrieval
	 */
	@Inject
	public LoggingXkcd(@Delegate final Xkcd xkcd) {
		this(xkcd, Logger.getLogger(LoggingXkcd.class.getCanonicalName()));
	}

	private LoggingXkcd(final Xkcd xkcd, final Logger logger) {
		this.xkcd = Objects.requireNonNull(xkcd, NULL_XKCD);
		this.logger = Objects.requireNonNull(logger, NULL_LOGGER);
	}

	@Override
	public Comic getComic(final int id) throws XkcdException {
		final Comic comic = xkcd.getComic(id);
		logger.info(String.format((comic == null) ? COMIC_NOT_FOUND : COMIC_RETRIEVED, id));
		return comic;
	}

	@Override
	public Comic getCurrentComic() throws XkcdException {
		final Comic comic = xkcd.getCurrentComic();
		logger.info(String.format(CURRENT_COMIC_RETRIEVED, comic.getId()));
		return comic;
	}
}
