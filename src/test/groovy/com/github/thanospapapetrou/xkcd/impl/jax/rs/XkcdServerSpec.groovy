package com.github.thanospapapetrou.xkcd.impl.jax.rs

import javax.ws.rs.NotFoundException

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.api.Xkcd
import com.github.thanospapapetrou.xkcd.api.XkcdException
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
		then: 'underlying xkcd returns no comic'
			1 * xkcdServer.xkcd.getComic(ID) >> null
		and: 'no other interactions happen'
			0 * _
		and: 'a not found exception is thrown'
			thrown(NotFoundException)
	}

	void 'Error retrieving a comic'() {
		given: 'an xkcd exception'
			XkcdException xkcdException = Mock(XkcdException)
		when: 'comic is retrieved'
			xkcdServer.getComic(ID)
		then: 'underlying xkcd fails to retrieve comic'
			1 * xkcdServer.xkcd.getComic(ID) >> { throw xkcdException }
		and: 'no other interactions happen'
			0 * _
		and: 'xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
			e == xkcdException
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

	void 'Error retrieving current comic'() {
		given: 'an xkcd exception'
			XkcdException xkcdException = Mock(XkcdException)
		when: 'current comic is retrieved'
			xkcdServer.currentComic
		then: 'underlying xkcd fails to retrieve current comic'
			1 * xkcdServer.xkcd.currentComic >> { throw xkcdException }
		and: 'no other interactions happen'
			0 * _
		and: 'xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
			e == xkcdException
	}
}
