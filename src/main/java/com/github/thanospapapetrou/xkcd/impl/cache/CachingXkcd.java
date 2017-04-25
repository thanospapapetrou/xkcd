package com.github.thanospapapetrou.xkcd.impl.cache;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import com.github.thanospapapetrou.xkcd.api.Xkcd;
import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cdi.Selector;

/**
 * Decorator extending an xkcd implementation with caching. Instances of this class are thread-safe, provided they are constructed with a thread-safe xkcd and cache.
 * 
 * @author thanos
 */
@Decorator
public class CachingXkcd implements Xkcd {
	private static final String COMIC_CACHED = "Comic %1$d cached";
	private static final String COMIC_NOT_FOUND_IN_CACHE = "Comic %1$d not found in cache";
	private static final String COMIC_RETRIEVED_FROM_CACHE = "Comic %1$d retrieved from cache";
	private static final String CURRENT_COMIC_RETRIEVED_FROM_CACHE = "Current comic (%1$d) retrieved from cache";
	private static final String ERROR_CACHING_COMIC = "Error caching comic %1$d";
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT+00:00");
	private static final Logger LOGGER = Logger.getLogger(CachingXkcd.class.getCanonicalName());
	private static final String LATEST_COMIC_FOUND_IN_CACHE_IS_STALE = "Latest comic found in cache (%1$d) is stale";
	private static final String NO_COMIC_FOUND_IN_CACHE = "No comic found in cache";
	private static final String NULL_XKCD = "xkcd must not be null";
	private static final List<Integer> UPDATE_DAYS = Arrays.asList(GregorianCalendar.MONDAY, GregorianCalendar.WEDNESDAY, GregorianCalendar.FRIDAY);

	private final Xkcd xkcd;
	private final Cache cache;

	/**
	 * Construct a new caching xkcd.
	 * 
	 * @param xkcd
	 *            the xkcd to use for comic retrieval
	 * @param cache
	 *            the cache to use for already retrieved comics
	 */
	@Inject
	public CachingXkcd(@Delegate final Xkcd xkcd, @Selector final Cache cache) {
		this.xkcd = Objects.requireNonNull(xkcd, NULL_XKCD);
		this.cache = cache;
	}

	private static Date nextUpdate(final Date date) {
		final Calendar calendar = new GregorianCalendar(GMT, Locale.ROOT);
		calendar.setTime(date);
		do {
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		} while (!UPDATE_DAYS.contains(calendar.get(Calendar.DAY_OF_WEEK)));
		return calendar.getTime();
	}

	@Override
	public Comic getComic(final int id) throws XkcdException {
		if (cache == null) {
			return xkcd.getComic(id);
		}
		Comic comic = cache.load(id);
		if (comic == null) {
			LOGGER.fine(String.format(COMIC_NOT_FOUND_IN_CACHE, id));
			comic = xkcd.getComic(id);
			if (comic != null) {
				try {
					cache.save(comic);
					LOGGER.fine(String.format(COMIC_CACHED, id));
				} catch (final XkcdException e) {
					LOGGER.log(Level.WARNING, String.format(ERROR_CACHING_COMIC, id), e);
				}
			}
		} else {
			LOGGER.fine(String.format(COMIC_RETRIEVED_FROM_CACHE, id));
		}
		return comic;
	}

	@Override
	public Comic getCurrentComic() throws XkcdException {
		if (cache == null) {
			return xkcd.getCurrentComic();
		}
		Comic comic = cache.loadLatest();
		if ((comic == null) || (nextUpdate(comic.getDate()).before(new Date()))) {
			LOGGER.fine((comic == null) ? NO_COMIC_FOUND_IN_CACHE : String.format(LATEST_COMIC_FOUND_IN_CACHE_IS_STALE, comic.getId()));
			final Integer cachedId = (comic == null) ? null : comic.getId();
			comic = xkcd.getCurrentComic();
			if ((cachedId == null) || (cachedId != comic.getId())) {
				try {
					cache.save(comic);
					LOGGER.fine(String.format(COMIC_CACHED, comic.getId()));
				} catch (final XkcdException e) {
					LOGGER.log(Level.WARNING, String.format(ERROR_CACHING_COMIC, comic.getId()), e);
				}
			}
		} else {
			LOGGER.fine(String.format(CURRENT_COMIC_RETRIEVED_FROM_CACHE, comic.getId()));
		}
		return comic;
	}
}
