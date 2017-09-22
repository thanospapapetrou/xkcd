package com.github.thanospapapetrou.xkcd.impl.jax.rs

import java.util.logging.Logger

import javax.ws.rs.NotFoundException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.client.Client
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.WebTarget

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.api.XkcdException
import com.github.thanospapapetrou.xkcd.domain.Comic

class XkcdClientSpec extends Specification {
	private static final URL BASE_URL = new URL('http://www.example.org/')
	private static final int ID = 1024

	private XkcdClient xkcdClient

	void setup() {
		xkcdClient = new XkcdClient(Mock(Client), Mock(WebTarget), Mock(Logger))
	}

	void 'Closing an xkcd client closes the underlying JAX-RS client'() {
		when: 'xkcd client is closed'
			xkcdClient.close()
		then: 'the underlying JAX-RS client is closed'
			1 * xkcdClient.client.close()
		and: 'no other interactions happen'
			0 * _
	}

	void 'Retrieving an existing comic'() {
		given: 'a JAX-RS target'
			WebTarget target = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved by ID'
			Comic result = xkcdClient.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcdClient.target.path(XkcdClient.GET_COMIC) >> target
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target.resolveTemplate(XkcdClient.ID, ID) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> comic
		and: 'the JAX-RS target URI is retrieved'
			1 * target.uri >> BASE_URL.toURI()
		and: 'a comic retrieved from message is logged as fine'
			1 * xkcdClient.logger.fine(String.format(XkcdClient.COMIC_RETRIEVED_FROM, ID, BASE_URL.toURI()))
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a non existing comic'() {
		given: 'a JAX-RS target'
			WebTarget target = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		when: 'comic is retrieved by ID'
			Comic result = xkcdClient.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcdClient.target.path(XkcdClient.GET_COMIC) >> target
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target.resolveTemplate(XkcdClient.ID, ID) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation fails'
			1 * invocation.invoke(Comic) >> { throw Mock(NotFoundException) }
		and: 'the JAX-RS target URI is retrieved'
			1 * target.uri >> BASE_URL.toURI()
		and: 'a comic not found message is logged as fine'
			1 * xkcdClient.logger.fine(String.format(XkcdClient.COMIC_NOT_FOUND, ID, BASE_URL.toURI()))
		and: 'no other interactions happen'
			0 * _
		and: 'null is returned'
			result == null
	}

	void 'Error retrieving a comic'() {
		given: 'a JAX-RS target'
			WebTarget target = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a web application exception'
			WebApplicationException webApplicationException = Mock(WebApplicationException)
		when: 'comic is retrieved by ID'
			xkcdClient.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcdClient.target.path(XkcdClient.GET_COMIC) >> target
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target.resolveTemplate(XkcdClient.ID, ID) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation fails'
			1 * invocation.invoke(Comic) >> { throw webApplicationException }
		and: 'the JAX-RS target URI is retrieved'
			1 * target.uri >> BASE_URL.toURI()
		and: 'no other interactions happen'
			0 * _
		and: 'an xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
		and: 'exception message is an error retrieving comic message'
			e.message == String.format(XkcdClient.ERROR_RETRIEVING_COMIC, ID, BASE_URL.toURI())
		and: 'exception cause is the web application exception'
			e.cause == webApplicationException
	}

	void 'Retrieving current comic'() {
		given: 'a JAX-RS target'
			WebTarget target = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved by ID'
			Comic result = xkcdClient.currentComic
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcdClient.target.path(XkcdClient.GET_CURRENT_COMIC) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> comic
		and: 'the comic ID is retrieved'
			1 * comic.id >> ID
		and: 'the JAX-RS target URI is retrieved'
			1 * target.uri >> BASE_URL.toURI()
		and: 'a current comic retrieved from message is logged as fine'
			1 * xkcdClient.logger.fine(String.format(XkcdClient.CURRENT_COMIC_RETRIEVED_FROM, ID, BASE_URL.toURI()))
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Error retrieving current comic'() {
		given: 'a JAX-RS target'
			WebTarget target = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a web application exception'
			WebApplicationException webApplicationException = Mock(WebApplicationException)
		when: 'comic is retrieved by ID'
			xkcdClient.currentComic
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcdClient.target.path(XkcdClient.GET_CURRENT_COMIC) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation fails'
			1 * invocation.invoke(Comic) >> { throw webApplicationException }
		and: 'the JAX-RS target URI is retrieved'
			1 * target.uri >> BASE_URL.toURI()
		and: 'no other interactions happen'
			0 * _
		and: 'an xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
		and: 'exception message is an error retrieving current comic message'
			e.message == String.format(XkcdClient.ERROR_RETRIEVING_CURRENT_COMIC, BASE_URL.toURI())
		and: 'exception cause is the web application exception'
			e.cause == webApplicationException
	}
}
