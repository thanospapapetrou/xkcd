package com.github.thanospapapetrou.xkcd.impl.cache.memory;

import java.util.Collections;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;

import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cache.Cache;

/**
 * Class implementing a cache in memory. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@ApplicationScoped
public class MemoryCache implements Cache {
	private static final String NULL_COMIC = "Comic must not be null";

	private final SortedMap<Integer, Comic> cache;

	/**
	 * Construct a new memory cache.
	 */
	public MemoryCache() {
		cache = Collections.synchronizedSortedMap(new TreeMap<Integer, Comic>());
	}

	@Override
	public void save(final Comic comic) {
		Objects.requireNonNull(comic, NULL_COMIC);
		cache.put(comic.getId(), comic);
	}

	@Override
	public Comic load(final int id) {
		return cache.get(id);
	}

	@Override
	public Comic loadLatest() {
		return cache.isEmpty() ? null : cache.get(cache.lastKey());
	}
}
