package com.github.thanospapapetrou.xkcd.api;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Interface representing the xkcd API.
 * 
 * @author thanos
 * @see <a href="https://xkcd.com/json.html">xkcd JSON API</a>
 */
public interface Xkcd {
	/**
	 * Get a specific xkcd comic.
	 * 
	 * @param id
	 *            the ID of the comic to retrieve
	 * @return the comic specified or <code>null</code> if it doesn't exist
	 * @throws XkcdException
	 *             if any errors occur while retrieving the comic specified
	 */
	public abstract Comic getComic(final int id) throws XkcdException;

	/**
	 * Get the current xkcd comic.
	 * 
	 * @return the current comic
	 * @throws XkcdException
	 *             if any errors occur while retrieving current comic
	 */
	public abstract Comic getCurrentComic() throws XkcdException;
}
