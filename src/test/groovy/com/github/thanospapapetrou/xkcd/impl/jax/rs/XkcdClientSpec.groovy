package com.github.thanospapapetrou.xkcd.impl.jax.rs

import java.util.logging.Logger

import javax.ws.rs.NotFoundException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.client.Client
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.WebTarget

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.SetterUtils
import com.github.thanospapapetrou.xkcd.api.XkcdException
import com.github.thanospapapetrou.xkcd.domain.Comic

class XkcdClientSpec extends Specification implements SetterUtils {
	private static final URL BASE_URL = new URL('http://www.example.org/')
	private static final String CLIENT = 'client'
	private static final int ID = 1024
	private static final String LOGGER = 'LOGGER'
	private static final String TARGET = 'target'
	private static final URI URI = BASE_URL.toURI()

	private XkcdClient xkcd

	void setup() {
		setStaticFinal(XkcdClient, LOGGER, Mock(Logger))
		xkcd = new XkcdClient()
		setFinal(xkcd, CLIENT, Mock(Client))
		setFinal(xkcd, TARGET, Mock(WebTarget))
	}

	void 'Constructing an xkcd client using a base URL'() {
		when: 'an xkcd client is constructed using a base URL'
			XkcdClient client = new XkcdClient(BASE_URL)
		then: 'the underlying JAX-RS client has only one comic message body reader registered'
			client.client.configuration.instances.size() == 1
			Object object = client.client.configuration.instances.toList()[0]
			object.class == ComicMessageBodyReader
		and: 'the the comic message body reader base URL is the base URL used to construct the xkcd client'
			object.baseUrl == BASE_URL
		and: 'the underlying JAX-RS target URI is the base URL used to construct the xkcd client '
			client.target.uri == BASE_URL.toURI()
	}

	void 'Closing an xkcd client closes the underlying JAX-RS client'() {
		when: 'xkcd client is closed'
			xkcd.close()
		then: 'the underlying JAX-RS client is closed'
			1 * xkcd.client.close()
		and: 'no other interactions happen'
			0 * _
	}

	void 'Retrieving an existing comic'() {
		given: 'a JAX-RS target'
			WebTarget target1 = Mock(WebTarget)
		and: 'another JAX-RS target'
			WebTarget target2 = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a comic'
			Comic comic = Mock(Comic)
		when: 'comic is retrieved by ID'
			Comic result = xkcd.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcd.target.path(XkcdClient.GET_COMIC) >> target1
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target1.resolveTemplate(XkcdClient.ID, ID) >> target2
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target2.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> comic
		and: 'the second JAX-RS target URI is retrieved'
			1 * target2.uri >> URI
		and: 'a comic retrieved message is logged as fine'
			1 * XkcdClient.LOGGER.fine(String.format(XkcdClient.COMIC_RETRIEVED_FROM, ID, URI))
		and: 'no other interactions happen'
			0 * _
		and: 'comic is returned'
			result == comic
	}

	void 'Retrieving a non existing comic'() {
		given: 'a JAX-RS target'
			WebTarget target1 = Mock(WebTarget)
		and: 'another JAX-RS target'
			WebTarget target2 = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		when: 'comic is retrieved by ID'
			Comic result = xkcd.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcd.target.path(XkcdClient.GET_COMIC) >> target1
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target1.resolveTemplate(XkcdClient.ID, ID) >> target2
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target2.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> { throw Mock(NotFoundException) }
		and: 'the second JAX-RS target URI is retrieved'
			1 * target2.uri >> URI
		and: 'a comic not found message is logged as fine'
			1 * XkcdClient.LOGGER.fine(String.format(XkcdClient.COMIC_NOT_FOUND, ID, URI))
		and: 'no other interactions happen'
			0 * _
		and: 'null is returned'
			result == null
	}

	void 'Error retrieving a comic'() {
		given: 'a JAX-RS target'
			WebTarget target1 = Mock(WebTarget)
		and: 'another JAX-RS target'
			WebTarget target2 = Mock(WebTarget)
		and: 'a JAX-RS invocation builder'
			Invocation.Builder invocationBuilder = Mock(Invocation.Builder)
		and: 'a JAX-RS invocation'
			Invocation invocation = Mock(Invocation)
		and: 'a web application exception'
			WebApplicationException webApplicationException = Mock(WebApplicationException)
		when: 'comic is retrieved by ID'
			xkcd.getComic(ID)
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcd.target.path(XkcdClient.GET_COMIC) >> target1
		and: 'a new JAX-RS target is created by resolving the ID URI template against the previous JAX-RS target'
			1 * target1.resolveTemplate(XkcdClient.ID, ID) >> target2
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target2.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> { throw webApplicationException }
		and: 'the second JAX-RS target URI is retrieved'
			1 * target2.uri >> URI
		and: 'no other interactions happen'
			0 * _
		and: 'an xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
		and: 'exception message is an error retrieving comic message'
			e.message == String.format(XkcdClient.ERROR_RETRIEVING_COMIC, ID, URI)
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
			Comic result = xkcd.currentComic
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcd.target.path(XkcdClient.GET_CURRENT_COMIC) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> comic
		and: 'the comic ID is retrieved'
			1 * comic.id >> ID
		and: 'the second JAX-RS target URI is retrieved'
			1 * target.uri >> URI
		and: 'a current comic retrieved message is logged as fine'
			1 * XkcdClient.LOGGER.fine(String.format(XkcdClient.CURRENT_COMIC_RETRIEVED_FROM, ID, URI))
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
			xkcd.currentComic
		then: 'a new JAX-RS target is created by appending the get comic path to the underlying JAX-RS target'
			1 * xkcd.target.path(XkcdClient.GET_CURRENT_COMIC) >> target
		and: 'a JAX-RS builder is created with media type \'application/json;charset=UTF-8\''
			1 * target.request(XkcdClient.APPLICATION_JSON_CHARSET_UTF_8) >> invocationBuilder
		and: 'a JAX-RS GET invocation is created'
			1 * invocationBuilder.buildGet() >> invocation
		and: 'invocation is invoked'
			1 * invocation.invoke(Comic) >> { throw webApplicationException }
		and: 'the second JAX-RS target URI is retrieved'
			1 * target.uri >> URI
		and: 'no other interactions happen'
			0 * _
		and: 'an xkcd exception is thrown'
			XkcdException e = thrown(XkcdException)
		and: 'exception message is an error retrieving current comic message'
			e.message == String.format(XkcdClient.ERROR_RETRIEVING_CURRENT_COMIC, URI)
		and: 'exception cause is the web application exception'
			e.cause == webApplicationException
	}
}
