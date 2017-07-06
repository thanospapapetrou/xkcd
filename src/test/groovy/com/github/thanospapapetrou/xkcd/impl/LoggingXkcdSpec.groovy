package com.github.thanospapapetrou.xkcd.impl

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.logging.Logger

import com.github.thanospapapetrou.xkcd.api.Xkcd
import com.github.thanospapapetrou.xkcd.domain.Comic

import spock.lang.Specification

class LoggingXkcdSpec extends Specification {
	private static final String LOGGER = 'LOGGER'
	private static final String MODIFIERS = 'modifiers'

	private static final int ID = 1024

	private LoggingXkcd loggingXkcd

	void setup() {
		Field logger = LoggingXkcd.getDeclaredField(LOGGER)
		logger.accessible = true
		Field modifiers = Field.getDeclaredField(MODIFIERS)
		modifiers.accessible = true
		modifiers.setInt(logger, logger.modifiers & (~Modifier.FINAL))
		logger.set(null, Mock(Logger))
		loggingXkcd = new LoggingXkcd(Mock(Xkcd))
	}

	void 'retrieving an existing comic is delegated to the underlying xkcd and a comic retrieved message is logged as info'() {
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

	void 'retrieving a non existing comic is delegated to the underlying xkcd and a comic not found message is logged as info'() {
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

	void 'retrieving current comic is delegated to the underlying xkcd and a current comic retrieved message is logged as info'() {
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
