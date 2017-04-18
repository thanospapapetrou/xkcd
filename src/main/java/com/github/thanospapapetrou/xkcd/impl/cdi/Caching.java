package com.github.thanospapapetrou.xkcd.impl.cdi;

import com.github.thanospapapetrou.xkcd.impl.cache.Cache;
import com.github.thanospapapetrou.xkcd.impl.cache.jdbc.JdbcCache;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.JpaCache;
import com.github.thanospapapetrou.xkcd.impl.cache.memory.MemoryCache;

/**
 * Enumeration representing the the possible caching modes.
 * 
 * @author thanos
 */
public enum Caching {
	/**
	 * Caching using JDBC.
	 */
	JDBC(JdbcCache.class),

	/**
	 * Caching using JPA.
	 */
	JPA(JpaCache.class),

	/**
	 * Caching in memory.
	 */
	MEMORY(MemoryCache.class),

	/**
	 * No caching.
	 */
	NONE(null);

	private final Class<? extends Cache> implementation;

	private Caching(final Class<? extends Cache> implementation) {
		this.implementation = implementation;
	}

	/**
	 * Get the {@link Cache} implementation corresponding to this caching mode.
	 * 
	 * @return the class implementing {@link Cache} that corresponds to this caching mode
	 */
	public Class<? extends Cache> getImplementation() {
		return implementation;
	}
}
