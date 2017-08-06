package com.github.thanospapapetrou.xkcd.impl

import java.util.logging.Logger

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.SetterUtils
import com.github.thanospapapetrou.xkcd.api.Xkcd
import com.github.thanospapapetrou.xkcd.domain.Comic

class LoggingXkcdSpec extends Specification implements SetterUtils { // TODO improve testability
	private static final int ID = 1024
	private static final String LOGGER = 'LOGGER'

	private LoggingXkcd loggingXkcd

	void setup() {
		setStaticFinal(LoggingXkcd, LOGGER, Mock(Logger))
		loggingXkcd = new LoggingXkcd(Mock(Xkcd))
	}

	void 'Retrieving an existing comic is delegated to the underlying xkcd and a comic retrieved message is logged as info'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved'
			Comic result = loggingXkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying xkcd'
			1 * loggingXkcd.xkcd.getComic(ID) >> comic
		and: 'a comic retrieved message is logged as info'
			1 * LoggingXkcd.LOGGER.info(String.format(LoggingXkcd.COMIC_RETRIEVED, ID))
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a non existing comic is delegated to the underlying xkcd and a comic not found message is logged as info'() {
		when: 'comic is retrieved'
			Comic result = loggingXkcd.getComic(ID)
		then: 'retrieval is delegated to the underlying xkcd'
			1 * loggingXkcd.xkcd.getComic(ID) >> null
		and: 'a comic not found message is logged as info'
			1 * LoggingXkcd.LOGGER.info(String.format(LoggingXkcd.COMIC_NOT_FOUND, ID))
		and: 'no other interactions happen'
			0 * _
		and: 'null is returned'
			result == null
	}

	void 'Retrieving current comic is delegated to the underlying xkcd and a current comic retrieved message is logged as info'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		when: 'current comic is retrieved'
			Comic result = loggingXkcd.currentComic
		then: 'retrieval is delegated to the underlying xkcd'
			1 * loggingXkcd.xkcd.currentComic >> comic
		and: 'the comic ID is retrieved'
			1 * comic.id >> ID
		and: 'a current comic retrieved message is logged as info'
			1 * LoggingXkcd.LOGGER.info(String.format(LoggingXkcd.CURRENT_COMIC_RETRIEVED, ID))
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}
}
