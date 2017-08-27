package com.github.thanospapapetrou.xkcd.impl.cache

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.api.Xkcd
import com.github.thanospapapetrou.xkcd.domain.Comic

class CachingXkcdSpec extends Specification {
	private static final int ID = 1024

	void 'Retrieving a commic without cache'() {
		given: 'a caching xkcd without a cache'
			CachingXkcd xkcd = new CachingXkcd(Mock(Xkcd), null)
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved'
			Comic result = xkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying xkcd'
			1 * xkcd.xkcd.getComic(ID) >> comic
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a commic with cache (cache hit)'() {
		given: 'a caching xkcd with a cache'
			CachingXkcd xkcd = new CachingXkcd(Mock(Xkcd), Mock(Cache))
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved'
			Comic result = xkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying cache'
			1 * xkcd.cache.load(ID) >> comic
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving an existing commic with cache (cache miss)'() {
		given: 'a caching xkcd with a cache'
			CachingXkcd xkcd = new CachingXkcd(Mock(Xkcd), Mock(Cache))
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved'
			Comic result = xkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying cache'
			1 * xkcd.cache.load(ID) >> null
		and: 'retrieval is delegated to the underlying xkcd'
			1 * xkcd.xkcd.getComic(ID) >> comic
		and: 'comic is cached'
			1 * xkcd.cache.save(comic)
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a non existing commic with cache (cache miss)'() {
		given: 'a caching xkcd with a cache'
			CachingXkcd xkcd = new CachingXkcd(Mock(Xkcd), Mock(Cache))
		when: 'comic is retrieved'
			Comic result = xkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying cache'
			1 * xkcd.cache.load(ID) >> null
		and: 'retrieval is delegated to the underlying xkcd'
			1 * xkcd.xkcd.getComic(ID) >> null
		and: 'no other interactions happen'
			0 * _
		and: 'null is returned'
			result == null
	}
}

//@Override
//public Comic getCurrentComic() throws XkcdException {
//	if (cache == null) {
//		return xkcd.getCurrentComic();
//	}
//	Comic comic = cache.loadLatest();
//	if ((comic == null) || (nextUpdate(comic.getDate()).before(new Date()))) {
//		LOGGER.fine((comic == null) ? NO_COMIC_FOUND_IN_CACHE : String.format(LATEST_COMIC_FOUND_IN_CACHE_IS_STALE, comic.getId()));
//		final Integer cachedId = (comic == null) ? null : comic.getId();
//		comic = xkcd.getCurrentComic();
//		if ((cachedId == null) || (cachedId != comic.getId())) {
//			try {
//				cache.save(comic);
//				LOGGER.fine(String.format(COMIC_CACHED, comic.getId()));
//			} catch (final XkcdException e) {
//				LOGGER.log(Level.WARNING, String.format(ERROR_CACHING_COMIC, comic.getId()), e);
//			}
//		}
//	} else {
//		LOGGER.fine(String.format(CURRENT_COMIC_RETRIEVED_FROM_CACHE, comic.getId()));
//	}
//	return comic;
//}
