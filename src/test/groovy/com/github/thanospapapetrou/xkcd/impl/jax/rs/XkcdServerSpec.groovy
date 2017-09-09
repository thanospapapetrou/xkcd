package com.github.thanospapapetrou.xkcd.impl.jax.rs

import javax.ws.rs.NotFoundException

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.api.Xkcd
import com.github.thanospapapetrou.xkcd.domain.Comic

class XkcdServerSpec extends Specification {
	private static final int ID = 1024

	private XkcdServer xkcdServer

	void setup() {
		xkcdServer = new XkcdServer(Mock(Xkcd))
	}

	void 'Retrieving an existing comic is delegated to the underlying xkcd'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved'
			Comic result = xkcdServer.getComic(ID)
		then: 'retrieval is delegated to the underlying xkcd'
			1 * xkcdServer.xkcd.getComic(ID) >> comic
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a non existing comic is delegated to the underlying xkcd and a not found exception is thrown'() {
		when: 'comic is retrieved'
			xkcdServer.getComic(ID)
		then: 'retrieval is delegated to the underlying xkcd'
			1 * xkcdServer.xkcd.getComic(ID) >> null
		and: 'no other interactions happen'
			0 * _
		and: 'a not found exception is thrown'
			thrown(NotFoundException)
	}

	void 'Retrieving current comic is delegated to the underlying xkcd'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		when: 'current comic is retrieved'
			Comic result = xkcdServer.currentComic
		then: 'retrieval is delegated to the underlying xkcd'
			1 * xkcdServer.xkcd.currentComic >> comic
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}
}
