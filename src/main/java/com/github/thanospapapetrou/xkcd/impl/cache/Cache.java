package com.github.thanospapapetrou.xkcd.impl.cache;

import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Interface representing a cache for xkcd comics.
 * 
 * @author thanos
 */
public interface Cache {
	/**
	 * Save a comic to the cache.
	 * 
	 * @param comic
	 *            the comic to save
	 * @throws XkcdException
	 *             if any errors occur while saving the comic specified
	 */
	public void save(final Comic comic) throws XkcdException;

	/**
	 * Load a comic from the cache.
	 * 
	 * @param id
	 *            the ID of the comic to load
	 * @return the comic specified or <code>null</code> if it can't be found in the cache
	 * @throws XkcdException
	 *             if any errors occur while retrieving the comic specified
	 */
	public Comic load(final int id) throws XkcdException;

	/**
	 * Load the latest comic from the cache.
	 * 
	 * @return the latest comic in the cache or <code>null</code> if the cache is empty
	 * @throws XkcdException
	 *             if any errors occur while retrieving the latest comic
	 */
	public Comic loadLatest() throws XkcdException;
}
